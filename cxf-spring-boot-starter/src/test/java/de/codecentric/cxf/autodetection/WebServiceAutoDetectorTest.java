package de.codecentric.cxf.autodetection;

import de.codecentric.cxf.TestServiceEndpoint;
import de.codecentric.cxf.autodetection.diagnostics.SeiImplClassNotFoundException;
import de.codecentric.cxf.autodetection.diagnostics.SeiNotFoundException;
import de.codecentric.cxf.autodetection.diagnostics.WebServiceClientNotFoundException;
import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.namespace.weatherservice.Weather;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.Arrays;
import java.util.List;

import static de.codecentric.cxf.autodetection.WebServiceAutoDetector.SEI_ANNOTATION;
import static de.codecentric.cxf.autodetection.WebServiceAutoDetector.WEB_SERVICE_CLIENT_ANNOTATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class WebServiceAutoDetectorTest {

    public static final Class WEATHER_SERVICE_ENDPOINT_INTERFACE = WeatherService.class;
    public static final Class WEATHER_WEBSERVICE_CLIENT = Weather.class;
    public static final Class WEATHER_SEI_IMPLEMENTING_CLASS = TestServiceEndpoint.class;

    private final BootStarterCxfException STARTER_EXCEPTION_NO_CLASS_FOUND = new BootStarterCxfException(WebServiceScanner.NO_CLASS_FOUND);

    private String seiAndWebServiceClientPackageName;
    private String seiImplementationPackageName;

    @BeforeEach
    public void init() throws BootStarterCxfException {
        seiAndWebServiceClientPackageName = PackageNameReader.build().readSeiAndWebServiceClientPackageNameFromCxfSpringBootMavenProperties();
        seiImplementationPackageName = PackageNameReader.build().readSeiImplementationPackageNameFromCxfSpringBootMavenProperties();
    }

    @Test
    public void is_SEI_Successfully_detected() throws BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassWithAnnotationAndIsAnInterface(SEI_ANNOTATION, seiAndWebServiceClientPackageName)).thenReturn(WEATHER_SERVICE_ENDPOINT_INTERFACE);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        WebServiceAutoDetector webServiceAutoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        Class serviceEndpointInterface = webServiceAutoDetector.searchServiceEndpointInterface();

        assertNotNull(serviceEndpointInterface);
        assertEquals(serviceEndpointInterface.getSimpleName(), WEATHER_SERVICE_ENDPOINT_INTERFACE.getSimpleName());
    }

    @Test public void is_SEI_Implementation_successfully_found_and_instantiated() throws NoSuchFieldException, BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassWhichImplementsAndPickFirst(WEATHER_SERVICE_ENDPOINT_INTERFACE, seiImplementationPackageName)).thenReturn(WEATHER_SEI_IMPLEMENTING_CLASS);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
        when(beanFactory.createBean(TestServiceEndpoint.class)).thenReturn(new TestServiceEndpoint(null));
        WebServiceAutoDetector autoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        WeatherService weatherServiceEndpointImpl = autoDetector.searchAndInstantiateSeiImplementation(WEATHER_SERVICE_ENDPOINT_INTERFACE);

        assertNotNull(weatherServiceEndpointImpl);
        assertEquals(weatherServiceEndpointImpl.getClass().getSimpleName(),WEATHER_SEI_IMPLEMENTING_CLASS.getSimpleName());
    }

    @Test public void is_WebServiceClient_successfully_found_and_instantiated() throws BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassWithAnnotationAndPickTheFirstOneFound(WEB_SERVICE_CLIENT_ANNOTATION, seiAndWebServiceClientPackageName)).thenReturn(WEATHER_WEBSERVICE_CLIENT);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        AutowireCapableBeanFactory beanFactory = mock(AutowireCapableBeanFactory.class);
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
        when(beanFactory.createBean(Weather.class)).thenReturn(new Weather());
        WebServiceAutoDetector autoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        Service webServiceClient = autoDetector.searchAndInstantiateWebServiceClient();

        assertNotNull(webServiceClient);

        QName serviceNameQName = webServiceClient.getServiceName();
        assertEquals(serviceNameQName.getLocalPart(), WEATHER_WEBSERVICE_CLIENT.getSimpleName());
    }

    @Test
    public void should_react_with_custom_startup_Failure_Message_console_if_SEI_implementing_class_is_missing() throws BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassWhichImplementsAndPickFirst(WEATHER_SERVICE_ENDPOINT_INTERFACE, seiImplementationPackageName)).thenThrow(STARTER_EXCEPTION_NO_CLASS_FOUND);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        WebServiceAutoDetector autoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        assertThrows(SeiImplClassNotFoundException.class, () -> autoDetector.searchAndInstantiateSeiImplementation(WEATHER_SERVICE_ENDPOINT_INTERFACE));
    }

    @Test
    public void should_react_with_custom_startup_Failure_Message_if_WebServiceClient_annotated_class_is_missing() throws BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassWithAnnotationAndPickTheFirstOneFound(WEB_SERVICE_CLIENT_ANNOTATION, seiAndWebServiceClientPackageName)).thenThrow(STARTER_EXCEPTION_NO_CLASS_FOUND);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        WebServiceAutoDetector autoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        assertThrows(WebServiceClientNotFoundException.class, () -> autoDetector.searchAndInstantiateWebServiceClient());
    }

    @Test
    public void should_react_with_custom_startup_Failure_Message_if_SEI_is_missing() throws BootStarterCxfException {

        WebServiceScanner scannerMock = mock(WebServiceScanner.class);
        when(scannerMock.scanForClassNamesWithAnnotation(SEI_ANNOTATION, seiAndWebServiceClientPackageName)).thenThrow(STARTER_EXCEPTION_NO_CLASS_FOUND);
        when(scannerMock.scanForClassWithAnnotationAndIsAnInterface(SEI_ANNOTATION, seiAndWebServiceClientPackageName)).thenCallRealMethod();
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        WebServiceAutoDetector autoDetector = new WebServiceAutoDetector(scannerMock, applicationContext);

        assertThrows(SeiNotFoundException.class, () -> autoDetector.searchServiceEndpointInterface());
    }

    protected static List<String> generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation() {
        return Arrays.asList(WEATHER_SERVICE_ENDPOINT_INTERFACE.getName(), WEATHER_SEI_IMPLEMENTING_CLASS.getName());
    }

}
