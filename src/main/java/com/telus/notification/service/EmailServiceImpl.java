package com.telus.notification.service;

import com.telus.notification.model.UserAccountApprovalEmailModel;
import com.telus.notification.model.UserRegistrationEmailModel;
import com.telus.notification.entity.NotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.Year;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationTemplateService templateService;

    @Autowired
    private NotificationTemplates notificationTemplates;

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

            // Prepare variables for template
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", model.getUserName());
            variables.put("userId", model.getUserEmail());
            variables.put("registrationDate", model.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("loginUrl", model.getLoginUrl());

            // Process template with variables
            String htmlContent = notificationTemplates.getUserRegistrationTemplate(variables);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Registration email sent to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send registration email to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail(), e);
            throw new RuntimeException("Failed to send registration email", e);
        }
    }
    
    
    
    
    ///
    
    @Override
    public void sendAccountApproveEmail(UserAccountApprovalEmailModel model, String rmgEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@telus.com");
            helper.setTo(rmgEmail);
            helper.setCc(new String[]{model.getUserEmail(), model.getManagerEmail()});
            helper.setSubject("Account Approved - TELUS");

            // Prepare variables for template
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", model.getUserName());
            variables.put("userId", model.getUserEmail());
            variables.put("registrationDate", model.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("currentYear", java.time.Year.now().getValue());

            // Process template with variables
            String htmlContent = notificationTemplates.getAccountApprovalTemplate(variables);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Account approval email sent to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send approval email to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail(), e);
            throw new RuntimeException("Failed to send approval email", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@telus.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("HTML email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendAccountRejectionEmail(UserAccountApprovalEmailModel model, String rmgEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@telus.com");
            helper.setTo(rmgEmail);
            helper.setCc(new String[]{model.getUserEmail(), model.getManagerEmail()});
            helper.setSubject("Account Rejected - TELUS");

            // Prepare variables for template
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", model.getUserName());
            variables.put("userId", model.getUserEmail());
            variables.put("registrationDate", model.getRegistrationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            variables.put("currentYear", java.time.Year.now().getValue());
            variables.put("rejectionReason", model.getRejectionReason());

            // Process template with variables
            String htmlContent = notificationTemplates.getAccountRejectionTemplate(variables);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Account rejection email sent to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send rejection email to RMG {}, user {}, and manager {}", rmgEmail, model.getUserEmail(), model.getManagerEmail(), e);
            throw new RuntimeException("Failed to send rejection email", e);
        }
    }
}
