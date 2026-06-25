DROP TABLE IF EXISTS subscription_device;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS device_type;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS subscription_event;
DROP TABLE IF EXISTS event_type;
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS plan;
DROP TABLE IF EXISTS billing_period;
DROP TABLE IF EXISTS service;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    role_id INT NOT NULL REFERENCES role(id) ON DELETE RESTRICT,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL,
    description TEXT,  
    website_url VARCHAR(512),
    logo_url VARCHAR(512)
);

CREATE TABLE billing_period (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE plan (
    id SERIAL PRIMARY KEY,
    service_id INT NOT NULL REFERENCES service(id) ON DELETE RESTRICT,
    name VARCHAR(64) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    billing_period_id INT NOT NULL REFERENCES billing_period(id) ON DELETE RESTRICT,
    max_devices INT
);

CREATE TABLE subscription (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    plan_id INT NOT NULL REFERENCES plan(id) ON DELETE RESTRICT,
    auto_renew BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE event_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE subscription_event (
    id SERIAL PRIMARY KEY,
    subscription_id INT NOT NULL REFERENCES subscription(id) ON DELETE CASCADE,
    event_id INT NOT NULL REFERENCES event_type(id) ON DELETE RESTRICT,
    event_date DATE NOT NULL,
    days INT
);

CREATE TABLE payment (
    id SERIAL PRIMARY KEY,
    event_id INT NOT NULL REFERENCES subscription_event(id) ON DELETE RESTRICT,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    paid_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    is_unread BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE device_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE device (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    type_id INT NOT NULL REFERENCES device_type(id) ON DELETE RESTRICT,
    note TEXT
);

CREATE TABLE subscription_device (
    device_id UUID NOT NULL REFERENCES device(id) ON DELETE CASCADE, 
    subscription_id INT NOT NULL REFERENCES subscription(id) ON DELETE CASCADE,
    PRIMARY KEY (device_id, subscription_id),
    added_at TIMESTAMPTZ DEFAULT NOW(),
    removed_at TIMESTAMPTZ
);

INSERT INTO role (name) VALUES
    ('admin'),
    ('user');

INSERT INTO "user" (role_id, first_name, last_name, email, password_hash, created_at) VALUES
    (1, 'Alice',   'Johnson',   'alice.johnson@example.com',   '$2b$12$AAAbbbCCCdddEEEfffGGGh1', '2025-01-15 09:00:00+00'),
    (2, 'Bob',     'Smith',     'bob.smith@example.com',       '$2b$12$AAAbbbCCCdddEEEfffGGGh2', '2025-02-10 10:30:00+00'),
    (2, 'Carol',   'Williams',  'carol.williams@example.com',  '$2b$12$AAAbbbCCCdddEEEfffGGGh3', '2025-03-05 11:00:00+00'),
    (2, 'David',   'Brown',     'david.brown@example.com',     '$2b$12$AAAbbbCCCdddEEEfffGGGh4', '2025-03-20 08:45:00+00'),
    (2, 'Eva',     'Davis',     'eva.davis@example.com',       '$2b$12$AAAbbbCCCdddEEEfffGGGh5', '2025-04-01 14:00:00+00'),
    (1, 'Frank',   'Martinez',  'frank.martinez@example.com',  '$2b$12$AAAbbbCCCdddEEEfffGGGh6', '2025-06-18 16:20:00+00'),
    (2, 'Grace',   'Garcia',    'grace.garcia@example.com',    '$2b$12$AAAbbbCCCdddEEEfffGGGh7', '2025-08-03 09:15:00+00'),
    (2, 'Henry',   'Wilson',    'henry.wilson@example.com',    '$2b$12$AAAbbbCCCdddEEEfffGGGh8', '2025-10-22 12:00:00+00'),
    (2, 'Irene',   'Anderson',  'irene.anderson@example.com',  '$2b$12$AAAbbbCCCdddEEEfffGGGh9', '2026-01-07 17:30:00+00'),
    (2, 'Jack',    'Taylor',    'jack.taylor@example.com',     '$2b$12$AAAbbbCCCdddEEEfffGGGhA', '2026-03-25 13:45:00+00');

INSERT INTO service (name, description, website_url, logo_url) VALUES
    ('Netflix',         'Video streaming service',       'https://www.netflix.com',   'https://cdn.example.com/logos/netflix.png'),
    ('Spotify',         'Music streaming service',       'https://www.spotify.com',   'https://cdn.example.com/logos/spotify.png'),
    ('Adobe CC',        'Creative cloud suite',          'https://www.adobe.com',     'https://cdn.example.com/logos/adobe.png'),
    ('Microsoft 365',   'Office productivity suite',     'https://www.microsoft.com', 'https://cdn.example.com/logos/ms365.png'),
    ('YouTube Premium', 'Ad-free video & music',         'https://www.youtube.com',   'https://cdn.example.com/logos/youtube.png'),
    ('iCloud',          'Apple cloud storage',           'https://www.icloud.com',    'https://cdn.example.com/logos/icloud.png'),
    ('NordVPN',         'VPN service',                   'https://www.nordvpn.com',   'https://cdn.example.com/logos/nordvpn.png'),
    ('Notion',          'All-in-one workspace',          'https://www.notion.so',     'https://cdn.example.com/logos/notion.png'),
    ('GitHub',          'Code hosting & collaboration',  'https://www.github.com',    'https://cdn.example.com/logos/github.png'),
    ('Duolingo',        'Language learning platform',    'https://www.duolingo.com',  'https://cdn.example.com/logos/duolingo.png');

INSERT INTO billing_period (name) VALUES
    ('daily'),
    ('weekly'),
    ('monthly'),
    ('yearly');

INSERT INTO plan (service_id, name, price, currency, billing_period_id, max_devices) VALUES
    (1, 'Standard',       13.99, 'EUR', 3, 2),
    (1, 'Premium',       179.99, 'EUR', 4, 4),
    (2, 'Individual',      9.99, 'EUR', 3, 1),
    (2, 'Family',         14.99, 'EUR', 3, 6),
    (3, 'Single App',     24.99, 'USD', 3, 2),
    (3, 'All Apps',       59.99, 'USD', 3, 5),
    (4, 'Personal',        6.99, 'EUR', 3, 5),
    (4, 'Business',       99.99, 'EUR', 4, 5),
    (5, 'Premium',        13.99, 'USD', 3, 3),
    (6, 'iCloud 200GB',    2.99, 'EUR', 3, NULL);

INSERT INTO subscription (user_id, plan_id, auto_renew) VALUES
    (1,  1, TRUE),
    (2,  3, TRUE),
    (3,  7, FALSE),
    (4,  2, TRUE),
    (5,  5, TRUE),
    (6,  9, FALSE),
    (7,  4, TRUE),
    (8,  6, TRUE),
    (9, 10, FALSE),
    (10, 8, TRUE);

INSERT INTO event_type (name) VALUES
    ('activated'),
    ('renewed'),
    ('cancelled'),
    ('paused'),
    ('resumed'),
    ('upgraded'),
    ('downgraded'),
    ('expired'),
    ('trial_started'),
    ('trial_ended');

INSERT INTO subscription_event (subscription_id, event_id, event_date, days) VALUES
    (1,  1, '2025-01-15', 30),
    (2,  1, '2025-02-10', 30),
    (3,  1, '2025-03-05', 365),
    (4,  2, '2025-04-01', 30),
    (5,  1, '2025-06-18', 30),
    (6,  3, '2025-08-01', NULL),
    (7,  2, '2025-10-22', 30),
    (8,  6, '2026-01-01', 30),
    (9,  4, '2026-02-10', NULL),
    (10, 1, '2026-03-25', 365);

INSERT INTO payment (event_id, amount, currency, paid_at) VALUES
    (1,  13.99,  'EUR', '2025-01-15 09:05:00+00'),
    (2,   9.99,  'EUR', '2025-02-10 10:35:00+00'),
    (3,   6.99,  'EUR', '2025-03-05 11:05:00+00'),
    (4, 179.99,  'EUR', '2025-04-01 14:05:00+00'),
    (5,  24.99,  'USD', '2025-06-18 16:25:00+00'),
    (7,  14.99,  'EUR', '2025-10-22 12:05:00+00'),
    (8,  59.99,  'USD', '2026-01-01 08:00:00+00'),
    (10, 99.99,  'EUR', '2026-03-25 13:50:00+00'),
    (1,  13.99,  'EUR', '2025-02-15 09:05:00+00'),
    (2,   9.99,  'EUR', '2025-03-10 10:35:00+00');

INSERT INTO notification (user_id, message, is_unread, created_at) VALUES
    (1,  'Your Netflix subscription has been renewed.',           TRUE,  '2025-02-15 09:06:00+00'),
    (2,  'Your Spotify subscription is active.',                  FALSE, '2025-02-10 10:36:00+00'),
    (3,  'Your Microsoft 365 plan renews in 7 days.',             TRUE,  '2025-03-28 08:00:00+00'),
    (4,  'Payment received for Netflix Premium.',                 FALSE, '2025-04-01 14:06:00+00'),
    (5,  'Adobe CC trial period ends tomorrow.',                  TRUE,  '2025-06-17 10:00:00+00'),
    (6,  'Your YouTube Premium subscription was cancelled.',      TRUE,  '2025-08-01 12:00:00+00'),
    (7,  'Spotify Family plan renewed successfully.',             FALSE, '2025-10-22 12:06:00+00'),
    (8,  'Adobe CC All Apps plan upgraded.',                      TRUE,  '2026-01-01 08:01:00+00'),
    (9,  'iCloud subscription paused.',                           TRUE,  '2026-02-10 09:00:00+00'),
    (10, 'Microsoft 365 Business plan activated.',                FALSE, '2026-03-25 13:51:00+00');

INSERT INTO device_type (name) VALUES
    ('smartphone'),
    ('tablet'),
    ('laptop'),
    ('desktop'),
    ('smart_tv'),
    ('game_console'),
    ('e-reader'),
    ('smartwatch'),
    ('streaming_stick');

INSERT INTO device (id, name, type_id, note) VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Alice iPhone 15',    1, 'Primary phone'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Bob MacBook Pro',    3, 'Work laptop'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Carol iPad Air',     2, NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'David Samsung TV',   5, 'Living room TV'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Eva Surface Pro',    3, 'Home laptop'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Frank PS5',          6, NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Grace Pixel 8',      1, 'Personal phone'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Henry Desktop PC',   4, 'Gaming rig'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Irene Kindle',       7, NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Jack Chromecast',    9, 'Bedroom TV stick');

INSERT INTO subscription_device (device_id, subscription_id, added_at, removed_at) VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 1,  '2025-01-15 09:10:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 2,  '2025-02-10 10:40:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 3,  '2025-03-05 11:10:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 4,  '2025-04-01 14:10:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 5,  '2025-06-18 16:30:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 6,  '2025-08-01 12:05:00+00', '2025-08-01 12:05:00+00'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 7,  '2025-10-22 12:10:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 8,  '2026-01-01 08:05:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 9,  '2026-02-10 09:05:00+00', NULL),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 10, '2026-03-25 13:55:00+00', NULL);
