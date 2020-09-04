package de.codecentric.cxf.configuration;

import de.codecentric.cxf.autodetection.WebServiceAutoDetector;
import de.codecentric.cxf.autodetection.WebServiceScanner;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.util.Map;

/**
 * While booting up the CXF-Framework and Servlets, we don´t override the Bean "dispatcherServlet" here - because,
 * if you want to use a second Servlet (e.g. because you need some REST-Endpoint via the @RestController Annotation),
 * you just could use it. Otherwise, those Servlets would override themselfs. 
 * 
 * @author jonashecht
 *
 */
@Configuration
@ConditionalOnClass(CXFServlet.class)
@PropertySource("classpath:spring-boot-starter-cxf.properties")
@Import({
    XmlValidationConfiguration.class,
    SoapMessageLoggerConfiguration.class,
    TimeLoggingConfiguration.class
})
public class CxfAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(CxfAutoConfiguration.class);

    @Value("${soap.service.base.url:/soap-api}")
    private String baseUrl;

    @Value("${soap.service.publishedEndpointUrl:NOT_SET}")
    private String publishedEndpointUrl;

    @Value("${cxf.servicelist.title:CXF SpringBoot Starter - service list}")
    private String serviceListTitle;

    private String serviceUrlEnding = "";
    private Object seiImplementation;
    private Service webServiceClient;


    @Bean
    public WebServiceAutoDetector webServiceAutoDetector() throws BootStarterCxfException {
        return new WebServiceAutoDetector(new WebServiceScanner());
    }

    @PostConstruct
    public void setUp() throws BootStarterCxfException {
        webServiceClient = webServiceAutoDetector().searchAndInstantiateWebServiceClient();
        serviceUrlEnding = "/" + webServiceClient().getServiceName().getLocalPart();
    }

    /**
     * We mostly want to autoinitialize the Endpoint and the CXFServlet.
     * But when in client mode, this isn´t always wanted (e.g. when you are in Client
     * only mode and just want to test or call some SOAP services, but not provide
     * services on your own.
     * <br><br>
     * Because there is (&amp; sadly will be) no @ConditionalOnMissingProperty in Spring Boot
     * (https://github.com/spring-projects/spring-boot/issues/4938), we need to use a workaround:
     * <br><br>
     * If endpoint.autoinit is NOT set, Endpoint autoinitialization will run.
     * If endpoint.autoinit is set to some other value than false, autoinitialization will also run.
     * <br><br>
     * Only if endpoint.autoinit = false, the autoinitialization isn´t running.
     */
    @Bean
    @ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
    public ServletRegistrationBean cxfDispatcherServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CXFServlet(), baseUrl + "/*");

        // Add custom Title to CXF´s ServiceList
        Map<String, String> initParameters = servletRegistrationBean.getInitParameters();
        initParameters.put("service-list-title", serviceListTitle);

        return servletRegistrationBean;
    }

    // If you don´t want to import the cxf.xml-Springbean-Config you have to setUp this Bus for yourself
    // <bean id="cxf" class="org.apache.cxf.bus.spring.SpringBus" destroy-method="shutdown"/>
    @Bean(name = Bus.DEFAULT_BUS_ID)
    @ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    @ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
    public Object seiImplementation() throws BootStarterCxfException {
        if(seiImplementation == null) {
            seiImplementation = webServiceAutoDetector().searchAndInstantiateSeiImplementation();
        }
        return seiImplementation;
    }

    @Bean
    @ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
    public Endpoint endpoint() throws BootStarterCxfException {

        LOG.info("Autodetection successful. Initializing javax.xml.ws.Endpoint based on " + seiImplementation().getClass().getName());

        EndpointImpl endpoint = new EndpointImpl(springBus(), seiImplementation());
        // CXF JAX-WS implementation relies on the correct ServiceName as QName-Object with
        // the name-Attribute´s text <wsdl:service name="Weather"> and the targetNamespace
        // "http://www.codecentric.de/namespace/weatherservice/"
        // Also the WSDLLocation must be set
        endpoint.setServiceName(webServiceClient().getServiceName());
        endpoint.setWsdlLocation(webServiceClient().getWSDLDocumentLocation().toString());
        if (publishedEndpointUrl.equals("NOT_SET")) {
            endpoint.setPublishedEndpointUrl(webServiceClient.getServiceName().getLocalPart());
        } else {
            endpoint.setPublishedEndpointUrl(publishedEndpointUrl);
        }
        // publish the Service under it´s name mentioned in the WSDL inside name attribute (example: <wsdl:service name="Weather">)
        endpoint.publish(serviceUrlEnding());
        return endpoint;
    }

    @Bean
    public Service webServiceClient() throws BootStarterCxfException {
        // Needed for correct ServiceName & WSDLLocation to publish contract first incl. original WSDL
        return webServiceClient;
    }
    
    /**
     * @return the base-URL, where the WebServices are configured (eihter via property or default-value)
     */
    public String baseUrl() {
        return baseUrl;
    }

    /**
     * @return the concrete Service URL-ending, where the WebService is configured according to your WSDL´s Service Name
     * (e.g. &quot;/Weather&quot; when there is this inside your WSDL: &lt;wsdl:service name=&quot;Weather&quot;&gt;)
     */
    public String serviceUrlEnding() {
        return serviceUrlEnding;
    }

    /**
     * @return the base-URL, where the WebServices are configured (eihter via property or default-value) and appended
     * the concrete Service URL-ending, where the WebService is configured according to your WSDL´s Service Name
     * (e.g. &quot;/Weather&quot; when there is this inside your WSDL: &lt;wsdl:service name=&quot;Weather&quot;&gt;)
     */
    public String baseAndServiceEndingUrl() {
        return baseUrl() + serviceUrlEnding();
    }
    
}
