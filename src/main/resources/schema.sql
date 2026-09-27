-- =========================================================
-- Content
-- =========================================================
CREATE TABLE IF NOT EXISTS content
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255)             NOT NULL,
    url          VARCHAR(255)             NOT NULL,
    type         VARCHAR(50)              NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- =========================================================
-- Subscriber
-- =========================================================
CREATE TABLE IF NOT EXISTS subscriber
(
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL
);

-- =========================================================
-- Event publication (Spring Modulith event log)
-- =========================================================
CREATE TABLE IF NOT EXISTS event_publication
(
    id                     UUID                     NOT NULL,
    listener_id            TEXT                     NOT NULL,
    event_type             TEXT                     NOT NULL,
    serialized_event       TEXT                     NOT NULL,
    publication_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date        TIMESTAMP WITH TIME ZONE,
    status                 VARCHAR(16),
    completion_attempts    INTEGER,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

-- Idempotent column additions (kept for migration history / older DBs)
ALTER TABLE event_publication
    ADD COLUMN IF NOT EXISTS status VARCHAR(16);
ALTER TABLE event_publication
    ADD COLUMN IF NOT EXISTS completion_attempts INTEGER;
ALTER TABLE event_publication
    ADD COLUMN IF NOT EXISTS last_resubmission_date TIMESTAMP WITH TIME ZONE;

-- Indexes
CREATE INDEX IF NOT EXISTS event_publication_serialized_event_hash_idx
    ON event_publication USING hash (serialized_event);

CREATE INDEX IF NOT EXISTS event_publication_by_completion_date_idx
    ON event_publication (completion_date);
-- =========================================================
-- Seed: content
-- =========================================================
INSERT INTO content (title, url, type, published_at)
VALUES ('Understanding Spring Modulith', 'https://example.com/blog/spring-modulith', 'ARTICLE',
        '2026-01-10 09:00:00+00'),
       ('Kotlin Coroutines Deep Dive', 'https://example.com/blog/kotlin-coroutines', 'ARTICLE',
        '2026-02-14 10:30:00+00'),
       ('Bootiful Java at KotlinConf', 'https://example.com/videos/bootiful-java', 'VIDEO', '2026-03-05 15:00:00+00'),
       ('Event-Driven Architecture Explained', 'https://example.com/podcasts/eda-explained', 'PODCAST',
        '2026-04-20 08:00:00+00'),
       ('Reactive Spring with WebFlux', 'https://example.com/blog/reactive-webflux', 'ARTICLE',
        '2026-05-01 12:00:00+00'),
       ('Modular Monoliths in Practice', 'https://example.com/videos/modular-monoliths', 'VIDEO',
        '2026-06-11 17:45:00+00');

-- =========================================================
-- Seed: subscriber
-- =========================================================
INSERT INTO subscriber (email)
VALUES ('ahmed@example.com'),
       ('jane.doe@example.com')