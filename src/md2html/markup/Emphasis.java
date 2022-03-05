package md2html.markup;

import java.util.List;

public class Emphasis extends MarkupElement {
    public Emphasis(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbols() {
        return "*";
    }

    @Override
    protected String getEndingSymbols() {
        return "*";
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<em>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</em>";
    }
}
