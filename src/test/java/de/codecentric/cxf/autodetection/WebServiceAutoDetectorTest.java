package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(SpringRunner.class)
public class WebServiceAutoDetectorTest {

    @Test
    public void isWebServiceSuccessfullyDetected() throws BootStarterCxfException {

        String interfaceName = WebServiceAutoDetector.searchServiceEndpointInterfaceName();

        assertThat(interfaceName, is(equalTo("WeatherService")));
    }

    @Test
    public void isWebServiceClientSuccessfullyDetected() throws BootStarterCxfException {

        Service webServiceClient = WebServiceAutoDetector.searchAndInstantiateWebServiceClient();

        assertThat(webServiceClient, is(notNullValue()));

        QName serviceNameQName = webServiceClient.getServiceName();
        assertThat(serviceNameQName.getLocalPart(), is(equalTo("Weather")));
    }
}
