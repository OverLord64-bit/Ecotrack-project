package com.ecotrack.report.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reporter_name", length = 150)
    private String reporterName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "waste_category", nullable = false, length = 30)
    private WasteCategory wasteCategory;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportStatus status = ReportStatus.SUBMITTED;

    // Identifiant externe provenant du Collection Service (gRPC).
    @Column(name = "collection_id", length = 100)
    private String collectionId;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_status", length = 30)
    private TransmissionStatus transmissionStatus;

    @Column(name = "transmission_reference", length = 255)
    private String transmissionReference;

    @Column(name = "transmission_attempts", nullable = false)
    private Integer transmissionAttempts = 0;

    @Column(name = "transmission_error", columnDefinition = "TEXT")
    private String transmissionError;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Report() {
    }

    public Report(
        String reporterName,
        String description,
        WasteCategory wasteCategory,
        Double latitude,
        Double longitude,
        String address
    ) {
        this.reporterName = reporterName;
        this.description = description;
        this.wasteCategory = wasteCategory;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.status = ReportStatus.SUBMITTED;
        this.transmissionAttempts = 0;
    }
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WasteCategory getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategory(WasteCategory wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public TransmissionStatus getTransmissionStatus() {
        return transmissionStatus;
    }

    public void setTransmissionStatus(TransmissionStatus transmissionStatus) {
        this.transmissionStatus = transmissionStatus;
    }

    public String getTransmissionReference() {
        return transmissionReference;
    }

    public void setTransmissionReference(String transmissionReference) {
        this.transmissionReference = transmissionReference;
    }

    public Integer getTransmissionAttempts() {
        return transmissionAttempts;
    }

    public void setTransmissionAttempts(Integer transmissionAttempts) {
        this.transmissionAttempts = transmissionAttempts;
    }

    public String getTransmissionError() {
        return transmissionError;
    }

    public void setTransmissionError(String transmissionError) {
        this.transmissionError = transmissionError;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}