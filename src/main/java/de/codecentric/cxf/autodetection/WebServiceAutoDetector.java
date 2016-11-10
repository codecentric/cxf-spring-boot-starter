package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.diagnostics.SeiImplementingClassNotFoundException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class WebServiceAutoDetector {

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceAutoDetector.class);
    private static final String NO_CLASS_FOUND = "No class found";

    @SuppressWarnings("unchecked")
    public <T> T searchAndInstantiateSeiImplementation(Class seiName) throws BootStarterCxfException {
        Class<T> implementingClass = null;
        try {
            implementingClass = scanForClassWhichImplementsAndPickFirst(seiName);
            LOG.debug("Found SEI implementing class: '{}'", implementingClass.getName());
        } catch (BootStarterCxfException exception) {
            SeiImplementingClassNotFoundException seiNotFound = new SeiImplementingClassNotFoundException("No SEI implementing class found");
            seiNotFound.setNotFoundClassName(seiName.getName());
            throw seiNotFound;
        }
        return instantiateFromClass(implementingClass);
    }

    public Class searchServiceEndpointInterface() throws BootStarterCxfException {
        Class sei = scanForClassWithAnnotationAndIsAnInterface(WebService.class);
        LOG.debug("Found Service Endpoint Interface (SEI): '{}'", sei.getName());
        return sei;
    }

    @SuppressWarnings("unchecked")
    public Service searchAndInstantiateWebServiceClient() throws BootStarterCxfException {
        Class<Service> webServiceClientClass = scanForClassWithAnnotationAndPickTheFirstOneFound(WebServiceClient.class);
        LOG.debug("Found WebServiceClient class: '{}'", webServiceClientClass.getName());
        return instantiateFromClass(webServiceClientClass);
    }

    private <T> Class scanForClassWithAnnotationAndPickTheFirstOneFound(Class<T> annotationName) throws BootStarterCxfException {
        return classForName(scanForClassNamesWithAnnotation(annotationName).get(0));
    }

    protected  <T> Class scanForClassWithAnnotationAndIsAnInterface(Class<T> annotationName) throws BootStarterCxfException {
        List<String> namesOfClassesWithAnnotation = scanForClassNamesWithAnnotation(annotationName);

        if(namesOfClassesWithAnnotation.size() > 1) {
            return justPickTheClassThatIsAnInterface(namesOfClassesWithAnnotation);
        } else {
            return classForName(namesOfClassesWithAnnotation.get(0));
        }
    }

    protected <T> List<String> scanForClassNamesWithAnnotation(Class<T> annotationName) throws BootStarterCxfException {
        List<String> namesOfClassesWithAnnotation = initScannerAndScan().getNamesOfClassesWithAnnotation(annotationName);

        if(namesOfClassesWithAnnotation.isEmpty()) {
            throw new BootStarterCxfException(NO_CLASS_FOUND);
        }
        return namesOfClassesWithAnnotation;
    }

    protected Class justPickTheClassThatIsAnInterface(List<String> namesOfClassesWithAnnotation) throws BootStarterCxfException {
        for (String className : namesOfClassesWithAnnotation) {
            if (isInterface(className)) {
                return classForName(className);
            }
        }
        throw new BootStarterCxfException(NO_CLASS_FOUND);
    }

    protected boolean isInterface(String className) throws BootStarterCxfException {
        return classForName(className).isInterface();
    }

    private <T> Class scanForClassWhichImplementsAndPickFirst(Class<T> interfaceName) throws BootStarterCxfException {
        List<String> namesOfClassesImplementing = initScannerAndScan().getNamesOfClassesImplementing(interfaceName);
        if(namesOfClassesImplementing.isEmpty()) {
            throw new BootStarterCxfException(NO_CLASS_FOUND);
        }
        return classForName(namesOfClassesImplementing.get(0));
    }

    private ScanResult initScannerAndScan() {
        return new FastClasspathScanner().scan();
    }

    private <T> T instantiateFromClass(Class<T> clazz) throws BootStarterCxfException {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();

        } catch (NoSuchMethodException |
                IllegalAccessException |
                InstantiationException |
                InvocationTargetException exception) {
            throw new BootStarterCxfException("Class couldn´t be instantiated", exception);
        }
    }

    protected Class<?> classForName(String className) throws BootStarterCxfException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            throw new BootStarterCxfException(NO_CLASS_FOUND, exception);
        }
    }

}
