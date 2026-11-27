package com.ecotrack.report.soap;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

@Configuration
public class MunicipalSoapConfiguration {

    @Value("${ecotrack.municipal.soap.uri:http://localhost:8083/ws}")
    private String municipalSoapUri;

    @Value("${ecotrack.municipal.soap.connection-timeout:3000}")
    private int connectionTimeout;

    @Value("${ecotrack.municipal.soap.read-timeout:5000}")
    private int readTimeout;

    @Bean
    public Jaxb2Marshaller municipalMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.ecotrack.contracts.municipal");
        return marshaller;
    }

    @Bean
    public HttpUrlConnectionMessageSender municipalMessageSender() {
        HttpUrlConnectionMessageSender sender =
                new HttpUrlConnectionMessageSender();

        sender.setConnectionTimeout(Duration.ofMillis(connectionTimeout));
        sender.setReadTimeout(Duration.ofMillis(readTimeout));

        return sender;
    }

    @Bean
    public WebServiceTemplate municipalWebServiceTemplate(
            Jaxb2Marshaller municipalMarshaller,
            HttpUrlConnectionMessageSender municipalMessageSender) {

        WebServiceTemplate template = new WebServiceTemplate();

        template.setMarshaller(municipalMarshaller);
        template.setUnmarshaller(municipalMarshaller);
        template.setDefaultUri(municipalSoapUri);
        template.setMessageSender(municipalMessageSender);

        return template;
    }
}