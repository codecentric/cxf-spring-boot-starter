package de.codecentric.cxf.diagnostics;

import org.junit.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import static de.codecentric.cxf.diagnostics.SeiImplementationMissingFailureAnalyzerHelper.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class SeiImplementationMissingFailureAnalyzerTest {

    private SeiImplementationMissingFailureAnalyzer analyzer = new SeiImplementationMissingFailureAnalyzer();

    @Test
    public void doesAnalysisContainCorrectDescription() throws Exception {

        SeiImplementingClassNotFoundException seiNotFoundException = constructException();

        FailureAnalysis failureAnalysis = analyzer.analyze(seiNotFoundException);

        assertThat(failureAnalysis.getDescription(), containsString("There was no SEI implementing class found"));
    }


}