-- -- CREATE TABLE NOTIFICATIONS (
-- --     notification_id INT PRIMARY KEY AUTO_INCREMENT,
-- --     external_user_id VARCHAR(50) NOT NULL,
-- --     message TEXT NOT NULL,
-- --     type VARCHAR(50) NOT NULL,
-- --     is_read BOOLEAN NOT NULL DEFAULT FALSE,
-- --     read_at DATETIME NULL,
-- --     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
-- --     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
-- --     created_by VARCHAR(50) NULL,
-- --     updated_by VARCHAR(50) NULL,
-- -- );

-- -- CREATE TABLE NOTIFICATION_TEMPLATES (
-- --     template_id INT PRIMARY KEY AUTO_INCREMENT,
-- --     name VARCHAR(100) NOT NULL UNIQUE,
-- --     subject VARCHAR(200) NOT NULL,
-- --     body_template TEXT NOT NULL,
-- --     event_type VARCHAR(50) NOT NULL,
-- --     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
-- --     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
-- --     created_by VARCHAR(50) NULL,
-- --     updated_by VARCHAR(50) NULL,
    
-- -- );

-- -- CREATE TABLE NOTIFICATION_CHANNELS (
-- --     channel_id INT PRIMARY KEY AUTO_INCREMENT,
-- --     name VARCHAR(50) NOT NULL UNIQUE,
-- --     type ENUM('email', 'sms', 'push', 'in_app') NOT NULL,
-- --     is_active BOOLEAN NOT NULL DEFAULT TRUE,
-- --     configuration JSON NOT NULL,
-- --     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
-- --     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
-- --     created_by VARCHAR(50) NULL,
-- --     updated_by VARCHAR(50) NULL
-- -- );

-- -- CREATE TABLE NOTIFICATION_PREFERENCES (
-- --     preference_id INT PRIMARY KEY AUTO_INCREMENT,
-- --     external_user_id VARCHAR(50) NOT NULL,
-- --     channel_id INT NOT NULL,
-- --     event_type VARCHAR(50) NOT NULL,
-- --     is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
-- --     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
-- --     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
-- --     created_by VARCHAR(50) NULL,
-- --     updated_by VARCHAR(50) NULL,
-- --     FOREIGN KEY (channel_id) REFERENCES NOTIFICATION_CHANNELS(channel_id),
-- --     UNIQUE KEY (external_user_id, channel_id, event_type)
-- -- );




-- -- INSERT INTO NOTIFICATION_TEMPLATES (
-- --   template_id, 
-- --   name, 
-- --   subject, 
-- --   body_template, 
-- --   event_type, 
-- --   created_at, 
-- --   updated_at, 
-- --   created_by, 
-- --   updated_by
-- -- ) VALUES (
-- --   1, 
-- --   'New Manager Registration', 
-- --   'New Manager Registration Requires Approval', 
-- --   '<!DOCTYPE html>\n<html xmlns:th=\"http://www.thymeleaf.org\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>Welcome to TELUS</title>\n    <style>\n        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333333; margin: 0; padding: 0; }\n        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n        .header { background-color: #4B286D; padding: 20px; text-align: center; }\n        .header img { max-width: 150px; }\n        .content { padding: 20px; background-color: #ffffff; }\n        .footer { background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 12px; }\n        .button { display: inline-block; padding: 10px 20px; background-color: #66CC00; color: #ffffff; text-decoration: none; border-radius: 5px; margin: 20px 0; }\n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"header\">\n            <img src=\"cid:telus-logo\" alt=\"TELUS Logo\">\n        </div>\n        <div class=\"content\">\n            <h2>Welcome to TELUS!</h2>\n            <p>Dear <span th:text=\"${userName}\">John Doe</span>,</p>\n            <p>Thank you for registering with TELUS. We''re excited to have you on board!</p>\n            <p>Your account has been successfully created with the following details:</p>\n            <ul>\n                <li>Username: <span th:text=\"${userName}\">John Doe</span></li>\n                <li>Email: <span th:text=\"${email}\">john.doe@example.com</span></li>\n            </ul>\n            <p>What''s Next?</p>\n            <ul>\n                <li>Complete your profile</li>\n                <li>Explore our services</li>\n                <li>Contact support if you need assistance</li>\n            </ul>\n            <a th:href=\"${loginUrl}\" class=\"button\">Login to Your Account</a>\n            <p>If you have any questions or need assistance, please don''t hesitate to contact our support team.</p>\n            <p>Best regards,<br>The TELUS Team</p>\n        </div>\n        <div class=\"footer\">\n            <p>This email was sent to <span th:text=\"${email}\">john.doe@example.com</span>. Please do not reply to this email.</p>\n            <p>&copy; <span th:text=\"${currentYear}\">2025</span> TELUS. All rights reserved.</p>\n        </div>\n    </div>\n</body>\n</html>',
-- --   'UserRegisterEvent', 
-- --   '2025-07-02 10:00:00', 
-- --   '2025-07-02 10:00:00', 
-- --   'null', 
-- --   'null'
-- -- );

