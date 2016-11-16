package de.codecentric.cxf.autodetection;


import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.Test;

import javax.jws.WebService;
import java.util.List;

import static de.codecentric.cxf.autodetection.WebServiceAutoDetectorTest.generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class WebServiceScannerTest {

    private WebServiceScanner webServiceScanner = new WebServiceScanner();

    @Test public void
    is_SEI_successfully_detected_when_the_SEI_implementing_class_also_has_the_same_WebService_Annotation() throws BootStarterCxfException {

        Class weather = null;

        try {
            weather = webServiceScanner.scanForClassWithAnnotationAndIsAnInterface(WebService.class);
        } catch (BootStarterCxfException exception) {
            fail("Interface should have been found!");
        }

        assertThat(weather, is(equalTo(WeatherService.class)));
    }

    @Test public void
    picks_the_interface_from_two_classes_where_one_is_an_interface_and_the_other_not() throws BootStarterCxfException, ClassNotFoundException {
        List<String> twoWebServices = generateListWithSeiAndSeiImplNameWithBothWebServiceAnnotation();

        Class interfaze = webServiceScanner.justPickTheClassThatIsAnInterface(twoWebServices);

        assertThat(interfaze, is(notNullValue()));
        assertThat(interfaze.isInterface(), is(true));
    }


}
