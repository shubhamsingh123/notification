package com.telus.notification.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.telus.notification.model.InterviewQuestionsEmailModel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class PdfGenerator {

    private static final String LOGO_URL = "https://storage.googleapis.com/telus_recruit_ai/naukri_gfg.png";
    private static final BaseColor PRIMARY_COLOR = new BaseColor(75, 40, 109); // Dark Purple
    private static final BaseColor SECONDARY_COLOR = new BaseColor(51, 51, 51); // Dark Gray
    private static final BaseColor LIGHT_GRAY = new BaseColor(128, 128, 128);

    // Font definitions
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, PRIMARY_COLOR);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_COLOR);
    private static final Font SUBSECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY_COLOR);
    private static final Font QUESTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, SECONDARY_COLOR);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 11, SECONDARY_COLOR);
    private static final Font LABEL_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, LIGHT_GRAY);

    public byte[] generateInterviewQuestionsPdf(InterviewQuestionsEmailModel model) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Add logo
            Image logo = Image.getInstance(new URL(LOGO_URL));
            logo.scaleToFit(200, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(Chunk.NEWLINE);

            // Title
            Paragraph title = new Paragraph("Interview Questions", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            // Candidate Info Section
            addCandidateInfo(document, model);

            // Process all questions
            for (int i = 0; i < model.getQuestions().size(); i++) {
                String questionText = model.getQuestions().get(i);
                String[] sections = questionText.split("\n## ");
                
                // Process each section
                for (int j = 0; j < sections.length; j++) {
                    String section = sections[j];
                    if (j == 0 && !section.startsWith("##")) {
                        // If the first section doesn't start with ##, treat it as a question without a section
                        addFormattedQuestion(document, i + 1, section);
                        continue;
                    }
                    
                    // Add section title
                    String sectionTitle = section.substring(0, section.indexOf("\n"));
                    Paragraph sectionPara = new Paragraph(sectionTitle, SECTION_FONT);
                    sectionPara.setSpacingBefore(20);
                    sectionPara.setSpacingAfter(15);
                    document.add(sectionPara);

                    // Process questions in this section
                    String[] questions = section.split("\n### ");
                    for (int k = 1; k < questions.length; k++) {
                        String question = questions[k];
                        addFormattedQuestion(document, i + 1, question);
                    }
                }
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new DocumentException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private void addCandidateInfo(Document document, InterviewQuestionsEmailModel model) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(30);

        // Add candidate info in a table format
        addTableRow(table, "Candidate:", model.getCandidateName());
        addTableRow(table, "Position:", model.getPosition());
        addTableRow(table, "Interview Date:", 
            model.getInterviewDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm")));
        addTableRow(table, "Interview Mode:", model.getInterviewMode());

        document.add(table);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setPaddingRight(10);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addFormattedQuestion(Document document, int questionNumber, String questionText) throws DocumentException {
        // Split the question into its components
        String[] parts = questionText.split("\n\\*\\*");
        
        // Add the main question
        String mainQuestion = parts[0].trim();
        Paragraph questionPara = new Paragraph();
        questionPara.add(new Chunk(questionNumber + ". ", QUESTION_FONT));
        questionPara.add(new Chunk(mainQuestion, NORMAL_FONT));
        questionPara.setSpacingBefore(15);
        questionPara.setSpacingAfter(10);
        document.add(questionPara);

        // Add Look for and Follow-up sections
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("Look for:")) {
                addLookForSection(document, part);
            } else if (part.startsWith("Follow-up:")) {
                addFollowUpSection(document, part);
            }
        }
    }

    private void addLookForSection(Document document, String text) throws DocumentException {
        Paragraph para = new Paragraph();
        para.setIndentationLeft(30);
        para.add(new Chunk("Look for:", LABEL_FONT));
        para.add(new Chunk(text.substring(8).trim(), NORMAL_FONT));
        para.setSpacingAfter(10);
        document.add(para);
    }

    private void addFollowUpSection(Document document, String text) throws DocumentException {
        Paragraph para = new Paragraph();
        para.setIndentationLeft(30);
        para.add(new Chunk("Follow-up:", LABEL_FONT));
        para.add(new Chunk(text.substring(10).trim(), NORMAL_FONT));
        para.setSpacingAfter(15);
        document.add(para);
    }
}
