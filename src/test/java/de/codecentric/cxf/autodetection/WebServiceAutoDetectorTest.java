package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.diagnostics.SeiImplementingClassNotFoundException;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class WebServiceAutoDetectorTest {

    @Mock
    private WebServiceAutoDetector webServiceAutoDetector;

    @Before
    public void setUp() {
        webServiceAutoDetector = Mockito.mock(WebServiceAutoDetector.class);
    }

    @Test
    public void isServiceEndpointInterfaceSuccessfullyDetected() throws BootStarterCxfException {

        Class serviceEndpointInterface = WebServiceAutoDetector.searchServiceEndpointInterface();

        assertThat(serviceEndpointInterface, is(notNullValue()));
        assertThat(serviceEndpointInterface.getSimpleName(), is(equalTo("WeatherService")));
    }

    @Test
    public void isServiceEndpointInterfaceImplementationSuccessfullyFoundAndInstantiated() throws NoSuchFieldException, BootStarterCxfException {
        Class serviceEndpointInterface = WebServiceAutoDetector.searchServiceEndpointInterface();

        WeatherService weatherServiceEndpointImpl = WebServiceAutoDetector.searchAndInstantiateSeiImplementation(serviceEndpointInterface);

        assertThat(weatherServiceEndpointImpl, is(notNullValue()));
        assertThat(weatherServiceEndpointImpl.getClass().getSimpleName(), is(equalTo("TestServiceEndpoint")));
    }

    @Test
    public void isWebServiceClientSuccessfullyFoundAndInstantiated() throws BootStarterCxfException {

        Service webServiceClient = WebServiceAutoDetector.searchAndInstantiateWebServiceClient();

        assertThat(webServiceClient, is(notNullValue()));

        QName serviceNameQName = webServiceClient.getServiceName();
        assertThat(serviceNameQName.getLocalPart(), is(equalTo("Weather")));
    }

    @Test(expected = SeiImplementingClassNotFoundException.class)
    public void shouldReactWithCustomStartupFailureMessagePresentedInConsoleIfSeiImplementingClassIsMissing() throws BootStarterCxfException {
        Class serviceEndpointInterface = WebServiceAutoDetector.searchServiceEndpointInterface();

        SeiImplementingClassNotFoundException seiNotFoundException = new SeiImplementingClassNotFoundException("No SEI implementing class found");
        seiNotFoundException.setNotFoundClassName(serviceEndpointInterface.getName());

        throw seiNotFoundException;


    }

}
