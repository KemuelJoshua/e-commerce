CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,

    provider VARCHAR(20) NULL,
    provider_id VARCHAR(255) NULL,

    username VARCHAR(50) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NULL,

    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,

    profile_picture VARCHAR(500),

    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_provider_provider_id UNIQUE (provider, provider_id)
);