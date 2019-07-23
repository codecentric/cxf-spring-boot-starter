package de.codecentric.cxf.autodetection.diagnostics;

import org.junit.Test;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class CxfSpringBootMavenPropertiesMissingFailureAnalyzerTest {

    private CxfSpringBootMavenPropertiesMissingFailureAnalyzer cxfSpringBootMavenPropertiesMissingFailureAnalyzer = new CxfSpringBootMavenPropertiesMissingFailureAnalyzer();


    @Test public void
    should_react_with_cxf_spring_boot_maven_properties_missing_failure_analyzer() throws CxfSpringBootMavenPropertiesNotFoundException {
        FailureAnalysis failureAnalysis = cxfSpringBootMavenPropertiesMissingFailureAnalyzer.analyze(new CxfSpringBootMavenPropertiesNotFoundException(
                "Could not read packageNames from cxf-spring-boot-maven.properties.", new FileNotFoundException()));

        assertThat(failureAnalysis.getDescription(), containsString(CxfSpringBootMavenPropertiesNotFoundException.MESSAGE));
    }


}
