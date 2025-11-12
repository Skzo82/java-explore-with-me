CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Индекс для ускорения поиска заявок по пользователю и событию
CREATE INDEX IF NOT EXISTS idx_participation_requests_event_id ON participation_requests(event_id);
CREATE INDEX IF NOT EXISTS idx_participation_requests_requester_id ON participation_requests(requester_id);