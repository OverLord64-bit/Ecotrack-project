package com.ecotrack.report.collection;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de la communication gRPC
 * entre Report Service et Collection Service.
 */
@Configuration
public class CollectionGrpcConfiguration {

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel collectionManagedChannel(
            @Value("${COLLECTION_GRPC_HOST:localhost}") String host,
            @Value("${COLLECTION_GRPC_PORT:9090}") int port
    ) {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public CollectionClient collectionClient(
            ManagedChannel collectionManagedChannel,
            @Value("${COLLECTION_GRPC_UNARY_DEADLINE_MS:3000}") long unaryDeadlineMs,
            @Value("${COLLECTION_GRPC_STREAM_DEADLINE_MS:300000}") long streamDeadlineMs
    ) {
        return new CollectionClient(
                collectionManagedChannel,
                unaryDeadlineMs,
                streamDeadlineMs
        );
    }
}