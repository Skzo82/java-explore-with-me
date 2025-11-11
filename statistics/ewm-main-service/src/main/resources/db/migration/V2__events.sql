-- events
CREATE TABLE IF NOT EXISTS events (
    id                 BIGSERIAL PRIMARY KEY,
    annotation         VARCHAR(2000)        NOT NULL,
    description        VARCHAR(7000)        NOT NULL,
    event_date         TIMESTAMP            NOT NULL,
    created_on         TIMESTAMP            NOT NULL,
    published_on       TIMESTAMP,
    initiator_id       BIGINT               NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id        BIGINT               NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    location_lat       NUMERIC(8,5)         NOT NULL,
    location_lon       NUMERIC(8,5)         NOT NULL,
    paid               BOOLEAN              NOT NULL DEFAULT FALSE,
    participant_limit  INTEGER              NOT NULL DEFAULT 0,
    request_moderation BOOLEAN              NOT NULL DEFAULT TRUE,
    title              VARCHAR(120)         NOT NULL,
    state              VARCHAR(16)          NOT NULL, -- PENDING|PUBLISHED|CANCELED
    views              INTEGER              NOT NULL DEFAULT 0,
    confirmed_requests INTEGER              NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_events_initiator  ON events(initiator_id);
CREATE INDEX IF NOT EXISTS idx_events_category   ON events(category_id);
CREATE INDEX IF NOT EXISTS idx_events_state_date ON events(state, event_date);