package com.telus.notification.service;

import com.telus.notification.exception.NotificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.test-connection:false}")
    private boolean testMode;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    public void sendSimpleMessage(String to, String subject, String text) {
        validateEmailParameters(to, subject, text);

        if (testMode) {
            logger.info("Test mode enabled. Email would have been sent to: {}", to);
            logger.info("Subject: {}", subject);
            logger.info("Content: {}", text);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            String errorMsg = String.format("Failed to send email to: %s", to);
            logger.error("{}: {}", errorMsg, e.getMessage(), e);
            throw new NotificationException(errorMsg, e);
        }
    }

    private void validateEmailParameters(String to, String subject, String text) {
        if (to == null || to.trim().isEmpty()) {
            throw new NotificationException("Email recipient cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(to).matches()) {
            throw new NotificationException("Invalid email address format: " + to);
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new NotificationException("Email subject cannot be null or empty");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new NotificationException("Email content cannot be null or empty");
        }
    }
}
