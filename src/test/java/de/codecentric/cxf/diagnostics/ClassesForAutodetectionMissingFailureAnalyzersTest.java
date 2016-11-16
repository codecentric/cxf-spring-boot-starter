package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;
import org.junit.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import static de.codecentric.cxf.diagnostics.ClassesForAutodetectionMissingFailureAnalyzerHelper.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


public class ClassesForAutodetectionMissingFailureAnalyzersTest {

    private SeiImplMissingFailureAnalyzer seiImplMissingFailureAnalyzer = new SeiImplMissingFailureAnalyzer();
    private WebServiceClientMissingFailureAnalyzer webServiceClientMissingFailureAnalyzer = new WebServiceClientMissingFailureAnalyzer();


    @Test public void
    does_SeiImplMissing_failure_analysis_contain_correct_description() throws BootStarterCxfException {

        SeiImplClassNotFoundException seiNotFoundException = mockSeiImplClassNotFoundException();

        FailureAnalysis failureAnalysis = seiImplMissingFailureAnalyzer.analyze(seiNotFoundException);

        assertThat(failureAnalysis.getDescription(), containsString("There was no SEI implementing class found"));
    }


    @Test public void
    does_WebServiceClientMissing_failure_analysis_contain_correct_description() throws BootStarterCxfException {

        WebServiceClientNotFoundException webServiceClientNotFoundException = mockWebServiceClientNotFoundException();

        FailureAnalysis failureAnalysis = webServiceClientMissingFailureAnalyzer.analyze(webServiceClientNotFoundException);

        assertThat(failureAnalysis.getDescription(), containsString("There was no class found, that´s annotated with javax.xml.ws.WebServiceClient"));
    }




}