-- -- INSERT INTO NOTIFICATION_CHANNELS (
-- --   channel_id, 
-- --   name, 
-- --   type, 
-- --   is_active, 
-- --   configuration, 
-- --   created_at, 
-- --   updated_at, 
-- --   created_by, 
-- --   updated_by
-- -- ) VALUES (
-- --   1, 
-- --   'Email Notifications', 
-- --   'email', 
-- --   true, 
-- --   '{"smtp_server": "smtp.telus.com", "smtp_port": 587, "sender_email": "shubham16cse06@gmail.com" , "password" : "xgai hyhf dmub sruh"}', 
-- --   '2025-07-02 10:00:00', 
-- --   '2025-07-02 10:00:00', 
-- --   'null', 
-- --   'null'
-- -- );


-- -- INSERT INTO NOTIFICATION_PREFERENCES (
-- --   preference_id, 
-- --   external_user_id, 
-- --   channel_id, 
-- --   event_type, 
-- --   is_enabled, 
-- --   created_at, 
-- --   updated_at, 
-- --   created_by, 
-- --   updated_by
-- -- ) VALUES (
-- --   1, 
-- --   'shubham16cse06@gmail.com', 
-- --   1, -- Email channel
-- --   'UserRegisterEvent', 
-- --   true, 
-- --   '2025-07-02 10:00:00', 
-- --   '2025-07-02 10:00:00', 
-- --   'null', 
-- --   'null'
-- -- );

-- -- /////////////

-- -- H2 Database Schema for Notification System

-- -- NOTIFICATIONS Table
-- CREATE TABLE NOTIFICATIONS (
--     notification_id INT PRIMARY KEY AUTO_INCREMENT,
--     external_user_id VARCHAR(50) NOT NULL,
--     message CLOB NOT NULL,
--     type VARCHAR(50) NOT NULL,
--     is_read BOOLEAN NOT NULL DEFAULT FALSE,
--     read_at TIMESTAMP NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     created_by VARCHAR(50) NULL,
--     updated_by VARCHAR(50) NULL
-- );

-- -- Create indexes for NOTIFICATIONS table
-- CREATE INDEX idx_notifications_user_type_read ON NOTIFICATIONS(external_user_id, type, is_read);

-- -- NOTIFICATION_TEMPLATES Table
-- CREATE TABLE NOTIFICATION_TEMPLATES (
--     template_id INT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(100) NOT NULL UNIQUE,
--     subject_tmp VARCHAR(200) NOT NULL,
--     body_template CLOB NOT NULL,
--     event_type VARCHAR(50) NOT NULL,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     created_by VARCHAR(50) NULL,
--     updated_by VARCHAR(50) NULL
-- );

-- -- Create indexes for NOTIFICATION_TEMPLATES table
-- CREATE INDEX idx_notification_templates_event_type ON NOTIFICATION_TEMPLATES(event_type);

