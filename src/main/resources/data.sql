INSERT INTO notification_templates (event_type, name, subject_tmp, body_template, created_at, updated_at)
SELECT 'UserRegistered', 'User Registration Template', 'Welcome to TELUS',
'<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to TELUS</title>
</head>
<body>
    <h1>Welcome to TELUS, ${userName}!</h1>
    <p>Your account has been successfully created with the following details:</p>
    <ul>
        <li>Username: ${userName}</li>
        <li>Email: ${userId}</li>
    </ul>
    <p>You can now log in to your account using the following link:</p>
    <a href="${loginUrl}">Login to Your Account</a>
    <p>If you have any questions or need assistance, please don''t hesitate to contact our support team.</p>
    <p>Best regards,<br>The TELUS Team</p>
    <p>&copy; ${currentYear} TELUS. All rights reserved.</p>
</body>
</html>',
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM notification_templates WHERE event_type = 'UserRegistered');
