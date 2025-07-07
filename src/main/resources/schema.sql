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
