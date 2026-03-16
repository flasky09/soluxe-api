-- Migration V1_12: Sync entities with schema and set default currency

-- 1. Add missing columns
ALTER TABLE room_types ADD COLUMN shortcut VARCHAR(50);

-- 2. Ensure departments matches entity (description already in V1_2 but for safety)
-- In V1_2 it was: description VARCHAR(255)
-- No action needed if already there, but we can ensure it exists if missing in some envs
-- However, Flyway normally handles this. If I want to be safe for H2:
-- ALTER TABLE departments ADD COLUMN IF NOT EXISTS description VARCHAR(255); -- MySQL 8.0.1+

-- 3. Set default currency to USD in hotel_settings
UPDATE hotel_settings SET currency = 'USD' WHERE currency IS NULL OR currency = 'KES';
