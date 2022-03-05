package md2html.markup;

import java.util.List;

public class Strikeout extends MarkupElement {
    public Strikeout(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbols() {
        return "~";
    }

    @Override
    protected String getEndingSymbols() {
        return "~";
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<s>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</s>";
    }
}
