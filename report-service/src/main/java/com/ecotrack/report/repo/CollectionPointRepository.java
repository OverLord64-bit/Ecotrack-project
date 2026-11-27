package com.ecotrack.report.repo;

import com.ecotrack.report.domain.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, UUID> {

    @Query("""
            SELECT DISTINCT cp
            FROM CollectionPoint cp
            LEFT JOIN FETCH cp.wasteTypes
            """)
    List<CollectionPoint> findAllWithWasteTypes();

    @Query("""
            SELECT DISTINCT cp
            FROM CollectionPoint cp
            LEFT JOIN FETCH cp.wasteTypes
            WHERE cp.id = :id
            """)
    Optional<CollectionPoint> findByIdWithWasteTypes(@Param("id") UUID id);
}