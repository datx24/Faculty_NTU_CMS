
ALTER TABLE posts ADD COLUMN thumbnail VARCHAR(255) AFTER view_count;

ALTER TABLE events 
ADD COLUMN banner VARCHAR(255) AFTER end_time,
ADD COLUMN registration_url VARCHAR(275) AFTER banner,
ADD COLUMN recap_post INT AFTER registration_url,
ADD CONSTRAINT fk_events_recap_post FOREIGN KEY (recap_post) REFERENCES posts(id);

ALTER TABLE notifications 
ADD COLUMN announcement_type ENUM('info', 'warning', 'success') DEFAULT 'info' AFTER is_active,
ADD COLUMN piority INT AFTER announcement_type;

ALTER TABLE notifications CHANGE COLUMN piority priority INT;

DROP TABLE IF EXISTS menu_items;

CREATE TABLE menu_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    menu_name VARCHAR(50) NOT NULL DEFAULT 'primary',
    label VARCHAR(100) NOT NULL,
    path VARCHAR(255) NOT NULL DEFAULT '#',
    parent_id INT DEFAULT NULL,
    menu_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES menu_items(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS settings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value LONGTEXT,
    description TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
