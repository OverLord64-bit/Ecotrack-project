-- V1: Create Report Service tables

CREATE TABLE reports (
    id UUID PRIMARY KEY,
    reporter_name VARCHAR(150),
    description TEXT NOT NULL,
    waste_category VARCHAR(30) NOT NULL
        CHECK (waste_category IN ('HOUSEHOLD', 'BULKY', 'CONSTRUCTION', 'OTHER')),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED'
        CHECK (status IN (
            'SUBMITTED', 'PENDING_PLANNING', 'PLANNED',
            'IN_PROGRESS', 'RESOLVED', 'REJECTED'
        )),
    collection_id VARCHAR(100),
    photo_url VARCHAR(1000),
    transmission_status VARCHAR(30)
        CHECK (transmission_status IS NULL OR transmission_status IN (
            'PENDING_TRANSMISSION', 'TRANSMITTED', 'REFUSED'
        )),
    transmission_reference VARCHAR(255),
    transmission_attempts INTEGER NOT NULL DEFAULT 0
        CHECK (transmission_attempts >= 0),
    transmission_error TEXT,
    resolved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_created_at ON reports(created_at);

CREATE TABLE collection_points (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE collection_point_waste_types (
    collection_point_id UUID NOT NULL,
    waste_type VARCHAR(30) NOT NULL
        CHECK (waste_type IN ('HOUSEHOLD', 'BULKY', 'CONSTRUCTION', 'OTHER')),

    CONSTRAINT fk_collection_point_waste_types
        FOREIGN KEY (collection_point_id)
        REFERENCES collection_points(id)
        ON DELETE CASCADE,

    CONSTRAINT pk_collection_point_waste_types
        PRIMARY KEY (collection_point_id, waste_type)
);