-- -- NOTIFICATION_CHANNELS Table
-- CREATE TABLE NOTIFICATION_CHANNELS (
--     channel_id INT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(50) NOT NULL UNIQUE,
--     type VARCHAR(10) NOT NULL CHECK (type IN ('email', 'sms', 'push', 'in_app')),
--     is_active BOOLEAN NOT NULL DEFAULT TRUE,
--     configuration VARCHAR(4000) NOT NULL, -- H2 doesn't have native JSON type, using VARCHAR
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     created_by VARCHAR(50) NULL,
--     updated_by VARCHAR(50) NULL
-- );

-- -- NOTIFICATION_PREFERENCES Table
-- CREATE TABLE NOTIFICATION_PREFERENCES (
--     preference_id INT PRIMARY KEY AUTO_INCREMENT,
--     external_user_id VARCHAR(50) NOT NULL,
--     channel_id INT NOT NULL,
--     event_type VARCHAR(50) NOT NULL,
--     is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
--     created_by VARCHAR(50) NULL,
--     updated_by VARCHAR(50) NULL,
--     FOREIGN KEY (channel_id) REFERENCES NOTIFICATION_CHANNELS(channel_id),
--     CONSTRAINT unique_user_channel_event UNIQUE (external_user_id, channel_id, event_type)
-- );


-- -- Sample data for NOTIFICATION_CHANNELS
-- INSERT INTO NOTIFICATION_CHANNELS (name, type, is_active, configuration, created_by, updated_by)
-- VALUES ('Email Notifications', 'email', TRUE, '{"smtp_server": "smtp.telus.com", "smtp_port": 587, "sender_email": "shubham16cse06@gmail.com" , "password" : "xgai hyhf dmub sruh"}');

-- -- Sample data for NOTIFICATION_TEMPLATES
-- INSERT INTO NOTIFICATION_TEMPLATES (name, subject_tmp, body_template, event_type, created_by, updated_by)
-- VALUES (
--   'New User Registration', 
--   'New User Registration Requires Approval', 
--   'A new manager {{username}} ({{email}}) has registered and requires your approval. Please login to the RMP Portal to review this request.', 
--   'USER_REGISTERED', 
--   'system', 
--   'system'
-- );

-- INSERT INTO NOTIFICATION_TEMPLATES (name, subject_tmp, body_template, event_type, created_by, updated_by)
-- VALUES (
--   'Account Approved', 
--   'Your Account Has Been Approved', 
--   'Congratulations {{username}}! Your account has been approved. You can now login to the RMP Portal.', 
--   'ACCOUNT_APPROVED', 
--   'system', 
--   'system'
-- );

-- ///////////////////////////////////////////////////////////
-- -- H2 Database Schema for Notification System

-- -- NOTIFICATIONS Table
CREATE TABLE NOTIFICATIONS (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    external_user_id VARCHAR(50) NOT NULL,
    message CLOB NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    created_by VARCHAR(50) NULL,
    updated_by VARCHAR(50) NULL
);

-- Create indexes for NOTIFICATIONS table
CREATE INDEX idx_notifications_user_type_read ON NOTIFICATIONS(external_user_id, type, is_read);

-- NOTIFICATION_TEMPLATES Table
CREATE TABLE NOTIFICATION_TEMPLATES (
    template_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    subject_tmp VARCHAR(200) NOT NULL,
    body_template CLOB NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    created_by VARCHAR(50) NULL,
    updated_by VARCHAR(50) NULL
);

-- Create indexes for NOTIFICATION_TEMPLATES table
CREATE INDEX idx_notification_templates_event_type ON NOTIFICATION_TEMPLATES(event_type);

