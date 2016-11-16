package de.codecentric.cxf.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * FailureAnalyzer to show custom Failure Message, if a WebServiceClient annotated class is missing
 * (which is mandatory for autodetection and instantiation of the CXF endpoint(s))
 *
 * @author jonashackt
 */
public class WebServiceClientMissingFailureAnalyzer extends AbstractFailureAnalyzer<WebServiceClientNotFoundException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, WebServiceClientNotFoundException cause) {
        return new FailureAnalysis("There was no class found, that´s annotated with javax.xml.ws.WebServiceClient and implements javax.xml.ws.Service",
                "Use the cxf-spring-boot-starter-maven-plugin (https://github.com/codecentric/cxf-spring-boot-starter-maven-plugin) " +
                        "to generate the needed class files from your WSDL & XSDs", cause);
    }
}
