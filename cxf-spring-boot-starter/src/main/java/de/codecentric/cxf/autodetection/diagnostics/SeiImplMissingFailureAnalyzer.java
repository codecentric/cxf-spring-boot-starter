package de.codecentric.cxf.autodetection.diagnostics;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * FailureAnalyzer to show custom Failure Message, if a SEI Implementation is missing
 * (which is mandatory for autodetection and instantiation of the CXF endpoint(s))
 *
 * @author jonashackt
 */
public class SeiImplMissingFailureAnalyzer extends AbstractFailureAnalyzer<SeiImplClassNotFoundException> {

    public static final String MESSAGE = "Build a Class that implements your Service Endpoint Interface (SEI): '%s' and is present in a scanned sub-package of: '%s'. ";

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, SeiImplClassNotFoundException cause) {
        return new FailureAnalysis(SeiImplClassNotFoundException.MESSAGE,
                String.format(MESSAGE, cause.getNotFoundClassName(), cause.getScannedBasePackage()), cause);
    }

}
