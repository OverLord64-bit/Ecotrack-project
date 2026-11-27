
package com.ecotrack.report.web;

import com.ecotrack.report.domain.Report;
import com.ecotrack.report.service.ReportService;
import com.ecotrack.report.web.dto.CreateReportRequest;
import com.ecotrack.report.web.dto.ReportPageResponse;
import com.ecotrack.report.web.dto.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ReportPageResponse findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReportResponse> reports = reportService.findAll(pageable)
                .map(ReportResponse::fromEntity);

        return ReportPageResponse.fromPage(reports);
    }

    @GetMapping("/{id}")
    public ReportResponse findById(@PathVariable UUID id) {
        Report report = reportService.findById(id);
        return ReportResponse.fromEntity(report);
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ReportResponse create(@RequestBody CreateReportRequest request) {
        Report report = reportService.create(request);
        return ReportResponse.fromEntity(report);
    }
}
