package de.codecentric.cxf.logging;

import de.codecentric.cxf.TestApplication;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port:8087"})
public class SleuthLogMessagesTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Autowired
    private WeatherService weatherServiceClient;

    @Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
    private Resource GetCityForecastByZIPTestXml;

    @Test public void
    sleuth_meta_data_should_be_print_out() throws Exception {
        callSoapService();

        String log = systemOutRule.getLog();
        assertThat(isSleuthLogLineSomewhereIn(log),is(true));
    }

    private boolean isSleuthLogLineSomewhereIn(String log) {
        Pattern sleuth_log_line_pattern = Pattern.compile("(INFO \\[-,\\w+,\\w+,false].*: Returning a forecast.)");
        Matcher loglineMatcher = sleuth_log_line_pattern.matcher(log);
        return loglineMatcher.find();
    }

    @Ignore
    @Test public void
    call_time_should_be_print_out() throws Exception {
        callSoapService();

        String log = systemOutRule.getLog();
        assertThat(log,containsString("Call time"));
    }

    private void callSoapService() throws de.codecentric.cxf.common.BootStarterCxfException, IOException, WeatherException {
        GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
                GetCityForecastByZIPTestXml.getInputStream(), GetCityForecastByZIP.class);

        weatherServiceClient.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
    }

}
