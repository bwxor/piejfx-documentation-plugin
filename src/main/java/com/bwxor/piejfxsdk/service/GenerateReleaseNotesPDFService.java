package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.dto.ReleaseNotesDocument;
import com.bwxor.piejfxsdk.dto.ReleaseNotesEntry;
import com.bwxor.piejfxsdk.state.ReleaseNotesDocumentState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GenerateReleaseNotesPDFService {

    private static final float TOP_MARGIN = 750f;
    private static final float BOTTOM_MARGIN = 50f;
    private static final float LEFT_MARGIN = 50f;
    private static final float RIGHT_MARGIN = 50f;
    private static final float LINE_HEIGHT = 18f;
    private static final float SUBCATEGORY_SPACING = 10f;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private PDFont fontRegular;
    private PDFont fontBold;

    private PDPage currentPage;
    private PDPageContentStream currentStream;
    private float currentY;

    public boolean generatePdf(String outputFolderPath) {
        try (PDDocument document = new PDDocument()) {
            fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            ReleaseNotesDocument doc = ReleaseNotesDocumentState.instance.getDocument();

            addTitlePage(document, doc);
            addEntriesSection(document, "New Features", doc.newFeatures());
            addEntriesSection(document, "Bug Fixes", doc.bugFixes());
            addEntriesSection(document, "Known Issues", doc.knownIssues());
            addMigrationNotes(document, doc);

            closeCurrentStream();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = LocalDateTime.now().format(formatter);
            Path fullPath = Paths.get(outputFolderPath, "release-notes-" + timestamp + ".pdf");
            document.save(fullPath.toFile());
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Release Notes PDF", e);
        }
    }

    private void addTitlePage(PDDocument document, ReleaseNotesDocument doc) throws IOException {
        createNewPage(document);
        writeText(document, "Release Notes", 12, false, 35f);
        for (String line : wrapText(doc.productName(), fontBold, 32, MAX_TEXT_WIDTH)) {
            writeText(document, line, 32, true, 42f);
        }
        currentY -= 30f;
        writeParagraph(document, "Version: " + doc.version(), 12, false, LINE_HEIGHT + 4f);
        writeParagraph(document, "Release Date: " + doc.releaseDate(), 12, false, LINE_HEIGHT + 4f);
        if (doc.releasedBy() != null && !doc.releasedBy().isBlank()) {
            writeParagraph(document, "Released By: " + doc.releasedBy(), 12, false, LINE_HEIGHT + 4f);
        }
        currentY -= SUBCATEGORY_SPACING;
        writeText(document, "Summary", 16, true, 24f);
        writeParagraph(document, doc.summary(), 12, false, LINE_HEIGHT);
    }

    private void addEntriesSection(PDDocument document, String title, List<ReleaseNotesEntry> entries) throws IOException {
        if (entries == null || entries.isEmpty()) return;
        createNewPage(document);
        writeText(document, title, 24, true, 30f);
        for (ReleaseNotesEntry e : entries) {
            String label = (e.category() != null && !e.category().isBlank()) ? "[" + e.category() + "] " : "";
            writeParagraph(document, label + e.description(), 12, false, LINE_HEIGHT);
            currentY -= 4f;
        }
    }

    private void addMigrationNotes(PDDocument document, ReleaseNotesDocument doc) throws IOException {
        if (doc.migrationNotes() == null || doc.migrationNotes().isBlank()) return;
        createNewPage(document);
        writeText(document, "Migration Notes", 24, true, 30f);
        writeParagraph(document, doc.migrationNotes(), 12, false, LINE_HEIGHT);
    }

    // ---- shared rendering helpers (identical to GenerateSRSPDFService) ----

    private void writeParagraph(PDDocument document, String text, int fontSize, boolean bold, float lineSpacing) throws IOException {
        if (text == null || text.trim().isEmpty()) return;
        PDFont font = bold ? fontBold : fontRegular;
        for (String hardLine : text.split("\r?\n")) {
            for (String line : wrapText(hardLine, font, fontSize, MAX_TEXT_WIDTH)) {
                writeText(document, line, fontSize, bold, lineSpacing);
            }
        }
    }

    private List<String> wrapText(String text, PDFont font, int fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            // Word is wider than a full line on its own: hard-break with hyphens
            if ((font.getStringWidth(word) / 1000f) * fontSize > maxWidth) {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                StringBuilder chunk = new StringBuilder();
                for (char c : word.toCharArray()) {
                    String potential = chunk + String.valueOf(c);
                    String withHyphen = chunk + "-";
                    if ((font.getStringWidth(potential) / 1000f) * fontSize <= maxWidth) {
                        chunk.append(c);
                    } else {
                        lines.add(withHyphen);
                        chunk = new StringBuilder(String.valueOf(c));
                    }
                }
                if (!chunk.isEmpty()) currentLine = chunk;
                continue;
            }

            String potentialLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            float potentialWidth = (font.getStringWidth(potentialLine) / 1000f) * fontSize;
            if (potentialWidth <= maxWidth) {
                // Word fits: append normally
                currentLine.append(currentLine.isEmpty() ? "" : " ").append(word);
            } else {
                float currentLineWidth = currentLine.isEmpty() ? 0f
                        : (font.getStringWidth(currentLine.toString()) / 1000f) * fontSize;
                if (currentLineWidth <= maxWidth * 0.5f) {
                    // Line is less than half full: wrap the whole word to the next line
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Line is more than half full: hyphenate the word at the break point
                    StringBuilder chunk = new StringBuilder();
                    String prefix = currentLine.isEmpty() ? "" : currentLine + " ";
                    for (char c : word.toCharArray()) {
                        String withChar   = prefix + chunk + c;
                        String withHyphen = prefix + chunk + "-";
                        if ((font.getStringWidth(withChar) / 1000f) * fontSize <= maxWidth) {
                            chunk.append(c);
                        } else if ((font.getStringWidth(withHyphen) / 1000f) * fontSize <= maxWidth) {
                            lines.add(withHyphen);
                            prefix = "";
                            chunk = new StringBuilder(String.valueOf(c));
                        } else {
                            if (!currentLine.isEmpty()) {
                                lines.add(currentLine.toString());
                                currentLine = new StringBuilder();
                                prefix = "";
                            }
                            chunk.append(c);
                        }
                    }
                    currentLine = new StringBuilder(prefix + chunk);
                }
            }
        }
        if (!currentLine.isEmpty()) lines.add(currentLine.toString());
        return lines;
    }

    private void createNewPage(PDDocument document) throws IOException {
        ReleaseNotesDocumentState state = ReleaseNotesDocumentState.instance;
        closeCurrentStream();

        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);

        if (state.getWatermarkFile() != null) {
            PDImageXObject img = PDImageXObject.createFromFile(state.getWatermarkFile().getPath(), document);
            float imgW = (float) (img.getWidth() * state.getWatermarkScale());
            float imgH = (float) (img.getHeight() * state.getWatermarkScale());
            float margin = 20f;
            float pw = currentPage.getMediaBox().getWidth();
            float ph = currentPage.getMediaBox().getHeight();
            float x = 0, y = 0;
            switch (state.getWatermarkPosition()) {
                case TOP_LEFT    -> { x = margin; y = ph - imgH - margin; }
                case TOP_RIGHT   -> { x = pw - imgW - margin; y = ph - imgH - margin; }
                case BOTTOM_LEFT -> { x = margin; y = margin; }
                case BOTTOM_RIGHT-> { x = pw - imgW - margin; y = margin; }
                default          -> { x = margin; y = margin; }
            }
            try (PDPageContentStream cs = new PDPageContentStream(
                    document, currentPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                cs.drawImage(img, x, y, imgW, imgH);
            }
        }

        currentStream = new PDPageContentStream(document, currentPage, PDPageContentStream.AppendMode.APPEND, true, true);
        currentY = TOP_MARGIN;
    }

    private void closeCurrentStream() throws IOException {
        if (currentStream != null) { currentStream.close(); currentStream = null; }
    }

    private boolean isPageOverflow(float required) {
        return (currentY - required) < BOTTOM_MARGIN;
    }

    private void writeText(PDDocument document, String text, int fontSize, boolean bold, float spacingAfter) throws IOException {
        if (text == null) return;
        if (isPageOverflow(spacingAfter)) createNewPage(document);
        currentStream.beginText();
        currentStream.setFont(bold ? fontBold : fontRegular, fontSize);
        currentStream.newLineAtOffset(LEFT_MARGIN, currentY);
        currentStream.showText(text);
        currentStream.endText();
        currentY -= spacingAfter;
    }
}
