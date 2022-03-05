package md2html.markup;

import java.util.List;

public abstract class MarkupElement extends ParentElement implements MarkupAndText{
    public MarkupElement(List<MarkupAndText> innerElements) {
        super(innerElements);
    }
}
