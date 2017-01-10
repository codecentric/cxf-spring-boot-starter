package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


public class WsdlScanner {

    public File findWsdl(String pathInClasspath) throws IOException {

        return ResourceUtils.getFile("classpath:" + pathInClasspath);
    }

    public File findWsdl() throws BootStarterCxfException {

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            Resource[] wsdls = resolver.getResources("**/*.wsdl");

            Optional<Resource> wsdl = Arrays.stream(wsdls).findFirst();

            if(wsdl.isPresent()) {
                return wsdl.get().getFile();
            } else {
                throw new FileNotFoundException();
            }
        } catch (IOException e) {
            throw new BootStarterCxfException("WSDL file not Found.", e);
        }

    }
}
