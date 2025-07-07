CREATE TABLE IF NOT EXISTS notification_templates (
    template_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    subject_tmp VARCHAR(200) NOT NULL,
    body_template CLOB NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50),
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_notification_templates_event_type ON notification_templates(event_type);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    external_user_id VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(255) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_notifications_external_user_id ON notifications(external_user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_is_read ON notifications(is_read);
