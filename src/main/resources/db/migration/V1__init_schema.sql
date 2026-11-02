-- ==============================================================================
-- Flyway Database Migration: V1__init_schema.sql
-- Description: Initial schema for Job Profile Manager (Master Profile Module)
-- ==============================================================================

CREATE TABLE IF NOT EXISTS profiles (
    id UUID PRIMARY KEY,
    user_id UUID,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    headline VARCHAR(200),
    current_designation VARCHAR(150) NOT NULL,
    summary TEXT,
    location VARCHAR(150),
    website_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_profiles_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_profiles_email ON profiles (email);
CREATE INDEX IF NOT EXISTS idx_profiles_user_id ON profiles (user_id);
CREATE INDEX IF NOT EXISTS idx_profiles_status ON profiles (status);
