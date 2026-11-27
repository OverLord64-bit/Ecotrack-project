package com.ecotrack.report.web.dto;

import com.ecotrack.report.domain.WasteCategory;

public record CreateReportRequest(
    String reporterName,
    String description,
    WasteCategory wasteCategory,
    Double latitude,
    Double longitude,
    String address
) {
}