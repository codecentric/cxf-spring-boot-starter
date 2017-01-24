package de.codecentric.cxf.autodetection.diagnostics;


import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class ClassesForAutodetectionMissingFailureAnalyzersTest {

    private SeiImplMissingFailureAnalyzer seiImplMissingFailureAnalyzer = new SeiImplMissingFailureAnalyzer();
    private WebServiceClientMissingFailureAnalyzer webServiceClientMissingFailureAnalyzer = new WebServiceClientMissingFailureAnalyzer();
    private SeiMissingFailureAnalyzer seiMissingFailureAnalyzer = new SeiMissingFailureAnalyzer();


    @Test public void
    does_SeiImplMissing_failure_analysis_contain_correct_description() throws BootStarterCxfException {

        SeiImplClassNotFoundException seiNotFoundException = SeiImplClassNotFoundException.build().setNotFoundClassName(WeatherService.class.getName());

        FailureAnalysis failureAnalysis = seiImplMissingFailureAnalyzer.analyze(seiNotFoundException);

        assertThat(failureAnalysis.getDescription(), containsString(SeiImplClassNotFoundException.MESSAGE));
    }


    @Test public void
    does_WebServiceClientMissing_failure_analysis_contain_correct_description() throws BootStarterCxfException {

        FailureAnalysis failureAnalysis = webServiceClientMissingFailureAnalyzer.analyze(new WebServiceClientNotFoundException());

        assertThat(failureAnalysis.getDescription(), containsString(WebServiceClientNotFoundException.MESSAGE));
    }

    @Test public void
    does_SeiMissing_failure_analysis_contain_correct_description() {
        FailureAnalysis failureAnalysis = seiMissingFailureAnalyzer.analyze(new SeiNotFoundException());

        assertThat(failureAnalysis.getDescription(), containsString(SeiNotFoundException.MESSAGE));
    }




}