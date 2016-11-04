package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class WebServiceAutoDetector {

    public static Service searchWebServiceClient() throws BootStarterCxfException {

        try {
            Class webServiceClientClass = scanForClassWithExtendJavaxXmlWsService();

            Constructor<?> constructor = webServiceClientClass.getConstructor();

            Object webServiceClient = constructor.newInstance();

            return (Service) webServiceClient;

        } catch (ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InstantiationException |
                InvocationTargetException exception) {
            throw new BootStarterCxfException("WebServiceClient Class not found", exception);
        }

    }

    private static Class scanForClassWithExtendJavaxXmlWsService() throws ClassNotFoundException {
        // see https://github.com/lukehutch/fast-classpath-scanner/wiki/1.-Usage#mechanism-2
        FastClasspathScanner fastClasspathScanner = new FastClasspathScanner();

        ScanResult scanResult = fastClasspathScanner.scan();

        List<String> namesOfClassesWithAnnotation = scanResult.getNamesOfClassesWithAnnotation(WebServiceClient.class);

        String webServiceClientClassName = namesOfClassesWithAnnotation.get(0);
        System.out.println("Class found: " + webServiceClientClassName);

        return Class.forName(webServiceClientClassName);
    }
}
