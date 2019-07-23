package de.codecentric.cxf.autodetection.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * FailureAnalyzer to show custom Failure Message, if the Service Endpoint Interface (SEI) is missing
 * (which is mandatory for autodetection and instantiation of the CXF endpoint(s))
 *
 * @author jonashackt
 */
public class SeiMissingFailureAnalyzer extends AbstractFailureAnalyzer<SeiNotFoundException>{

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, SeiNotFoundException cause) {
        return new FailureAnalysis(SeiNotFoundException.MESSAGE,
                "Use the cxf-spring-boot-starter-maven-plugin (https://github.com/codecentric/cxf-spring-boot-starter-maven-plugin) " +
                        "to generate the needed class files from your WSDL & XSDs", cause);
    }
}
