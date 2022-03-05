package md2html.markup;

import java.util.List;

public class Strong extends MarkupElement {
    public Strong(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbols() {
        return "__";
    }

    @Override
    protected String getEndingSymbols() {
        return "__";
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<strong>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</strong>";
    }
}
