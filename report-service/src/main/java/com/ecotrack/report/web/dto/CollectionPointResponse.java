package com.ecotrack.report.web.dto;

import com.ecotrack.report.domain.CollectionPoint;
import com.ecotrack.report.domain.WasteCategory;

import java.util.List;
import java.util.UUID;

public record CollectionPointResponse(
    UUID id,
    String name,
    String description,
    Double latitude,
    Double longitude,
    String address,
    List<WasteCategory> wasteTypes,
    Boolean active
) {
    public static CollectionPointResponse fromEntity(CollectionPoint point) {
        return new CollectionPointResponse(
            point.getId(),
            point.getName(),
            point.getDescription(),
            point.getLatitude(),
            point.getLongitude(),
            point.getAddress(),
            point.getWasteTypes(),
            point.getActive()
        );
    }
}