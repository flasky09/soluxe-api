-- Migration V1_13: Remove currency from hotel_settings
ALTER TABLE hotel_settings DROP COLUMN currency;
