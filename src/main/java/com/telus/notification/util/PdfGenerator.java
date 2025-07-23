package com.telus.notification.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.telus.notification.model.InterviewQuestionsEmailModel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;

@Component
public class PdfGenerator {

    private static final String LOGO_URL = "https://storage.googleapis.com/telus_recruit_ai/Telus_logo_logotype.png";
    private static final BaseColor PRIMARY_COLOR = new BaseColor(75, 40, 109); // Dark Purple
    private static final BaseColor SECONDARY_COLOR = new BaseColor(51, 51, 51); // Dark Gray

    public byte[] generateInterviewQuestionsPdf(InterviewQuestionsEmailModel model) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50); // Margins: 50pt on all sides
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add logo
            Image logo = Image.getInstance(new URL(LOGO_URL));
            logo.scaleToFit(200, 100); // Scale logo to reasonable size
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(Chunk.NEWLINE);

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, PRIMARY_COLOR);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, SECONDARY_COLOR);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, SECONDARY_COLOR);
            Font questionFont = FontFactory.getFont(FontFactory.HELVETICA, 12, PRIMARY_COLOR);

            // Title
            Paragraph title = new Paragraph("Interview Questions", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            // Candidate Info Section
            Paragraph candidateInfo = new Paragraph();
            candidateInfo.add(new Chunk("Candidate: ", subtitleFont));
            candidateInfo.add(new Chunk(model.getCandidateName() + "\n", normalFont));
            candidateInfo.add(new Chunk("Position: ", subtitleFont));
            candidateInfo.add(new Chunk(model.getPosition() + "\n", normalFont));
            candidateInfo.add(new Chunk("Interview Date: ", subtitleFont));
            candidateInfo.add(new Chunk(
                model.getInterviewDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm")), 
                normalFont
            ));
            candidateInfo.setSpacingAfter(30);
            document.add(candidateInfo);

            // Questions Section
            Paragraph questionsTitle = new Paragraph("Interview Questions:", subtitleFont);
            questionsTitle.setSpacingAfter(20);
            document.add(questionsTitle);

            for (int i = 0; i < model.getQuestions().size(); i++) {
                Paragraph question = new Paragraph();
                question.add(new Chunk((i + 1) + ". ", questionFont));
                question.add(new Chunk(model.getQuestions().get(i), normalFont));
                question.setSpacingAfter(15);
                document.add(question);
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new DocumentException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
