-- comments
CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    event_id  BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    text      VARCHAR(1000) NOT NULL,
    created   TIMESTAMP NOT NULL,
    updated   TIMESTAMP
);

-- Индексы для ускорения выборки по событию и автору
CREATE INDEX IF NOT EXISTS idx_comments_event_id  ON comments(event_id);
CREATE INDEX IF NOT EXISTS idx_comments_author_id ON comments(author_id);
