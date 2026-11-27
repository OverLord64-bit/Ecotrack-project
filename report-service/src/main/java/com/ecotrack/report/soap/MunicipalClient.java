package com.ecotrack.report.soap;

import com.ecotrack.contracts.municipal.GetRecordStatusRequest;
import com.ecotrack.contracts.municipal.GetRecordStatusResponse;
import com.ecotrack.contracts.municipal.SubmitCollectionRecordRequest;
import com.ecotrack.contracts.municipal.SubmitCollectionRecordResponse;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class MunicipalClient {

    private final WebServiceTemplate webServiceTemplate;

    public MunicipalClient(WebServiceTemplate municipalWebServiceTemplate) {
        this.webServiceTemplate = municipalWebServiceTemplate;
    }

    /**
     * Transmet un enregistrement de collecte au service municipal.
     */
    public SubmitCollectionRecordResponse submitCollectionRecord(
            SubmitCollectionRecordRequest request) {

        Object response = webServiceTemplate.marshalSendAndReceive(request);

        if (!(response instanceof SubmitCollectionRecordResponse)) {
            throw new IllegalStateException(
                    "Unexpected SOAP response for SubmitCollectionRecord");
        }

        return (SubmitCollectionRecordResponse) response;
    }

    /**
     * Consulte le statut d'un enregistrement auprès du service municipal.
     */
    public GetRecordStatusResponse getRecordStatus(
            GetRecordStatusRequest request) {

        Object response = webServiceTemplate.marshalSendAndReceive(request);

        if (!(response instanceof GetRecordStatusResponse)) {
            throw new IllegalStateException(
                    "Unexpected SOAP response for GetRecordStatus");
        }

        return (GetRecordStatusResponse) response;
    }
}