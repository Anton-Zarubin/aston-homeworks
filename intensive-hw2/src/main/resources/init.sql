CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tags
(
    tag_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS news
(
    news_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    news_title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT now(),
    FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS news_tags(
                           news_id_nt BIGINT,
                           tag_id_nt BIGINT,
                           PRIMARY KEY (news_id_nt, tag_id_nt),
                           FOREIGN KEY (news_id_nt) REFERENCES news(news_id),
                           FOREIGN KEY (tag_id_nt) REFERENCES tags(tag_id)
);