-- Master DB: Tenant configuration table
-- Maps subdomains to per-tenant database keys

CREATE TABLE IF NOT EXISTS tenant_config (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    subdomain   VARCHAR(50)  NOT NULL UNIQUE,
    hotel_name  VARCHAR(100) NOT NULL,
    primary_color VARCHAR(7),
    logo_url    TEXT,
    db_key      VARCHAR(50)  NOT NULL,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
