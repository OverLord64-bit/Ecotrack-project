package com.ecotrack.report.web.dto;

import com.ecotrack.report.domain.WasteCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CollectionPointRequest(
    @NotBlank(message = "Collection point name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    String name,

    String description,

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
    @DecimalMax(value = "90.0", message = "Latitude must not exceed 90")
    Double latitude,

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
    @DecimalMax(value = "180.0", message = "Longitude must not exceed 180")
    Double longitude,

    @Size(max = 500, message = "Address must not exceed 500 characters")
    String address,

    List<WasteCategory> wasteTypes,

    Boolean active
) {}