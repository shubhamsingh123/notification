INSERT INTO notification_templates (template_id, name, event_type, body_template, created_at, updated_at)
VALUES (
    'user-registration-template',
    'User Registration Email',
    'USER_REGISTRATION',
    '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to TELUS</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background-color: #4B286D;
            padding: 20px;
            text-align: center;
        }
        .header img {
            max-width: 150px;
        }
        .content {
            padding: 20px;
            background-color: #ffffff;
        }
        .footer {
            background-color: #f4f4f4;
            padding: 20px;
            text-align: center;
            font-size: 12px;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #66CC00;
            color: #ffffff;
            text-decoration: none;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <img src="cid:telus-logo" alt="TELUS Logo">
        </div>
        <div class="content">
            <h2>Welcome to TELUS!</h2>
            <p>Dear <span th:text="${userName}">John Doe</span>,</p>
            
            <p>Thank you for registering with TELUS. We''re excited to have you on board!</p>
            
            <p>Your account has been successfully created with the following details:</p>
            <ul>
                <li>Username: <span th:text="${userName}">John Doe</span></li>
                <li>Email: <span th:text="${userId}">john.doe@example.com</span></li>
            </ul>

            <p>What''s Next?</p>
            <ul>
                <li>Complete your profile</li>
                <li>Explore our services</li>
                <li>Contact support if you need assistance</li>
            </ul>

            <a th:href="${loginUrl}" class="button">Login to Your Account</a>

            <p>If you have any questions or need assistance, please don''t hesitate to contact our support team.</p>

            <p>Best regards,<br>
            The TELUS Team</p>
        </div>
        <div class="footer">
            <p>This email was sent to <span th:text="${userId}">john.doe@example.com</span>. Please do not reply to this email.</p>
            <p>&copy; <span th:text="${currentYear}">2025</span> TELUS. All rights reserved.</p>
        </div>
    </div>
</body>
</html>',
    NOW(),
    NOW()
);
