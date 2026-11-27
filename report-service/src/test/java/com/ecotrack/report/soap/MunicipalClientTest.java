package com.ecotrack.report.soap;

import com.ecotrack.contracts.municipal.GetRecordStatusRequest;
import com.ecotrack.contracts.municipal.GetRecordStatusResponse;
import com.ecotrack.contracts.municipal.SubmitCollectionRecordRequest;
import com.ecotrack.contracts.municipal.SubmitCollectionRecordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import javax.xml.datatype.DatatypeFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

class MunicipalClientTest {

    private static final String NAMESPACE =
            "http://ecotrack.example/municipal";

    private MunicipalClient municipalClient;
    private MockWebServiceServer mockServer;

    @BeforeEach
    void setUp() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.ecotrack.contracts.municipal");
        marshaller.afterPropertiesSet();

        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri("http://localhost:8083/ws");

        mockServer = MockWebServiceServer.createServer(template);
        municipalClient = new MunicipalClient(template);
    }

    @Test
    void shouldSubmitCollectionRecord() throws Exception {
        String requestXml =
                "<SubmitCollectionRecordRequest xmlns=\"" + NAMESPACE + "\">"
                + "<recordId>11111111-1111-4111-8111-111111111111</recordId>"
                + "<reportId>22222222-2222-4222-8222-222222222222</reportId>"
                + "<collectionId>33333333-3333-4333-8333-333333333333</collectionId>"
                + "<completedAt>2026-11-27T20:00:00Z</completedAt>"
                + "<address>Monastir</address>"
                + "<notes>Collection completed</notes>"
                + "</SubmitCollectionRecordRequest>";

        String responseXml =
                "<SubmitCollectionRecordResponse xmlns=\"" + NAMESPACE + "\">"
                + "<accepted>true</accepted>"
                + "<municipalReference>MUN-2026-001</municipalReference>"
                + "<receivedAt>2026-11-27T20:01:00Z</receivedAt>"
                + "<duplicate>false</duplicate>"
                + "</SubmitCollectionRecordResponse>";

        mockServer.expect(payload(new StringSource(requestXml)))
                .andRespond(withPayload(new StringSource(responseXml)));

        SubmitCollectionRecordRequest request =
                new SubmitCollectionRecordRequest();

        request.setRecordId("11111111-1111-4111-8111-111111111111");
        request.setReportId("22222222-2222-4222-8222-222222222222");
        request.setCollectionId("33333333-3333-4333-8333-333333333333");
        request.setCompletedAt(
                DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar("2026-11-27T20:00:00Z"));
        request.setAddress("Monastir");
        request.setNotes("Collection completed");

        SubmitCollectionRecordResponse response =
                municipalClient.submitCollectionRecord(request);

        assertThat(response.isAccepted()).isTrue();
        assertThat(response.getMunicipalReference())
                .isEqualTo("MUN-2026-001");
        assertThat(response.isDuplicate()).isFalse();

        mockServer.verify();
    }

    @Test
    void shouldGetRecordStatus() {
        String requestXml =
                "<GetRecordStatusRequest xmlns=\"" + NAMESPACE + "\">"
                + "<recordId>11111111-1111-4111-8111-111111111111</recordId>"
                + "</GetRecordStatusRequest>";

        String responseXml =
                "<GetRecordStatusResponse xmlns=\"" + NAMESPACE + "\">"
                + "<found>true</found>"
                + "<status>ACCEPTED</status>"
                + "<municipalReference>MUN-2026-001</municipalReference>"
                + "</GetRecordStatusResponse>";

        mockServer.expect(payload(new StringSource(requestXml)))
                .andRespond(withPayload(new StringSource(responseXml)));

        GetRecordStatusRequest request = new GetRecordStatusRequest();
        request.setRecordId("11111111-1111-4111-8111-111111111111");

        GetRecordStatusResponse response =
                municipalClient.getRecordStatus(request);

        assertThat(response.isFound()).isTrue();
        assertThat(response.getStatus()).isEqualTo("ACCEPTED");
        assertThat(response.getMunicipalReference())
                .isEqualTo("MUN-2026-001");

        mockServer.verify();
    }
}