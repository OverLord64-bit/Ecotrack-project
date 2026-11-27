package com.ecotrack.report.collection;

import com.ecotrack.contracts.collection.v1.CollectionInfo;
import com.ecotrack.contracts.collection.v1.CollectionStatus;
import com.ecotrack.contracts.collection.v1.GetCollectionRequest;
import com.ecotrack.contracts.collection.v1.PlanCollectionRequest;
import com.ecotrack.contracts.collection.v1.Priority;
import com.ecotrack.contracts.collection.v1.WatchStatusRequest;
import com.ecotrack.contracts.collection.v1.CollectionStatusEvent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(
        named = "ecotrack.grpc.integration",
        matches = "true"
)
class CollectionClientIntegrationTest {

    @Test
    @Timeout(value = 90, unit = TimeUnit.SECONDS)
    void shouldPlanRetrieveAndWatchCollection() throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        try {
            CollectionClient client = new CollectionClient(
                    channel,
                    5000,
                    60000
            );

            // 1. Preparer une demande de planification unique.
            String requestId = UUID.randomUUID().toString();
            String reportId = UUID.randomUUID().toString();

            Instant scheduledFor = Instant.now().plusSeconds(60);

            PlanCollectionRequest request = PlanCollectionRequest.newBuilder()
                    .setRequestId(requestId)
                    .setReportId(reportId)
                    .setAddress("Test integration - EcoTrack")
                    .setScheduledFor(
                            com.google.protobuf.Timestamp.newBuilder()
                                    .setSeconds(scheduledFor.getEpochSecond())
                                    .setNanos(scheduledFor.getNano())
                                    .build()
                    )
                    .setPriority(Priority.PRIORITY_NORMAL)
                    .build();

            // 2. Tester PlanCollection.
            CollectionInfo planned = client.planCollection(request);

            assertNotNull(planned);
            assertFalse(planned.getId().isBlank());
            assertEquals(reportId, planned.getReportId());

            String collectionId = planned.getId();

            // 3. Tester GetCollection.
            GetCollectionRequest getRequest =
                    GetCollectionRequest.newBuilder()
                            .setCollectionId(collectionId)
                            .build();

            CollectionInfo retrieved = client.getCollection(getRequest);

            assertEquals(collectionId, retrieved.getId());
            assertEquals(reportId, retrieved.getReportId());

            // 4. Tester WatchStatus.
            WatchStatusRequest watchRequest =
                    WatchStatusRequest.newBuilder()
                            .setCollectionId(collectionId)
                            .build();

            List<CollectionStatus> receivedStatuses = new ArrayList<>();

            java.util.Iterator<CollectionStatusEvent> events =
                    client.watchStatus(watchRequest);

            while (events.hasNext()) {
                CollectionStatusEvent event = events.next();
                receivedStatuses.add(event.getStatus());

                if (event.getStatus() == CollectionStatus.COLLECTION_STATUS_COMPLETED) {
                    break;
                }
            }

            // 5. Verifier que le flux a bien atteint COMPLETED.
            assertFalse(receivedStatuses.isEmpty());
            assertTrue(
                    receivedStatuses.contains(
                            CollectionStatus.COLLECTION_STATUS_COMPLETED
                    ),
                    "The collection stream should reach COMPLETED"
            );

            System.out.println("gRPC integration test successful.");
            System.out.println("Collection ID: " + collectionId);
            System.out.println("Received statuses: " + receivedStatuses);

        } finally {
            channel.shutdownNow();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}