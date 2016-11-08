package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.autodetection.WebServiceAutoDetector;
import org.junit.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class SeiImplementationMissingFailureAnalyzerTest {

    private SeiImplementationMissingFailureAnalyzer analyzer = new SeiImplementationMissingFailureAnalyzer();

    @Test
    public void doesAnalysisContainCorrectDescription() throws Exception {

        Class serviceEndpointInterface = WebServiceAutoDetector.searchServiceEndpointInterface();

        SeiImplementingClassNotFoundException seiNotFoundException = new SeiImplementingClassNotFoundException("No SEI implementing class found");
        seiNotFoundException.setNotFoundClassName(serviceEndpointInterface.getName());

        FailureAnalysis failureAnalysis = analyzer.analyze(seiNotFoundException);

        assertThat(failureAnalysis.getDescription(), containsString("There was no SEI implementing class found"));
    }



}