package md2html;

import md2html.markup.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class MarkdownParser implements AutoCloseable {
    private final BufferedReader reader;
    private String line;
    private int pos;

    private Element cachedNextBlock;

    private boolean isOpen = true;

    private enum InlineElement {EMPHASIS, STRONG, STRIKEOUT, CODE, INSERTION, REMOVAL}

    public MarkdownParser(InputStream source, Charset charset) {
        reader = new BufferedReader(new InputStreamReader(source, charset));
    }

    private void updateLine() throws IOException {
        if (line != null && pos >= line.length()) {
            line = reader.readLine();
            pos = 0;
        }
    }

    private boolean isEmphasisSymbol(char c) {
        return c == '*' || c == '_';
    }

    private boolean isEmphasisStart() {
        if (isEmphasisSymbol(line.charAt(pos)) &&
                (pos + 1 == line.length() || line.charAt(pos + 1) != line.charAt(pos))) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean isStrongStart() {
        if (line.length() - pos >= 2 && isEmphasisSymbol(line.charAt(pos)) && isEmphasisSymbol(line.charAt(pos + 1))) {
            pos += 2;
            return true;
        }
        return false;
    }

    private boolean twoCharChecker(char c) {
        if (line.length() - pos >= 2 && line.charAt(pos) == c && line.charAt(pos + 1) == c) {
            pos += 2;
            return true;
        }
        return false;
    }

    private boolean oneCharChecker(char c) {
        if (line.charAt(pos) == c) {
            pos++;
            return true;
        }
        return false;
    }

    private String escapeSymbol(char c) {
        switch (c) {
            case '>':
                return "&gt;";
            case '<':
                return "&lt;";
            case '&':
                return "&amp;";
            default:
                return c + "";
        }
    }

    private char readNextSymbol() {
        char c = line.charAt(pos++);
        if (c == '\\' && pos < line.length()) { // && isMarkupSpecialSymbol(line.charAt(pos))) {
            return line.charAt(pos++);
        }
        return c;
    }

    private InlineElement getInlineElementPart() {
        if (isEmphasisStart()) {
            return InlineElement.EMPHASIS;
        } else if (isStrongStart()) {
            return InlineElement.STRONG;
        } else if (twoCharChecker('-')) {
            return InlineElement.STRIKEOUT;
        } else if (oneCharChecker('`')) {
            return InlineElement.CODE;
        } else if (twoCharChecker('<') || twoCharChecker('>')) {
            return InlineElement.INSERTION;
        } else if (twoCharChecker('}') || twoCharChecker('{')) {
            return InlineElement.REMOVAL;
        }
        return null;
    }

    private MarkupAndText createInlineElement(InlineElement element, List<MarkupAndText> content) {
        MarkupAndText res = null;
        switch (element) {
            case EMPHASIS:
                res = new Emphasis(content);
                break;
            case STRONG:
                res = new Strong(content);
                break;
            case STRIKEOUT:
                res = new Strikeout(content);
                break;
            case CODE:
                res = new Code(content);
                break;
            case INSERTION:
                res = new Insertion(content);
                break;
            case REMOVAL:
                res = new Removal(content);
                break;
        }
        return res;
    }

    private String getPairPart(String part) {
        switch (part) {
            case ">>":
                return "<<";
            case "<<":
                return ">>";
            case "{{":
                return "}}";
            case "}}":
                return "{{";
            default:
                return part;
        }
    }

    private boolean tryParseNextText(Stack<String> openWrappers, List<MarkupAndText> result) throws IOException {
        StringBuilder curText = new StringBuilder();
        while (line != null && pos < line.length()) {
            int cachedPos = pos;
            InlineElement mdBlockPart = getInlineElementPart();
            if (mdBlockPart != null) {
                String blockString = line.substring(cachedPos, pos);

                if (curText.length() > 0) {
                    result.add(new Text(curText.toString()));
                    curText = new StringBuilder();
                }

                int posInStack = openWrappers.search(getPairPart(blockString));
                if (posInStack != -1) {
                    if (posInStack != 1) {
                        pos = cachedPos;
                        return false;
                    }
                    return true;
                }

                openWrappers.push(blockString);

                List<MarkupAndText> nextParsedContent = new ArrayList<>();
                if (!tryParseNextText(openWrappers, nextParsedContent)) {
                    result.add(new Text(blockString));
                    result.addAll(nextParsedContent);
                } else {
                    result.add(createInlineElement(mdBlockPart, nextParsedContent));
                }
                openWrappers.pop();
            } else {
                curText.append(escapeSymbol(readNextSymbol()));
            }
            if (line != null && !line.isEmpty() && pos >= line.length()) {
                result.add(new Text(curText.toString()));
                result.add(new LineSeparator());
                curText = new StringBuilder();
                updateLine();
            }
        }
        return openWrappers.isEmpty();
    }

    private List<MarkupAndText> parseText() throws IOException {
        Stack<String> openWrappers = new Stack<>();
        List<MarkupAndText> result = new ArrayList<>();
        tryParseNextText(openWrappers, result);
        result.remove(result.size() - 1);
        return result;
    }

    private Paragraph parseParagraph() throws IOException {
        return new Paragraph(parseText());
    }

    private int getHeadingLevel() {
        int headingLevel = 0;
        while (pos < line.length() && line.charAt(pos) == '#') {
            ++headingLevel;
            ++pos;
        }
        return headingLevel;
    }

    private boolean isHeading() {
        int cachedPos = pos;
        int headingLevel = getHeadingLevel();
        if (headingLevel > 0 && pos < line.length() && Character.isWhitespace(line.charAt(pos))) {
            pos = cachedPos;
            return true;
        }
        pos = cachedPos;
        return false;
    }

    private Heading parseHeading() throws IOException {
        int headingLevel = getHeadingLevel();
        pos++;
        return new Heading(parseText(), headingLevel);
    }

    private Element parseNextBlock() throws IOException {
        line = reader.readLine();
        while (line != null && line.isEmpty()) {
            line = reader.readLine();
        }

        if (line == null) {
            return null;
        }

        // heading
        if (isHeading()) {
            return parseHeading();
        }

        // paragraph
        return parseParagraph();
    }

    private boolean hasNextBlock() throws IOException {
        if (cachedNextBlock == null) {
            cachedNextBlock = parseNextBlock();
        }
        return cachedNextBlock != null;
    }

    private Element nextBlock() throws IOException {
        if (hasNextBlock()) {
            Element res = cachedNextBlock;
            cachedNextBlock = null;
            return res;
        }
        throw new NoSuchElementException("Next block is absent!");
    }

    private void ensureOpen() {
        if (!isOpen) {
            throw new IllegalStateException("Markdown Parser is closed!");
        }
    }

    public Document parse() throws IOException {
        ensureOpen();

        List<Element> content = new ArrayList<>();

        while (hasNextBlock()) {
            content.add(nextBlock());
            content.add(new LineSeparator());
        }

        return new Document(content);
    }

    @Override
    public void close() throws IOException {
        ensureOpen();
        reader.close();
        isOpen = false;
    }
}
