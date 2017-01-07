package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WebServiceScanner {

    protected static final String NO_CLASS_FOUND = "No class found";

    protected <T> Class scanForClassWhichImplementsAndPickFirst(Class<T> interfaceName) throws BootStarterCxfException {
        List<String> namesOfClassesWithInterface = new ArrayList<>();

        Set<BeanDefinition> beans = scanForClasses(new AssignableTypeFilter(interfaceName),"de.codecentric");

        if (beans.isEmpty()) {
            throw new BootStarterCxfException(WebServiceAutoDetector.NO_CLASS_FOUND);
        }

        beans.forEach((BeanDefinition beanDef) -> namesOfClassesWithInterface.add(beanDef.getBeanClassName()));

        return justPickTheClassThatIsNotAnInterface(namesOfClassesWithInterface);
    }

    protected Class justPickTheClassThatIsNotAnInterface(List<String> namesOfClasses) throws BootStarterCxfException {
        for (String className : namesOfClasses) {
            if (!isInterface(className)) {
                return classForName(className);
            }
        }
        throw new BootStarterCxfException(NO_CLASS_FOUND);
    }

    private Set<BeanDefinition> scanForClasses(TypeFilter typeFilter, String basePackage) {
        ClassPathScanningCandidateComponentProvider scanningProvider = new InterfaceIncludingClassPathScanningCandidateComponentProvider();
        scanningProvider.addIncludeFilter(typeFilter);
        return scanningProvider.findCandidateComponents(basePackage);
    }

    protected <T extends Annotation> Class scanForClassWithAnnotationAndPickTheFirstOneFound(Class<T> annotationName) throws BootStarterCxfException {
        return classForName(scanForClassNamesWithAnnotation(annotationName).get(0));
    }

    protected <T extends Annotation> List<String> scanForClassNamesWithAnnotation(Class<T> annotation) throws BootStarterCxfException {
        List<String> namesOfClassesWithAnnotation = new ArrayList<>();

        Set<BeanDefinition> beans = scanForClasses(new AnnotationTypeFilter(annotation), "de.codecentric");

        if(beans.isEmpty()) {
            throw new BootStarterCxfException(NO_CLASS_FOUND);
        }

        beans.stream().forEach((BeanDefinition bean) -> namesOfClassesWithAnnotation.add(bean.getBeanClassName()));
        return namesOfClassesWithAnnotation;
    }

    protected  <T extends Annotation> Class scanForClassWithAnnotationAndIsAnInterface(Class<T> annotationName) throws BootStarterCxfException {
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

    protected Class<?> classForName(String className) throws BootStarterCxfException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            throw new BootStarterCxfException(NO_CLASS_FOUND, exception);
        }
    }

}