package md2html.markup;

public class Text implements Element, MarkupAndText {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder res) {
        res.append(text);
    }

    @Override
    public void toHtml(StringBuilder res) {
        res.append(text);
    }
}
