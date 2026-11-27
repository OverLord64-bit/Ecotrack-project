package com.ecotrack.report.web.dto;

import com.ecotrack.report.domain.WasteCategory;
import java.util.List;

public record CollectionPointRequest(
        String name,
        String description,
        Double latitude,
        Double longitude,
        String address,
        List<WasteCategory> wasteTypes,
        Boolean active
) {}