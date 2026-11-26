-- V2: Add geographic coordinate constraints

ALTER TABLE reports
    ADD CONSTRAINT chk_reports_latitude
        CHECK (latitude BETWEEN -90 AND 90),
    ADD CONSTRAINT chk_reports_longitude
        CHECK (longitude BETWEEN -180 AND 180);

ALTER TABLE collection_points
    ADD CONSTRAINT chk_collection_points_latitude
        CHECK (latitude BETWEEN -90 AND 90),
    ADD CONSTRAINT chk_collection_points_longitude
        CHECK (longitude BETWEEN -180 AND 180);
