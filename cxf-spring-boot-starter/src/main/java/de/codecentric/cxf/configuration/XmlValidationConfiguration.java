package de.codecentric.cxf.configuration;

import de.codecentric.cxf.xmlvalidation.CustomFaultBuilder;
import de.codecentric.cxf.xmlvalidation.SoapFaultBuilder;
import de.codecentric.cxf.xmlvalidation.XmlValidationInterceptor;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;

/**
 * Configure extended XML-Schema validation incl. customizing of the responding SoapFaults.
 * Could be activated by configuring your own FaultDetailBuilder as a {@link Bean},
 * which must implement {@link CustomFaultBuilder}. Additionally, a {@link Bean}
 * of Type {@link Endpoint} which configures a {@link EndpointImpl} is needed.
 * 
 * @author Jonas Hecht
 *
 */
@Configuration
@ConditionalOnBean(CustomFaultBuilder.class)
@ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
public class XmlValidationConfiguration {

    @Autowired
    public Endpoint endpoint;

    @Bean
    public SoapFaultBuilder soapFaultBuilder() {
        return new SoapFaultBuilder();
    }
    
    @Bean
    public AbstractSoapInterceptor soapInterceptor() {
        XmlValidationInterceptor xmlValidationInterceptor = new XmlValidationInterceptor();
        xmlValidationInterceptor.setSoapFaultBuilder(soapFaultBuilder());
        EndpointImpl endpointImpl = (EndpointImpl)endpoint; // we need the implementation here, to configure our Interceptor
        endpointImpl.getOutFaultInterceptors().add(xmlValidationInterceptor);
        return xmlValidationInterceptor;
    }
}
