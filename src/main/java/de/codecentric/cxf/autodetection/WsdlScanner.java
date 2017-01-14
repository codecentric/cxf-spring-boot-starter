package de.codecentric.cxf.autodetection;

import com.sun.tools.xjc.api.XJC;
import de.codecentric.cxf.BootCxfMojo;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class WsdlScanner {

    private static final String TARGET_NAMESPACE_COULDNT_BE_EXTRACTED = "targetNamespace could not be extracted from WSDL file.";
    private static final String WSDL_FILE_NOT_FOUND = "WSDL file not Found.";
    private static final String CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND = "cxf-spring-boot-maven.properties file not found.";
    private static final String MANIFEST_FILE_NOT_FOUND = "MANIFEST.MF not Found.";
    private static final String MANIFEST_NOT_CONTAINING_PACKAGENAME = "MANIFEST.MF doesn´ contain packageName.";

    // (?<=targetNamespace=")[:./a-zA-Z]+(?=")
    private static final String REGEX_FIND_TARGET_NAMESPACE_CONTENT = "(?<=targetNamespace=\")[:./a-zA-Z]+(?=\")";

    public static WsdlScanner ready() {
        return new WsdlScanner();
    }


    public File findWsdl(String pathInClasspath) throws IOException {

        return ResourceUtils.getFile("classpath:" + pathInClasspath);
    }

    public Resource findWsdl() throws BootStarterCxfException {

        try {
            return findInClasspath("classpath*:**/*.wsdl");
        } catch (IOException ioExc) {
            throw new BootStarterCxfException(WSDL_FILE_NOT_FOUND, ioExc);
        }
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

    public String readTargetNamespaceFromWsdl() throws BootStarterCxfException {
        Resource wsdl = findWsdl();

        try {
            Matcher matcher = buildMatcher(readResourceIntoString(wsdl), REGEX_FIND_TARGET_NAMESPACE_CONTENT);

            if (matcher.find()) {
                return matcher.group(0);
            } else {
                throw new BootStarterCxfException(TARGET_NAMESPACE_COULDNT_BE_EXTRACTED);
            }
        } catch (IOException ioExc) {
            throw new BootStarterCxfException(TARGET_NAMESPACE_COULDNT_BE_EXTRACTED, ioExc);
        }
    }

    private String readResourceIntoString(Resource wsdl) throws IOException {
        return Files.lines(Paths.get(wsdl.getURI())).collect(Collectors.joining());
    }

    private static Matcher buildMatcher(String string2SearchIn, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string2SearchIn);
    }

    public String generatePackageNameFromTargetNamespaceInWsdl() throws BootStarterCxfException {
        String targetNamespaceFromWsdl = readTargetNamespaceFromWsdl();
        return XJC.getDefaultPackageName(targetNamespaceFromWsdl);
    }

    public String readPackageNameFromCxfSpringBootMavenProperties() throws BootStarterCxfException {

        try {
            Properties cxfSpringBootMavenProperties = new Properties();
            cxfSpringBootMavenProperties.load(cxfSpringBootMavenPropertiesAsInputStream());
            return cxfSpringBootMavenProperties.getProperty(BootCxfMojo.PACKAGE_NAME_KEY);
        } catch (IOException ioExc) {
            //TODO: Failure Analyzer for not finding cxf-spring-boot-maven.propterties
            throw new BootStarterCxfException(CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NOT_FOUND, ioExc);
        }
    }

    private InputStream cxfSpringBootMavenPropertiesAsInputStream() throws IOException {
        return findInClasspath("classpath*:**/" + BootCxfMojo.CXF_SPRING_BOOT_MAVEN_PROPERTIES_FILE_NAME).getInputStream();
    }
}
