CREATE TABLE task_attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    task_id BIGINT NOT NULL,
    uploader_id BIGINT NOT NULL,
    
    original_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    
    file_data LONGBLOB NOT NULL,
    
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_task_attachment_task
        FOREIGN KEY (task_id)
        REFERENCES task(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_task_attachment_user
        FOREIGN KEY (uploader_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);