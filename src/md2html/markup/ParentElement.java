package md2html.markup;

import java.util.List;

public abstract class ParentElement implements Element {
    private final List<? extends Element> innerElements;

    public ParentElement(List<? extends Element> innerElements) {
        this.innerElements = innerElements;
    }

    @Override
    public void toMarkdown(StringBuilder res) {
        res.append(getStartingSymbols());
        for (MarkdownElement inner : innerElements) {
            inner.toMarkdown(res);
        }
        res.append(getEndingSymbols());
    }

    @Override
    public void toHtml(StringBuilder res) {
        res.append(getStartingSymbolsHtml());
        for (HtmlElement inner : innerElements) {
            inner.toHtml(res);
        }
        res.append(getEndingSymbolsHtml());
    }

    protected String getStartingSymbols() {
        return "";
    }

    protected String getEndingSymbols() {
        return "";
    }

    protected String getStartingSymbolsHtml() {
        return "";
    }

    protected String getEndingSymbolsHtml() {
        return "";
    }
}
