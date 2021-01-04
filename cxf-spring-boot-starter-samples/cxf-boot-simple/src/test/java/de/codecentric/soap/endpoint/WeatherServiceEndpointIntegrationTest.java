package de.codecentric.soap.endpoint;


import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import de.codecentric.soap.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(
		classes = TestApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = { "server.port:8092" }
)
public class WeatherServiceEndpointIntegrationTest {
	
	@Autowired
	private WeatherService weatherServiceClient;

	@Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
	private Resource GetCityForecastByZIPTestXml;
	
	@Test
	public void getCityForecastByZIP() throws WeatherException, BootStarterCxfException, IOException {
		// Given
		GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
				GetCityForecastByZIPTestXml.getInputStream(), GetCityForecastByZIP.class);
		
		// When
		ForecastReturn forecastReturn = weatherServiceClient.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
}
