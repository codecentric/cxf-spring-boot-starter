package de.codecentric.cxf.endpoint;

import de.codecentric.cxf.TestApplication;
import de.codecentric.cxf.soaprawclient.SoapRawClient;
import org.apache.http.client.fluent.Request;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes=TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = { "server.port:8088"}
        )
public class WeatherServiceRelativeUrlTest {

    @Autowired
    private SoapRawClient soapRawClient;

    @Test
    public void should_return_a_relative_url() throws Exception {
        final String response = Request
                .Get("http://localhost:8088/soap-api/Weather?wsdl")
                .execute()
                .returnContent()
                .asString();
        assertThat(response, Matchers.containsString("<s:include schemaLocation=\"Weather?xsd=Weather1.0.xsd\"/>"));
    }
}
