package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.TestServiceEndpoint;
import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.diagnostics.SeiImplementingClassNotFoundException;
import de.codecentric.namespace.weatherservice.Weather;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class WebServiceAutoDetectorTest {

    private WebServiceAutoDetector webServiceAutoDetector;
    private WebServiceAutoDetectorTestable webServiceAutoDetectorTestable;

    @Before
    public void setUp() {
        webServiceAutoDetector = new WebServiceAutoDetector();
        webServiceAutoDetectorTestable = new WebServiceAutoDetectorTestable();
    }

    @Test public void
    is_SEI_Successfully_detected() throws BootStarterCxfException {

        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();

        assertThat(serviceEndpointInterface, is(notNullValue()));
        assertThat(serviceEndpointInterface.getSimpleName(), is(equalTo("WeatherService")));
    }

    @Test public void
    is_SEI_successfully_detected_when_the_SEI_implementing_class_also_has_the_same_WebService_Annotation() throws BootStarterCxfException {

        Class weather = null;

        try {
            weather = webServiceAutoDetectorTestable.scanForClassWithAnnotationAndIsAnInterface(WebService.class);
        } catch (BootStarterCxfException exception) {
            fail("Interface should have been found!");
        }

        assertThat(weather, is(equalTo(WeatherService.class)));
    }

    protected static List<String> generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation() {
        Class sei = WeatherService.class;
        Class endpointImplementation = TestServiceEndpoint.class;
        return Arrays.asList(sei.getSimpleName(), endpointImplementation.getSimpleName());
    }

    @Test public void
    picks_the_interface_from_two_classes_where_one_is_an_interface_and_the_other_not() throws BootStarterCxfException, ClassNotFoundException {
        List<String> twoWebServices = generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation();

        Class interfaze = webServiceAutoDetectorTestable.justPickTheClassThatIsAnInterface(twoWebServices);

        assertThat(interfaze, is(notNullValue()));
        assertThat(interfaze.isInterface(), is(true));
    }

    @Test public void
    is_SEI_Implementation_successfully_found_and_instantiated() throws NoSuchFieldException, BootStarterCxfException {
        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();

        WeatherService weatherServiceEndpointImpl = webServiceAutoDetector.searchAndInstantiateSeiImplementation(serviceEndpointInterface);

        assertThat(weatherServiceEndpointImpl, is(notNullValue()));
        assertThat(weatherServiceEndpointImpl.getClass().getSimpleName(), is(equalTo("TestServiceEndpoint")));
    }

    @Test public void
    is_WebServiceClient_successfully_found_and_instantiated() throws BootStarterCxfException {

        Service webServiceClient = webServiceAutoDetector.searchAndInstantiateWebServiceClient();

        assertThat(webServiceClient, is(notNullValue()));

        QName serviceNameQName = webServiceClient.getServiceName();
        assertThat(serviceNameQName.getLocalPart(), is(equalTo("Weather")));
    }

    @Test(expected = SeiImplementingClassNotFoundException.class) public void
    should_react_with_custom_startup_Failure_Message_presented_in_console_if_SEI_implementing_class_is_missing() throws BootStarterCxfException {
        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();

        SeiImplementingClassNotFoundException seiNotFoundException = new SeiImplementingClassNotFoundException("No SEI implementing class found");
        seiNotFoundException.setNotFoundClassName(serviceEndpointInterface.getName());

        throw seiNotFoundException;


    }

}
