package com.ecotrack.report.collection;

import com.ecotrack.contracts.collection.v1.CollectionInfo;
import com.ecotrack.contracts.collection.v1.CollectionServiceGrpc;
import com.ecotrack.contracts.collection.v1.CollectionStatusEvent;
import com.ecotrack.contracts.collection.v1.GetCollectionRequest;
import com.ecotrack.contracts.collection.v1.PlanCollectionRequest;
import com.ecotrack.contracts.collection.v1.WatchStatusRequest;

import io.grpc.ManagedChannel;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Client gRPC permettant au Report Service
 * de communiquer avec le Collection Service.
 */
public class CollectionClient {

    private final CollectionServiceGrpc.CollectionServiceBlockingStub blockingStub;

    private final long unaryDeadlineMs;
    private final long streamDeadlineMs;

    public CollectionClient(
            ManagedChannel channel,
            long unaryDeadlineMs,
            long streamDeadlineMs
    ) {
        this.blockingStub = CollectionServiceGrpc.newBlockingStub(channel);
        this.unaryDeadlineMs = unaryDeadlineMs;
        this.streamDeadlineMs = streamDeadlineMs;
    }

    /**
     * Demande la planification d'une collecte.
     */
    public CollectionInfo planCollection(PlanCollectionRequest request) {
        return blockingStub
                .withDeadlineAfter(unaryDeadlineMs, TimeUnit.MILLISECONDS)
                .planCollection(request);
    }

    /**
     * Recupere les informations d'une collecte.
     */
    public CollectionInfo getCollection(GetCollectionRequest request) {
        return blockingStub
                .withDeadlineAfter(unaryDeadlineMs, TimeUnit.MILLISECONDS)
                .getCollection(request);
    }

    /**
     * S'abonne aux evenements de changement de statut.
     *
     * Le deadline est applique a l'appel streaming.
     * L'Iterator permet de lire les evenements recus.
     */
    public Iterator<CollectionStatusEvent> watchStatus(WatchStatusRequest request) {
        return blockingStub
                .withDeadlineAfter(streamDeadlineMs, TimeUnit.MILLISECONDS)
                .watchStatus(request);
    }
}