UPDATE user_notification_details
SET body_template = 'Welcome {{username}}! You have been registered with role {{role}}. Your email is {{email}} and your status is {{status}}'
WHERE body_template IS NULL OR body_template = '';
