-- Recipes database schema
-- Generated from recipes-db/src/main/resources/liquibase/*.xml.
-- Target: MySQL/MariaDB

CREATE TABLE IF NOT EXISTS recipe (
    id INT NOT NULL AUTO_INCREMENT,
    filename VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    rating DECIMAL(4, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tag (
    id INT NOT NULL AUTO_INCREMENT,
    tag VARCHAR(50) NOT NULL,
    modification_date BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS recipe_tag (
    recipe_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (recipe_id, tag_id),
    CONSTRAINT fk_recipe_tag_tag
        FOREIGN KEY (tag_id) REFERENCES tag (id),
    CONSTRAINT fk_recipe_tag_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comment (
    id INT NOT NULL AUTO_INCREMENT,
    recipe_id INT NULL,
    last_modification DATETIME NOT NULL,
    content VARCHAR(600) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comment_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipe (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
