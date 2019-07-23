package de.codecentric.cxf.autodetection;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

/**
 * Because org.springframework.core.type.classreading.ClassMetadataReadingVisitor.isConcrete() does exclude Interfaces from beeing returned,
 * we have to override isCandidateComponent(AnnotatedBeanDefinition beanDefinition) to return true in every case.
 * Now our Interface is also returned.
 */
class InterfaceIncludingClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {
    public InterfaceIncludingClassPathScanningCandidateComponentProvider() {
        // false => Don't use default filters (@Component, etc.)
        super(false);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }
}
