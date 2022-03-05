package md2html.markup;

import java.util.List;

public class Heading extends ParentElement {
    private final int headingLevel;

    public Heading(List<MarkupAndText> innerElements, int headingLevel) {
        super(innerElements);
        this.headingLevel = headingLevel;
    }

    @Override
    protected String getStartingSymbols() {
        return "#".repeat(headingLevel);
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<h" + headingLevel + ">";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</h" + headingLevel + ">";
    }
}
