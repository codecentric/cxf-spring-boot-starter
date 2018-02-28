package de.codecentric.cxf.logging;

import de.codecentric.cxf.TestApplication;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

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

    private static final Pattern SLEUTH_LOG_LINE_PATTERN = Pattern.compile("(INFO \\[-,\\w+,\\w+,false].*: Returning a forecast.)");

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Autowired
    private WeatherService weatherServiceClient;

    @Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
    private Resource GetCityForecastByZIPTestXml;

    @Test public void
    sleuth_meta_data_should_be_print_out() throws Exception {
        GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
                GetCityForecastByZIPTestXml.getInputStream(), GetCityForecastByZIP.class);
        weatherServiceClient.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());

        String log = systemOutRule.getLog();
        Matcher foundLoglineMatcher = SLEUTH_LOG_LINE_PATTERN.matcher(log);
        assertThat(foundLoglineMatcher.find(),is(true));
        assertThat(log,containsString("Call time"));
    }

}
