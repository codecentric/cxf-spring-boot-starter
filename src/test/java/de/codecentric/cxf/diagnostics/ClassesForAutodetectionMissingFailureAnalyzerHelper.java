package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.autodetection.WebServiceAutoDetector;
import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.namespace.weatherservice.WeatherService;

public class ClassesForAutodetectionMissingFailureAnalyzerHelper {

    protected static SeiImplClassNotFoundException mockSeiImplClassNotFoundException() throws BootStarterCxfException {
        Class serviceEndpointInterface = WeatherService.class;

        SeiImplClassNotFoundException seiImplNotFoundException = new SeiImplClassNotFoundException("No SEI implementing class found");
        seiImplNotFoundException.setNotFoundClassName(serviceEndpointInterface.getName());
        return seiImplNotFoundException;
    }

    protected static WebServiceClientNotFoundException mockWebServiceClientNotFoundException() throws BootStarterCxfException {
        return new WebServiceClientNotFoundException("There was no class found, that´s annotated with javax.xml.ws.WebServiceClient and implements javax.xml.ws.Service");
    }
}