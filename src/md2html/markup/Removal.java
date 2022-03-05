package md2html.markup;

import java.util.List;

public class Removal extends MarkupElement {
    public Removal(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<del>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</del>";
    }
}
