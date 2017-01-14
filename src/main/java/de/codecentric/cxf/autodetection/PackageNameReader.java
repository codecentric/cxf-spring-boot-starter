package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.BootCxfMojo;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;


public class PackageNameReader {

    private static final String CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND = "Could not read packageNames from cxf-spring-boot-maven.properties.";

    public static PackageNameReader ready() {
        return new PackageNameReader();
    }

    public String readSeiImplementationPackageNameFromCxfSpringBootMavenProperties() throws BootStarterCxfException {

        try {
            Properties cxfSpringBootMavenProperties = new Properties();
            cxfSpringBootMavenProperties.load(cxfSpringBootMavenPropertiesAsInputStream());
            return cxfSpringBootMavenProperties.getProperty(BootCxfMojo.SEI_IMPLEMENTATION_PACKAGE_NAME_KEY);
        } catch (IOException ioExc) {
            //TODO: Failure Analyzer for not finding cxf-spring-boot-maven.propterties
            throw new BootStarterCxfException(CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND, ioExc);
        }
    }

    private InputStream cxfSpringBootMavenPropertiesAsInputStream() throws IOException {
        return findInClasspath("classpath*:**/" + BootCxfMojo.CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NAME).getInputStream();
    }

    private Resource findInClasspath(String pattern) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources(pattern);

        Optional<Resource> first = Arrays.stream(resources).findFirst();

        if(first.isPresent()) {
            return first.get();
        } else {
            throw new FileNotFoundException();
        }
    }

    public String readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties() throws BootStarterCxfException {

        try {
            Properties cxfSpringBootMavenProperties = new Properties();
            cxfSpringBootMavenProperties.load(cxfSpringBootMavenPropertiesAsInputStream());
            return cxfSpringBootMavenProperties.getProperty(BootCxfMojo.SEI_AND_WEB_SERVICE_CLIENT_PACKAGE_NAME_KEY);
        } catch (IOException ioExc) {
            //TODO: Failure Analyzer for not finding cxf-spring-boot-maven.propterties
            throw new BootStarterCxfException(CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND, ioExc);
        }
    }
}
