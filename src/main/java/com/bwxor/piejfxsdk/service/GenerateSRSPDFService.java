package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.state.SRSDocumentState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GenerateSRSPDFService {
    private static final float TOP_MARGIN = 750f;
    private static final float BOTTOM_MARGIN = 50f;
    private static final float LEFT_MARGIN = 50f;
    private static final float RIGHT_MARGIN = 50f;
    private static final float LINE_HEIGHT = 18f;

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private final PDFont fontRoman = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
    private final PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);

    private PDPage currentPage;
    private PDPageContentStream currentStream;
    private float currentY;

    public boolean generatePdf(String outputFolderPath) {
        try (PDDocument document = new PDDocument()) {

            addTitleToDocument(document);
            addRevisionHistoryToDocument(document);
            addIntroductionToDocument(document);

            closeCurrentStream();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "generated-" + timestamp + ".pdf";

            Path fullPath = Paths.get(outputFolderPath, fileName);
            document.save(fullPath.toFile());
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate SRS PDF", e);
        }
    }

    private void addTitleToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;

        createNewPage(document);

        writeText(document, "Software Requirements Specification", 12, false, 25f);
        writeText(document, srsDocumentState.getSrsDocumentTitlePage().projectName(), 32, true, 40f);
    }

    private void addRevisionHistoryToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var revisionHistory = srsDocumentState.getSrsDocumentRevisionHistory().srsRevisions();

        createNewPage(document);
        writeText(document, "Revision History", 24, true, 30f);

        for (var r : revisionHistory) {
            String header = r.version() + ", on " + r.date() + " by " + r.author() + ":";
            writeText(document, header, 12, false, LINE_HEIGHT);
            writeParagraph(document, r.description(), 12, false, LINE_HEIGHT);
        }
    }

    private void addIntroductionToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var introduction = srsDocumentState.getSrsDocumentIntroduction();

        createNewPage(document);
        writeText(document, "1. Introduction", 24, true, 30f);

        writeText(document, "1.1. Purpose", 16, true, 24f);
        writeParagraph(document, introduction.purpose(), 12, false, LINE_HEIGHT);

        writeText(document, "1.2. Scope", 16, true, 24f);
        writeParagraph(document, introduction.scope(), 12, false, LINE_HEIGHT);

        var definitions = introduction.definitionsAcronymsAbreviations();
        writeText(document, "1.3. Definitions, Acronyms, and Abbreviations", 16, true, 24f);

        for (var d : definitions.entrySet()) {
            String entryText = d.getKey() + ": " + d.getValue();
            writeParagraph(document, entryText, 12, false, LINE_HEIGHT);
        }

        writeText(document, "1.4. References", 16, true, 24f);
        for (String ref : introduction.references()) {
            writeParagraph(document, ref, 12, false, LINE_HEIGHT);
        }
    }

    private void writeParagraph(PDDocument document, String text, int fontSize, boolean bold, float lineSpacing) throws IOException {
        if (text == null || text.trim().isEmpty()) return;

        PDFont font = bold ? fontBold : fontRoman;
        String[] hardLines = text.split("\r?\n");

        for (String hardLine : hardLines) {
            List<String> wrappedLines = wrapText(hardLine, font, fontSize, MAX_TEXT_WIDTH);

            for (String line : wrappedLines) {
                writeText(document, line, fontSize, bold, lineSpacing);
            }
        }
    }

    private List<String> wrapText(String text, PDFont font, int fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String potentialLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float stringWidth = (font.getStringWidth(potentialLine) / 1000f) * fontSize;

            if (stringWidth <= maxWidth) {
                currentLine.append(currentLine.length() == 0 ? "" : " ").append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private void createNewPage(PDDocument document) throws IOException {
        closeCurrentStream();

        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);

        currentStream = new PDPageContentStream(document, currentPage);
        currentY = TOP_MARGIN;
    }

    private void closeCurrentStream() throws IOException {
        if (currentStream != null) {
            currentStream.close();
            currentStream = null;
        }
    }

    private boolean isPageOverflow(float requiredSpace) {
        return (currentY - requiredSpace) < BOTTOM_MARGIN;
    }

    private void writeText(PDDocument document, String text, int fontSize, boolean bold, float spacingAfter) throws IOException {
        if (text == null) return;

        if (isPageOverflow(spacingAfter)) {
            createNewPage(document);
        }

        currentStream.beginText();
        currentStream.setFont(bold ? fontBold : fontRoman, fontSize);
        currentStream.newLineAtOffset(LEFT_MARGIN, currentY);
        currentStream.showText(text);
        currentStream.endText();

        currentY -= spacingAfter;
    }
}