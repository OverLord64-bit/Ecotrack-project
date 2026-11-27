
package com.ecotrack.report.service;

import com.ecotrack.report.domain.Report;
import com.ecotrack.report.repo.ReportRepository;
import com.ecotrack.report.web.dto.CreateReportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report create(CreateReportRequest request) {
        Report report = new Report(
                request.reporterName(),
                request.description(),
                request.wasteCategory(),
                request.latitude(),
                request.longitude(),
                request.address()
        );

        return reportRepository.save(report);
    }

    public Page<Report> findAll(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    public Report findById(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Report not found: " + id
                ));
    }
}
