package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.autodetection.WebServiceAutoDetector;
import de.codecentric.cxf.common.BootStarterCxfException;

public class SeiImplementationMissingFailureAnalyzerHelper {

    protected static SeiImplementingClassNotFoundException constructException() throws BootStarterCxfException {
        WebServiceAutoDetector webServiceAutoDetector = new WebServiceAutoDetector();

        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();

        SeiImplementingClassNotFoundException seiNotFoundException = new SeiImplementingClassNotFoundException("No SEI implementing class found");
        seiNotFoundException.setNotFoundClassName(serviceEndpointInterface.getName());
        return seiNotFoundException;
    }
}