-- NOTIFICATION_CHANNELS Table
CREATE TABLE NOTIFICATION_CHANNELS (
    channel_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(10) NOT NULL CHECK (type IN ('email', 'sms', 'push', 'in_app')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    configuration VARCHAR(4000) NOT NULL, -- H2 doesn't have native JSON type, using VARCHAR
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    created_by VARCHAR(50) NULL,
    updated_by VARCHAR(50) NULL
);

-- NOTIFICATION_PREFERENCES Table
CREATE TABLE NOTIFICATION_PREFERENCES (
    preference_id INT PRIMARY KEY AUTO_INCREMENT,
    external_user_id VARCHAR(50) NOT NULL,
    channel_id INT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    created_by VARCHAR(50) NULL,
    updated_by VARCHAR(50) NULL,
    FOREIGN KEY (channel_id) REFERENCES NOTIFICATION_CHANNELS(channel_id),
    CONSTRAINT unique_user_channel_event UNIQUE (external_user_id, channel_id, event_type)
);

-- Sample data for NOTIFICATION_CHANNELS
INSERT INTO NOTIFICATION_CHANNELS (name, type, is_active, configuration, created_by, updated_by)
VALUES ('Email Notifications', 'email', TRUE, '{"smtp_server": "smtp.telus.com", "smtp_port": 587, "sender_email": "shubham16cse06@gmail.com" , "password" : "xgai hyhf dmub sruh"}', 'system', 'system');

-- Sample data for NOTIFICATION_TEMPLATES
INSERT INTO NOTIFICATION_TEMPLATES (name, subject_tmp, body_template, event_type, created_by, updated_by)
VALUES (
  'User Registration Template',
  'Welcome to TELUS',
  '<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Approved - TELUS</title>
    <style>
        @import url(''https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap'');
        
        body {
            font-family: ''Roboto'', Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background: linear-gradient(135deg, #4B286D 0%, #66CC00 100%);
            color: white;
            text-align: center;
            padding: 20px;
            border-radius: 10px 10px 0 0;
        }
        .content {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 0 0 10px 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .logo {
            max-width: 150px;
            margin-bottom: 20px;
        }
        h1 {
            color: #4B286D;
            margin-bottom: 20px;
        }
        .cta-button {
            display: inline-block;
            background-color: #66CC00;
            color: white;
            text-decoration: none;
            padding: 12px 24px;
            border-radius: 5px;
            font-weight: bold;
            margin-top: 20px;
            transition: background-color 0.3s ease;
        }
        .cta-button:hover {
            background-color: #4B286D;
        }
        .footer {
            text-align: center;
            margin-top: 20px;
            font-size: 12px;
            color: #666;
        }
        .icon {
            font-size: 24px;
            margin-right: 10px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <img src="./Telus-Logo.svg" alt="TELUS Logo" class="logo">
        </div>
        <div class="content">
            <h1>🎉 Congratulations, Shubham!</h1>
            <p>Your <strong>TELUS</strong> account has been <strong>approved</strong> and is now active. 🚀</p>
            <p>You can now access all TELUS services using your email:</p>
            <p style="font-weight: bold; color: #4B286D;">${userEmail}</p>
            <a href="${loginUrl}" class="cta-button">Log in to Your Account</a>
            <p>If you have any questions about your account or our services, our support team is here to help.</p>
            <p>
                <span class="icon">📞</span> Call us: 1-888-811-2323<br>
                <span class="icon">✉️</span> Email: support@telus.com
            </p>
            <p>Best regards,<br><strong>The TELUS Team</strong></p>
        </div>
        <div class="footer">
            © ${currentYear} TELUS. All rights reserved.<br>
            <a href="https://www.telus.com/privacy" style="color: #4B286D;">Privacy Policy</a> | 
            <a href="https://www.telus.com/terms" style="color: #4B286D;">Terms of Service</a>
        </div>
    </div>
</body>
</html>',
  'UserRegistered',
  'system',
  'system'
);


-- Sample data for NOTIFICATION_TEMPLATES
INSERT INTO NOTIFICATION_TEMPLATES (name, subject_tmp, body_template, event_type, created_by, updated_by)
VALUES (
  'Account Approved',
  'Welcome to TELUS',
  '<!DOCTYPE html>
<html>
<head>
    <title>Welcome to TELUS</title>
</head>
<body>
    <h1>Welcome to TELUS, ${userName}!</h1>
    
</body>
</html>',
  'AccountApproved',
  'system',
  'system'
);

-- Sample data for NOTIFICATION_PREFERENCES
INSERT INTO NOTIFICATION_PREFERENCES (external_user_id, channel_id, event_type, is_enabled, created_by, updated_by)
VALUES ('shubham16cse06@gmail.com', 1, 'UserRegistered', TRUE, 'system', 'system');
