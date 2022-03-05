package md2html.markup;

public class LineSeparator implements MarkupAndText {
    private final String separator;

    public LineSeparator(String separator) {
        this.separator = separator;
    }

    public LineSeparator() {
        this(System.lineSeparator());
    }

    @Override
    public void toHtml(StringBuilder res) {
        res.append(separator);
    }

    @Override
    public void toMarkdown(StringBuilder res) {
        res.append(separator);
    }
}
