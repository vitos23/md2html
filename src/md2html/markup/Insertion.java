package md2html.markup;

import java.util.List;

public class Insertion extends MarkupElement {
    public Insertion(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<ins>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</ins>";
    }
}
