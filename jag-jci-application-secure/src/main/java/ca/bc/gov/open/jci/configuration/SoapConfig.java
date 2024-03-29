package ca.bc.gov.open.jci.configuration;

import ca.bc.gov.open.jci.models.serializers.InstantDeserializer;
import ca.bc.gov.open.jci.models.serializers.InstantSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import jakarta.xml.soap.SOAPMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {
    @Value("${jci.username}")
    private String username;

    @Value("${jci.password}")
    private String password;

    @Value("${jci.ords-read-timeout}")
    private String ordsReadTimeout;

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate =
                restTemplateBuilder
                        .basicAuthentication(username, password)
                        .setReadTimeout(Duration.ofSeconds(Integer.parseInt(ordsReadTimeout)))
                        .build();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new InstantDeserializer());
        module.addSerializer(Instant.class, new InstantSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(SOAPMessage.WRITE_XML_DECLARATION, "true");
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setMessageProperties(props);
        messageFactory.setSoapVersion(SoapVersion.SOAP_12);
        return messageFactory;
    }

    @Bean(name = "CCD.Source.CivilFileContent.ws:CivilFileContentSecure")
    public Wsdl11Definition civilFileContentSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/civilFileContentSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CodeValues.ws.provider:CodeValuesSecure")
    public Wsdl11Definition codeValuesSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/codeValuesSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CourtLists.ws.provider:CourtListSecure")
    public Wsdl11Definition courtListSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/courtListSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CriminalFileContent.ws.provider:CriminalFileContentSecure")
    public Wsdl11Definition criminalFileContentSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(
                new ClassPathResource("xsdSchemas/criminalFileContentSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetROPReport.ws:GetROPReportSecure")
    public Wsdl11Definition getRopReportSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getRopReportSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetDocument.ws:GetDocumentSecure")
    public Wsdl11Definition getDocumentSecureWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getDocumentSecure.wsdl"));
        return wsdl11Definition;
    }
}
