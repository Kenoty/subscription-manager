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
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE users (
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
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
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
    event_id INT REFERENCES subscription_event(id) ON DELETE RESTRICT,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    paid_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    is_unread BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE device_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE device (
    id UUID PRIMARY KEY,
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