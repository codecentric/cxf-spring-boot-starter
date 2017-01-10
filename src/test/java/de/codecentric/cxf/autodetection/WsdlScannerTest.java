package de.codecentric.cxf.autodetection;


import de.codecentric.cxf.common.BootStarterCxfException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class WsdlScannerTest {

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
        File wsdl = wsdlScanner.findWsdl().getFile();
        assertThat(wsdl.getName(), is(equalTo(weatherServiceWsdl.getFile().getName())));
    }

    @Test public void
    read_target_namespace_from_Wsdl() throws BootStarterCxfException, IOException {
        String targetNamespace = wsdlScanner.readTargetNamespaceFromWsdl();

        assertThat(targetNamespace, is(equalTo("http://www.codecentric.de/namespace/weatherservice/")));
    }

}
