package com.telus.notification.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.telus.notification.model.InterviewQuestionsEmailModel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class PdfGenerator {

    public byte[] generateInterviewQuestionsPdf(InterviewQuestionsEmailModel model) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph("Interview Questions", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Candidate: " + model.getCandidateName(), normalFont));
        document.add(new Paragraph("Position: " + model.getPosition(), normalFont));
        document.add(new Paragraph("Interview Date: " + 
            model.getInterviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), normalFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Questions:", normalFont));
        for (int i = 0; i < model.getQuestions().size(); i++) {
            document.add(new Paragraph((i + 1) + ". " + model.getQuestions().get(i), normalFont));
        }

        document.close();
        return out.toByteArray();
    }
}
