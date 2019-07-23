package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.autodetection.diagnostics.CxfSpringBootMavenPropertiesNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;


public class PackageNameReader {

    protected static final String CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND = "Could not read packageNames from cxf-spring-boot-maven.properties.";

    public static PackageNameReader build() {
        return new PackageNameReader();
    }


    public String readSeiImplementationPackageNameFromCxfSpringBootMavenProperties() throws CxfSpringBootMavenPropertiesNotFoundException {

        return readPackageNameFromCxfSpringBootMavenProperties("sei.implementation.package.name");
    }

    public String readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties() throws CxfSpringBootMavenPropertiesNotFoundException {

        return readPackageNameFromCxfSpringBootMavenProperties("sei.and.webserviceclient.package.name");
    }

    private String readPackageNameFromCxfSpringBootMavenProperties(String seiImplementationPackageNameKey) throws CxfSpringBootMavenPropertiesNotFoundException {
        try {
            Properties cxfSpringBootMavenProperties = new Properties();
            cxfSpringBootMavenProperties.load(cxfSpringBootMavenPropertiesAsInputStream());
            return cxfSpringBootMavenProperties.getProperty(seiImplementationPackageNameKey);
        } catch (IOException ioExc) {
            throw new CxfSpringBootMavenPropertiesNotFoundException(CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND, ioExc);
        }
    }

    private InputStream cxfSpringBootMavenPropertiesAsInputStream() throws IOException {
        return findInClasspath("classpath*:**/" + "cxf-spring-boot-maven.properties").getInputStream();
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
}
