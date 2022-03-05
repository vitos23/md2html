package md2html;

import md2html.markup.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class MyTest {
    public static void main(String[] args) throws IOException {
        String text = "}}please}}remove{{{me{{";
        InputStream is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MarkdownParser parser = new MarkdownParser(is, StandardCharsets.UTF_8);
        Document document = parser.parse();
        StringBuilder res = new StringBuilder();
        document.toHtml(res);
        System.out.println(res);
    }
}
