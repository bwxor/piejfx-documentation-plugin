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
    private static final float SUBCATEGORY_SPACING = 10f;

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float MAX_TEXT_WIDTH = PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private final PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private final PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private PDPage currentPage;
    private PDPageContentStream currentStream;
    private float currentY;

    public boolean generatePdf(String outputFolderPath) {
        try (PDDocument document = new PDDocument()) {
            addTitleToDocument(document);
            addRevisionHistoryToDocument(document);
            addIntroductionToDocument(document);
            addOverallDescriptionToDocument(document);
            addFunctionalRequirementsToDocument(document);
            addNonFunctionalRequirementsToDocument(document);
            addExternalInterfaceRequirementsToDocument(document);
            addUseCasesToDocument(document);
            addAppendixToDocument(document);

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
        var titlePage = srsDocumentState.getSrsDocumentTitlePage();

        createNewPage(document);

        writeText(document, "Software Requirements Specification", 12, false, 35f);
        var titleWrapped = wrapText(titlePage.projectName(), fontBold, 32, MAX_TEXT_WIDTH);

        for (String line : titleWrapped) {
            writeText(document, line, 32, true, 42f);
        }

        // Extra gap so metadata is clearly separated from the title
        currentY -= 40f;

        if (titlePage.documentVersion() != null && !titlePage.documentVersion().isBlank()) {
            writeText(document, "Version: " + titlePage.documentVersion(), 12, false, LINE_HEIGHT + 4f);
        }
        if (titlePage.date() != null && !titlePage.date().isBlank()) {
            writeText(document, "Date: " + titlePage.date(), 12, false, LINE_HEIGHT + 4f);
        }
        if (titlePage.authors() != null && !titlePage.authors().isBlank()) {
            writeParagraph(document, "Authors: " + titlePage.authors(), 12, false, LINE_HEIGHT + 4f);
        }
        if (titlePage.status() != null) {
            writeText(document, "Status: " + titlePage.status().getValue(), 12, false, LINE_HEIGHT + 4f);
        }
    }

    private void addRevisionHistoryToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var revisionHistory = srsDocumentState.getSrsDocumentRevisionHistory().srsRevisions();

        createNewPage(document);
        writeText(document, "Revision History", 24, true, 30f);

        for (var r : revisionHistory) {
            writeParagraph(document, r.version() + " (" +r.date() + ")", 12, true, LINE_HEIGHT);
            writeText(document, " by " + r.author(), 12, true, LINE_HEIGHT);
            writeParagraph(document, r.description(), 12, false, 20f);
        }
    }

    private void addIntroductionToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var introduction = srsDocumentState.getSrsDocumentIntroduction();

        createNewPage(document);
        writeText(document, "1. Introduction", 24, true, 30f);

        writeText(document, "1.1. Purpose", 16, true, 24f);
        writeParagraph(document, introduction.purpose(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "1.2. Scope", 16, true, 24f);
        writeParagraph(document, introduction.scope(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        var definitions = introduction.definitionsAcronymsAbreviations();
        writeText(document, "1.3. Definitions, Acronyms, and Abbreviations", 16, true, 24f);

        for (var d : definitions.entrySet()) {
            String entryText = d.getKey() + ": " + d.getValue();
            writeParagraph(document, entryText, 12, false, LINE_HEIGHT);
        }
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "1.4. References", 16, true, 24f);
        for (String ref : introduction.references()) {
            writeParagraph(document, ref, 12, false, LINE_HEIGHT);
        }
    }

    private void addOverallDescriptionToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var overall = srsDocumentState.getSrsDocumentOverallDescription();

        createNewPage(document);
        writeText(document, "2. Overall Description", 24, true, 30f);

        writeText(document, "2.1. Product Perspective", 16, true, 24f);
        writeParagraph(document, overall.productPerspective(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "2.2. User Classes and Characteristics", 16, true, 24f);
        for (var entry : overall.userClassesAndCharacteristics().entrySet()) {
            writeParagraph(document, entry.getKey() + ": " + entry.getValue(), 12, false, LINE_HEIGHT);
        }
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "2.3. Operating Environment", 16, true, 24f);
        writeParagraph(document, overall.operatingEnvironment(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "2.4. Constraints", 16, true, 24f);
        writeParagraph(document, overall.constraints(), 12, false, LINE_HEIGHT);
    }

    private void addFunctionalRequirementsToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var requirements = srsDocumentState.getSrsDocumentFunctionalRequirements().requirements();

        createNewPage(document);
        writeText(document, "3. Functional Requirements", 24, true, 30f);

        for (var r : requirements) {
            String header = r.id() + " [" + r.priority() + "]";
            writeText(document, header, 12, true, LINE_HEIGHT);
            writeParagraph(document, r.requirement(), 12, false, LINE_HEIGHT);
        }
    }

    private void addNonFunctionalRequirementsToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var nfr = srsDocumentState.getSrsDocumentNonFunctionalRequirements();

        createNewPage(document);
        writeText(document, "4. Non-Functional Requirements", 24, true, 30f);

        writeText(document, "4.1. Performance", 16, true, 24f);
        writeParagraph(document, nfr.performanceRequirements(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "4.2. Security", 16, true, 24f);
        writeParagraph(document, nfr.securityRequirements(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "4.3. Usability", 16, true, 24f);
        writeParagraph(document, nfr.usabilityRequirements(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "4.4. Reliability", 16, true, 24f);
        writeParagraph(document, nfr.reliabilityRequirements(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "4.5. Scalability", 16, true, 24f);
        writeParagraph(document, nfr.scalabilityRequirements(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "4.6. Compliance", 16, true, 24f);
        writeParagraph(document, nfr.complianceRequirements(), 12, false, LINE_HEIGHT);
    }

    private void addExternalInterfaceRequirementsToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var eir = srsDocumentState.getSrsDocumentExternalInterfaceRequirements();

        createNewPage(document);
        writeText(document, "5. External Interface Requirements", 24, true, 30f);

        writeText(document, "5.1. User Interfaces", 16, true, 24f);
        writeParagraph(document, eir.userInterfaces(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "5.2. Hardware Interfaces", 16, true, 24f);
        writeParagraph(document, eir.hardwareInterfaces(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "5.3. Software Interfaces", 16, true, 24f);
        writeParagraph(document, eir.softwareInterfaces(), 12, false, LINE_HEIGHT);
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "5.4. Communication Interfaces", 16, true, 24f);
        writeParagraph(document, eir.communicationInterfaces(), 12, false, LINE_HEIGHT);
    }

    private void addUseCasesToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var useCases = srsDocumentState.getSrsDocumentUseCases().useCases();

        createNewPage(document);
        writeText(document, "6. Use Cases", 24, true, 30f);

        for (var uc : useCases) {
            writeParagraph(document, uc.id(), 14, true, 20f);
            writeParagraph(document, "Actor: " + uc.actor(), 12, false, LINE_HEIGHT);
            writeText(document, "Preconditions:", 12, true, LINE_HEIGHT);
            writeParagraph(document, uc.preconditions(), 12, false, LINE_HEIGHT);
            writeText(document, "Main Flow:", 12, true, LINE_HEIGHT);
            writeParagraph(document, uc.mainFlow(), 12, false, LINE_HEIGHT);
            writeText(document, "Alternate Flow:", 12, true, LINE_HEIGHT);
            writeParagraph(document, uc.alternateFlow(), 12, false, LINE_HEIGHT);
            writeText(document, "Post Conditions:", 12, true, LINE_HEIGHT);
            writeParagraph(document, uc.postConditions(), 12, false, LINE_HEIGHT);
            currentY -= SUBCATEGORY_SPACING;
        }
    }

    private void addAppendixToDocument(PDDocument document) throws IOException {
        SRSDocumentState srsDocumentState = SRSDocumentState.instance;
        var appendices = srsDocumentState.getSrsDocumentAppendices();

        createNewPage(document);
        writeText(document, "Appendix", 24, true, 30f);

        writeText(document, "Glossary", 16, true, 24f);
        for (var entry : appendices.glossary().entrySet()) {
            writeParagraph(document, entry.getKey(), 12, true, LINE_HEIGHT);
            writeParagraph(document, entry.getValue(), 12, false, 20f);
        }
        currentY -= SUBCATEGORY_SPACING;

        writeText(document, "Open Issues", 16, true, 24f);
        for (String issue : appendices.openIssues()) {
            writeParagraph(document, issue, 12, false, LINE_HEIGHT);
        }
    }

    private void writeParagraph(PDDocument document, String text, int fontSize, boolean bold, float lineSpacing) throws IOException {
        if (text == null || text.trim().isEmpty()) return;

        PDFont font = bold ? fontBold : fontRegular;
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
            String potentialLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            float stringWidth = (font.getStringWidth(potentialLine) / 1000f) * fontSize;

            if (stringWidth <= maxWidth) {
                currentLine.append(currentLine.isEmpty() ? "" : " ").append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        if (!currentLine.isEmpty()) {
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
        currentStream.setFont(bold ? fontBold : fontRegular, fontSize);
        currentStream.newLineAtOffset(LEFT_MARGIN, currentY);
        currentStream.showText(text);
        currentStream.endText();

        currentY -= spacingAfter;
    }
}