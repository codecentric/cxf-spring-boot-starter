package de.codecentric.soap.endpoint;


import de.codecentric.soap.CxfBootSimpleClientApplication;
import de.codecentric.soap.internalmodel.WeatherResponse;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = CxfBootSimpleClientApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WeatherServiceSoapClientIntegrationTest {

	@LocalServerPort
	private int port;

	@Before
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

		assertThat(weatherResponse.getCity(), is("Weimar"));
	}

}
