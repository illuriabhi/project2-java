import org.apache.commons.text.similarity.CosineSimilarity;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentSimilarityTool {

    public static void main(String[] args) throws Exception {
        // Input files
        String file1 = "doc1.txt";
        String file2 = "doc2.txt";
        String stopwordsFile = "stopwords.txt";

        // Read and tokenize
        String text1 = Files.readString(Paths.get(file1));
        String text2 = Files.readString(Paths.get(file2));

        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens1 = tokenizer.tokenize(text1);
        String[] tokens2 = tokenizer.tokenize(text2);

        // Load stopwords
        Set<String> stopwords = new HashSet<>(Files.readAllLines(Paths.get(stopwordsFile)));

        // Clean and filter
        List<String> clean1 = cleanTokens(tokens1, stopwords);
        List<String> clean2 = cleanTokens(tokens2, stopwords);

        // Compute frequency maps
        Map<CharSequence, Integer> freq1 = getFrequency(clean1);
        Map<CharSequence, Integer> freq2 = getFrequency(clean2);

        // Compute cosine similarity
        CosineSimilarity cosine = new CosineSimilarity();
        double similarity = cosine.cosineSimilarity(freq1, freq2);

        // Get matching words
        Set<String> common = new HashSet<>(clean1);
        common.retainAll(clean2);

        // Display results
        System.out.printf("Similarity: %.2f%%\n", similarity * 100);
        System.out.println("Matching Words: " + common);

        // Export
        exportText(similarity, common);
        exportPDF(similarity, common);
    }

    private static List<String> cleanTokens(String[] tokens, Set<String> stopwords) {
        return Arrays.stream(tokens)
            .map(String::toLowerCase)
            .filter(t -> t.matches("[a-z]+"))
            .filter(t -> !stopwords.contains(t))
            .collect(Collectors.toList());
    }

    private static Map<CharSequence, Integer> getFrequency(List<String> tokens) {
        Map<CharSequence, Integer> freq = new HashMap<>();
        for (String token : tokens)
            freq.put(token, freq.getOrDefault(token, 0) + 1);
        return freq;
    }

    private static void exportText(double similarity, Set<String> common) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(String.format("Similarity: %.2f%%", similarity * 100));
        lines.add("Matching Words: " + common);
        Files.write(Paths.get("similarity_results.txt"), lines);
        System.out.println("Text results saved to similarity_results.txt");
    }

    private static void exportPDF(double similarity, Set<String> common) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        PDPageContentStream out = new PDPageContentStream(doc, page);
        out.beginText();
        out.setFont(PDType1Font.HELVETICA, 12);
        out.setLeading(14.5f);
        out.newLineAtOffset(50, 750);

        out.showText(String.format("Similarity Score: %.2f%%", similarity * 100));
        out.newLine();
        out.showText("Matching Words: " + common);
        out.endText();
        out.close();

        doc.save("similarity_report.pdf");
        doc.close();

        System.out.println("PDF report saved to similarity_report.pdf");
    }
}
