package com.ecotrack.report.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "collection_points")
public class CollectionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String address;

    @ElementCollection(targetClass = WasteCategory.class)
    @CollectionTable(
        name = "collection_point_waste_types",
        joinColumns = @JoinColumn(name = "collection_point_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "waste_type", nullable = false, length = 30)
    private List<WasteCategory> wasteTypes = new ArrayList<>();

    @Column(nullable = false)
    private Boolean active = true;

    protected CollectionPoint() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<WasteCategory> getWasteTypes() {
        return wasteTypes;
    }

    public void setWasteTypes(List<WasteCategory> wasteTypes) {
        this.wasteTypes = wasteTypes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}