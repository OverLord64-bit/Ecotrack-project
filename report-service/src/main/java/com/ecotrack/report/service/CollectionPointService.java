package com.ecotrack.report.service;

import com.ecotrack.report.domain.CollectionPoint;
import com.ecotrack.report.repo.CollectionPointRepository;
import com.ecotrack.report.web.dto.CollectionPointRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;

    public CollectionPointService(CollectionPointRepository collectionPointRepository) {
        this.collectionPointRepository = collectionPointRepository;
    }

    public List<CollectionPoint> findAll() {
        return collectionPointRepository.findAllWithWasteTypes();
    }

    public CollectionPoint findById(UUID id) {
        return collectionPointRepository.findByIdWithWasteTypes(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Collection point not found: " + id
                ));
    }
    public CollectionPoint create(CollectionPointRequest request) {
        CollectionPoint point = new CollectionPoint(
            request.name(),
            request.description(),
            request.latitude(),
            request.longitude(),
            request.address(),
            request.wasteTypes(),
            request.active()
    );

    return collectionPointRepository.save(point);
}

    public CollectionPoint update(UUID id, CollectionPointRequest request) {
        CollectionPoint point = findById(id);

        applyRequest(point, request);

        return collectionPointRepository.save(point);
    }

    public void delete(UUID id) {
        CollectionPoint point = findById(id);
        collectionPointRepository.delete(point);
    }

    private void applyRequest(
            CollectionPoint point,
            CollectionPointRequest request
    ) {
        point.setName(request.name());
        point.setDescription(request.description());
        point.setLatitude(request.latitude());
        point.setLongitude(request.longitude());
        point.setAddress(request.address());
        point.setWasteTypes(
                request.wasteTypes() != null
                        ? request.wasteTypes()
                        : List.of()
        );
        point.setActive(
                request.active() != null
                        ? request.active()
                        : true
        );
    }
}