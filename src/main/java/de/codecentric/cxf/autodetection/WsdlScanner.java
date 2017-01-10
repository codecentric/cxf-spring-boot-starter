package de.codecentric.cxf.autodetection;

import com.sun.tools.xjc.api.XJC;
import de.codecentric.cxf.common.BootStarterCxfException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class WsdlScanner {

    private static final String TARGET_NAMESPACE_COULDNT_BE_EXTRACTED = "targetNamespace could not be extracted from WSDL file.";
    private static final String WSDL_FILE_NOT_FOUND = "WSDL file not Found.";

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
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            Resource[] wsdls = resolver.getResources("**/*.wsdl");

            Optional<Resource> wsdl = Arrays.stream(wsdls).findFirst();

            if(wsdl.isPresent()) {
                return wsdl.get();
            } else {
                throw new FileNotFoundException();
            }
        } catch (IOException ioExc) {
            throw new BootStarterCxfException(WSDL_FILE_NOT_FOUND, ioExc);
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
}
