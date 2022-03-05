package md2html.markup;

import java.util.List;

public class Code extends ParentElement implements MarkupAndText {
    public Code(List<MarkupAndText> innerElements) {
        super(innerElements);
    }

    @Override
    protected String getStartingSymbolsHtml() {
        return "<code>";
    }

    @Override
    protected String getEndingSymbolsHtml() {
        return "</code>";
    }
}
