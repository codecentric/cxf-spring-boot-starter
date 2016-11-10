package de.codecentric.cxf.configuration;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import de.codecentric.cxf.autodetection.WebServiceAutoDetector;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import de.codecentric.cxf.logging.LogCorrelationFilter;

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
    SoapMessageLoggerConfiguration.class
})
public class CxfAutoConfiguration {

    @Value("${soap.service.base.url:/soap-api}")
    private String baseUrl;

    @Value("${cxf.servicelist.title:CXF SpringBoot Starter - service list}")
    private String serviceListTitle;

    private String serviceUrlEnding = "";

    private WebServiceAutoDetector webServiceAutoDetector = new WebServiceAutoDetector();;

    @PostConstruct
    public void setUp() throws BootStarterCxfException {
        serviceUrlEnding = "/" + webServiceClient().getServiceName().getLocalPart();
    }

    @Bean
    public ServletRegistrationBean cxfDispatcherServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CXFServlet(), baseUrl + "/*");

        // Add custom Title to CXF´s ServiceList
        Map<String, String> initParameters = servletRegistrationBean.getInitParameters();
        initParameters.put("service-list-title", serviceListTitle);
        
        return servletRegistrationBean;
    }
    
    // If you don´t want to import the cxf.xml-Springbean-Config you have to setUp this Bus for yourself
    // <bean id="cxf" class="org.apache.cxf.bus.spring.SpringBus" destroy-method="shutdown"/>
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Object seiImplementation() throws BootStarterCxfException {
        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();
        return webServiceAutoDetector.searchAndInstantiateSeiImplementation(serviceEndpointInterface);
    }

    @Bean
    public Endpoint endpoint() throws BootStarterCxfException {
        EndpointImpl endpoint = new EndpointImpl(springBus(), seiImplementation());
        // CXF JAX-WS implementation relies on the correct ServiceName as QName-Object with
        // the name-Attribute´s text <wsdl:service name="Weather"> and the targetNamespace
        // "http://www.codecentric.de/namespace/weatherservice/"
        // Also the WSDLLocation must be set
        endpoint.setServiceName(webServiceClient().getServiceName());
        endpoint.setWsdlLocation(webServiceClient().getWSDLDocumentLocation().toString());
        // publish the Service under it´s name mentioned in the WSDL inside name attribute (example: <wsdl:service name="Weather">)
        endpoint.publish(serviceUrlEnding);
        return endpoint;
    }

    @Bean
    public Service webServiceClient() throws BootStarterCxfException {
        // Needed for correct ServiceName & WSDLLocation to publish contract first incl. original WSDL
        return webServiceAutoDetector.searchAndInstantiateWebServiceClient();
    }
    
    /**
     * @return the base-URL, where the WebServices are configured (eihter via property or default-value)
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @return the concrete Service URL-ending, where the WebService is configured according to your WSDL´s Service Name
     * (e.g. &quot;/Weather&quot; when there is this inside your WSDL: &lt;wsdl:service name=&quot;Weather&quot;&gt;)
     */
    public String getServiceUrlEnding() {
        return serviceUrlEnding;
    }

    /**
     * @return the base-URL, where the WebServices are configured (eihter via property or default-value) and appended
     * the concrete Service URL-ending, where the WebService is configured according to your WSDL´s Service Name
     * (e.g. &quot;/Weather&quot; when there is this inside your WSDL: &lt;wsdl:service name=&quot;Weather&quot;&gt;)
     */
    public String getBaseAndServiceEndingUrl() {
        return baseUrl + serviceUrlEnding;
    }
    
    // Register Filter for Correlating Logmessages from the same Service-Consumer
    @Bean
    public Filter filter() {
        return new LogCorrelationFilter();
    }

}
