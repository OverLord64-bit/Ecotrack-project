package com.ecotrack.report.web.dto;

import com.ecotrack.report.domain.Report;
import com.ecotrack.report.domain.ReportStatus;
import com.ecotrack.report.domain.TransmissionStatus;
import com.ecotrack.report.domain.WasteCategory;

import java.time.Instant;
import java.util.UUID;

public record ReportResponse(
    UUID id,
    String reporterName,
    String description,
    WasteCategory wasteCategory,
    Double latitude,
    Double longitude,
    String address,
    ReportStatus status,
    String collectionId,
    String photoUrl,
    TransmissionStatus transmissionStatus,
    String transmissionReference,
    Integer transmissionAttempts,
    String transmissionError,
    Instant resolvedAt,
    Instant createdAt,
    Instant updatedAt
) {
    public static ReportResponse fromEntity(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getReporterName(),
            report.getDescription(),
            report.getWasteCategory(),
            report.getLatitude(),
            report.getLongitude(),
            report.getAddress(),
            report.getStatus(),
            report.getCollectionId(),
            report.getPhotoUrl(),
            report.getTransmissionStatus(),
            report.getTransmissionReference(),
            report.getTransmissionAttempts(),
            report.getTransmissionError(),
            report.getResolvedAt(),
            report.getCreatedAt(),
            report.getUpdatedAt()
        );
    }
}