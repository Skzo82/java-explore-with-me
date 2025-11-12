-- Таблица подборок
CREATE TABLE IF NOT EXISTS compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT FALSE
);

-- Связующая таблица подборка-событие
CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
    event_id       BIGINT NOT NULL REFERENCES events(id)       ON DELETE CASCADE,
    CONSTRAINT compilation_events_pk PRIMARY KEY (compilation_id, event_id)
);