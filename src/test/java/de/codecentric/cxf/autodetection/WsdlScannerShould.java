package de.codecentric.cxf.autodetection;


import de.codecentric.cxf.common.BootStarterCxfException;
import org.apache.maven.shared.invoker.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
public class WsdlScannerShould {

    @Value("classpath:wsdl/Weather1.0.wsdl")
    private Resource weatherServiceWsdl;

    private WsdlScanner wsdlScanner = new WsdlScanner();

    @Test public void
    find_Wsdl_in_classpath_with_given_path() throws IOException {

        File wsdl = wsdlScanner.findWsdl("wsdl/Weather1.0.wsdl");
        assertThat(wsdl.getName(), is(equalTo(weatherServiceWsdl.getFile().getName())));
    }

    @Test public void
    find_Wsdl_in_classpath() throws IOException, BootStarterCxfException {
        File wsdl = wsdlScanner.findWsdl();
        assertThat(wsdl.getName(), is(equalTo(weatherServiceWsdl.getFile().getName())));
    }


}
