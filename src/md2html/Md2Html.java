package md2html;

import md2html.markup.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Expected 2 arguments: <input file> <output file>");
            return;
        }

        String input = args[0];
        String output = args[1];

        try {
            try (MarkdownParser parser = new MarkdownParser(new FileInputStream(input), StandardCharsets.UTF_8)) {
                Document document = parser.parse();
                StringBuilder res = new StringBuilder();
                document.toHtml(res);

                try {
                    try (FileWriter writer = new FileWriter(output, StandardCharsets.UTF_8)) {
                        writer.write(res.toString());
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while writing output file!");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file wasn't found!");
        } catch (IOException e) {
            System.out.println("An error occurred while reading input file!");
        }
    }
}
