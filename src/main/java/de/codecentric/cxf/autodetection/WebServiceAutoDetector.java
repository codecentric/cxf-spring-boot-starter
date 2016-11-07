package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class WebServiceAutoDetector {

    private static final Logger LOG = LoggerFactory.getLogger(WebServiceAutoDetector.class);

    @SuppressWarnings("unchecked")
    public static <T> T searchAndInstantiateSeiImplementation(Class seiName) throws BootStarterCxfException {

        Class<T> implementingClass = scanForClassWhichImplementsInterface(seiName);
        return instantiateFromClass(implementingClass);
    }

    public static Class searchServiceEndpointInterface() throws BootStarterCxfException {
        return scanForClassWithAnnotation(WebService.class);
    }

    @SuppressWarnings("unchecked")
    public static Service searchAndInstantiateWebServiceClient() throws BootStarterCxfException {
        Class<Service> webServiceClientClass = scanForClassWithAnnotation(WebServiceClient.class);
        return instantiateFromClass(webServiceClientClass);
    }

    private static <T> Class scanForClassWhichImplementsInterface(Class<T> interfaze) throws BootStarterCxfException {
        try {
            String className = scanForClassNameWhichImplements(interfaze);
            LOG.debug("Class found: {}", className);
            return Class.forName(className);

        } catch (ClassNotFoundException exception) {
            throw new BootStarterCxfException("WebServiceClient Class not found", exception);
        }
    }

    private static <T> Class scanForClassWithAnnotation(Class<T> annotationName) throws BootStarterCxfException {
        try {
            String className = scanForClassNameWithAnnotation(annotationName);
            LOG.debug("Class found: {}", className);
            return Class.forName(className);

        } catch (ClassNotFoundException exception) {
            throw new BootStarterCxfException("WebServiceClient Class not found", exception);
        }

    }


    private static <T> String scanForClassNameWithAnnotation(Class<T> annotation) {
        return initScannerAndScan().getNamesOfClassesWithAnnotation(annotation).get(0);
    }

    private static <T> String scanForClassNameWhichImplements(Class<T> interfaze) {
        return initScannerAndScan().getNamesOfClassesImplementing(interfaze).get(0);
    }

    private static ScanResult initScannerAndScan() {
        return new FastClasspathScanner().scan();
    }

    private static <T> T instantiateFromClass(Class<T> clazz) throws BootStarterCxfException {

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



}
