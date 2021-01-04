package de.codecentric.cxf.soaprawclient;

import de.codecentric.cxf.TestApplication;
import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.configuration.CxfAutoConfiguration;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes=TestApplication.class)
public class SoapRawClientTest {

    @Autowired
    private CxfAutoConfiguration cxfAutoConfiguration;

    @Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
    private Resource GetCityForecastByZIPTestXml;

    @Test
    public void callSoapServiceFails() throws Exception {
        String falseSoapServiceUrl = "http://foobar:8087" + cxfAutoConfiguration.baseAndServiceEndingUrl();
        SoapRawClient soapRawClient = new SoapRawClient(falseSoapServiceUrl, WeatherService.class);
        try {
            soapRawClient.callSoapService(GetCityForecastByZIPTestXml.getInputStream());
            fail();
        } catch (BootStarterCxfException bootStarterException) {

            assertThat(bootStarterException.getMessage(), containsString(SoapRawClient.ERROR_MESSAGE));

            Throwable unknownHostException = bootStarterException.getCause();
            assertThat(unknownHostException.getClass(), is(typeCompatibleWith(IOException.class)));
            assertThat(unknownHostException.getMessage(), containsString("foobar"));
        }
    }
}