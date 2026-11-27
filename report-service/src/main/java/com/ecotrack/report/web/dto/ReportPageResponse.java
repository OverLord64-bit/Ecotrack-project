package com.ecotrack.report.web.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ReportPageResponse(
    List<ReportResponse> items,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static ReportPageResponse fromPage(Page<ReportResponse> page) {
        return new ReportPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}