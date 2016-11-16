package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import java.util.List;

public class WebServiceScanner {

    protected static final String NO_CLASS_FOUND = "No class found";

    protected <T> Class scanForClassWhichImplementsAndPickFirst(Class<T> interfaceName) throws BootStarterCxfException {
        List<String> namesOfClassesImplementing = initScannerAndScan().getNamesOfClassesImplementing(interfaceName);
        if (namesOfClassesImplementing.isEmpty()) {
            throw new BootStarterCxfException(WebServiceAutoDetector.NO_CLASS_FOUND);
        }
        return classForName(namesOfClassesImplementing.get(0));
    }

    protected <T> Class scanForClassWithAnnotationAndPickTheFirstOneFound(Class<T> annotationName) throws BootStarterCxfException {
        return classForName(scanForClassNamesWithAnnotation(annotationName).get(0));
    }

    protected <T> List<String> scanForClassNamesWithAnnotation(Class<T> annotationName) throws BootStarterCxfException {
        List<String> namesOfClassesWithAnnotation = initScannerAndScan().getNamesOfClassesWithAnnotation(annotationName);

        if(namesOfClassesWithAnnotation.isEmpty()) {
            throw new BootStarterCxfException(NO_CLASS_FOUND);
        }
        return namesOfClassesWithAnnotation;
    }

    protected  <T> Class scanForClassWithAnnotationAndIsAnInterface(Class<T> annotationName) throws BootStarterCxfException {
        List<String> namesOfClassesWithAnnotation = scanForClassNamesWithAnnotation(annotationName);

        if(namesOfClassesWithAnnotation.size() > 1) {
            return justPickTheClassThatIsAnInterface(namesOfClassesWithAnnotation);
        } else {
            return classForName(namesOfClassesWithAnnotation.get(0));
        }
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

    private ScanResult initScannerAndScan() {
        return new FastClasspathScanner().scan();
    }


    protected Class<?> classForName(String className) throws BootStarterCxfException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            throw new BootStarterCxfException(NO_CLASS_FOUND, exception);
        }
    }
}