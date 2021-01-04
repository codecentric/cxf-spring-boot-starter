package de.codecentric.soap.endpoint;


import de.codecentric.soap.CxfBootSimpleClientApplication;
import de.codecentric.soap.internalmodel.WeatherResponse;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
		classes = CxfBootSimpleClientApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WeatherServiceSoapClientIntegrationTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void callWeatherServiceSoapClient() {
		WeatherResponse weatherResponse =
				when()
					.get("/weather/99423/45/50555")
				.then()
					.statusCode(HttpStatus.SC_OK)
					.extract().body().as(WeatherResponse.class);

		assertEquals(weatherResponse.getCity(),"Weimar");
	}

}
