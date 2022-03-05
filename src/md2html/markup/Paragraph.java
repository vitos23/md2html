package md2html.markup;

import java.util.List;

public class Paragraph extends ParentElement implements Element {
    public Paragraph(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<p>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</p>";
    }
}
