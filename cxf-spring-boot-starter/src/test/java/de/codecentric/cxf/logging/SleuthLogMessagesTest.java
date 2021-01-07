package de.codecentric.cxf.logging;

import de.codecentric.cxf.TestApplication;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.stream.SystemOut;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SystemStubsExtension.class)
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port:8087"})
public class SleuthLogMessagesTest {

    @SystemStub
    private SystemOut systemOut;

    @Autowired
    private WeatherService weatherServiceClient;

    @Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
    private Resource GetCityForecastByZIPTestXml;

    @Test
    public void sleuth_meta_data_should_be_print_out() throws Exception {
        callSoapService();

        assertTrue(isSleuthLogLineSomewhereIn(systemOut.getText()));
    }

    private boolean isSleuthLogLineSomewhereIn(String log) {
        Pattern sleuth_log_line_pattern = Pattern.compile("(INFO \\[,\\w+,\\w+.*: Returning a forecast.)");
        Matcher loglineMatcher = sleuth_log_line_pattern.matcher(log);
        return loglineMatcher.find();
    }

    @Disabled
    @Test public void
    call_time_should_be_print_out() throws Exception {
        callSoapService();

        assertThat(systemOut.getText(), containsString("Call time"));
    }

    private void callSoapService() throws de.codecentric.cxf.common.BootStarterCxfException, IOException, WeatherException {
        GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
                GetCityForecastByZIPTestXml.getInputStream(), GetCityForecastByZIP.class);

        weatherServiceClient.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
    }

}
