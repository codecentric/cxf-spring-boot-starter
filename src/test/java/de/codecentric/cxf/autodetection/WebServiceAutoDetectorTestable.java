package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.namespace.weatherservice.Weather;
import de.codecentric.namespace.weatherservice.WeatherService;

import javax.jws.WebService;
import java.util.List;

public class WebServiceAutoDetectorTestable extends WebServiceAutoDetector {

    @Override
    protected Class<?> classForName(String className) throws BootStarterCxfException {
        if("WeatherService".equals(className)) {
            return WeatherService.class;
        } else {
            return super.classForName(className);
        }
    }

    @Override
    protected boolean isInterface(String className) throws BootStarterCxfException {
        if("WeatherService".equals(className)) {
            return true;
        } else if ("TestServiceEndpoint".equals(className)) {
            return false;
        } else {
            return super.isInterface(className);
        }
    }

    @Override
    protected <T> List<String> scanForClassNamesWithAnnotation(Class<T> annotationName) throws BootStarterCxfException {
        if(WebService.class.equals(annotationName)) {
            return WebServiceAutoDetectorTest.generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation();
        } else {
            return super.scanForClassNamesWithAnnotation(annotationName);
        }
    }
}
