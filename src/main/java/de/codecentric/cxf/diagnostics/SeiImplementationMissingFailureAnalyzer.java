package de.codecentric.cxf.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * FailureAnalyzer to show custom Failure Message, if a SEI Implementation is missing (which is mandatory for
 *
 * Created by jonashecht on 07.11.16.
 */
public class SeiImplementationMissingFailureAnalyzer extends AbstractFailureAnalyzer<SeiImplementingClassNotFoundException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, SeiImplementingClassNotFoundException cause) {
        return new FailureAnalysis("There was no SEI implementing class found.",
                String.format("Build a Class that implements your Service Endpoint Interface (SEI): '%s' and try again!", cause.getNotFoundClassName()), cause);
    }

}
