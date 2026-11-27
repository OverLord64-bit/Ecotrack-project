package com.ecotrack.report.web;

import com.ecotrack.report.domain.CollectionPoint;
import com.ecotrack.report.service.CollectionPointService;
import com.ecotrack.report.web.dto.CollectionPointRequest;
import com.ecotrack.report.web.dto.CollectionPointResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/collection-points")
public class CollectionPointController {

    private final CollectionPointService collectionPointService;

    public CollectionPointController(CollectionPointService collectionPointService) {
        this.collectionPointService = collectionPointService;
    }

    @GetMapping
    public List<CollectionPointResponse> findAll() {
        return collectionPointService.findAll()
                .stream()
                .map(CollectionPointResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public CollectionPointResponse findById(@PathVariable UUID id) {
        CollectionPoint point = collectionPointService.findById(id);
        return CollectionPointResponse.fromEntity(point);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionPointResponse create(@RequestBody CollectionPointRequest request) {
        CollectionPoint point = collectionPointService.create(request);
        return CollectionPointResponse.fromEntity(point);
    }

    @PutMapping("/{id}")
    public CollectionPointResponse update(
            @PathVariable UUID id,
            @RequestBody CollectionPointRequest request
    ) {
        CollectionPoint point = collectionPointService.update(id, request);
        return CollectionPointResponse.fromEntity(point);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        collectionPointService.delete(id);
    }
}