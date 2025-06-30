package com.telus.notification.service;

import com.telus.notification.model.UserRegistrationEmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@telus.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Simple email sent to: {}", to);
        } catch (MailException e) {
            logger.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send simple email", e);
        }
    }

    @Override
    public void sendUserRegistrationEmail(UserRegistrationEmailModel model, String rmgEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@telus.com");
            helper.setTo(rmgEmail);
            helper.setCc(new String[]{model.getUserEmail(), model.getManagerEmail()});
            helper.setSubject("New User Registration - TELUS");

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", model.getUserName());
            variables.put("userEmail", model.getUserEmail());
            variables.put("registrationDate", model.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("loginUrl", model.getLoginUrl());
            variables.put("currentYear", java.time.Year.now().getValue());
            context.setVariables(variables);

            String htmlContent = templateEngine.process("user-registration-template", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Failed to send registration email to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail(), e);
            throw new RuntimeException("Failed to send registration email", e);
        }
    }
}
