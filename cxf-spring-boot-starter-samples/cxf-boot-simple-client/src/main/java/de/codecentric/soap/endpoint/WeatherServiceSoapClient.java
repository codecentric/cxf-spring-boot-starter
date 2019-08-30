package de.codecentric.soap.endpoint;

import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.soap.internalmodel.WeatherRequest;
import de.codecentric.soap.internalmodel.WeatherResponse;
import de.codecentric.soap.transformation.GetCityForecastByZIPInMapper;
import de.codecentric.soap.transformation.GetCityForecastByZIPOutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherServiceSoapClient {


    private static final Logger LOG = LoggerFactory.getLogger(WeatherServiceSoapClient.class);

    @Autowired
    private WeatherService weatherServiceClient;

    @RequestMapping(path = "/{zipcode}/{seniority}/{cash}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public WeatherResponse getCityForecastByZIP (@PathVariable("zipcode") String zipcode, @PathVariable("seniority") int seniority, @PathVariable("cash") int cash) throws WeatherException {

        LOG.info("GET called on /weather/" + zipcode + "/" + seniority + "/" + cash);

        WeatherRequest weatherRequest = new WeatherRequest();
        weatherRequest.setZipcode(zipcode);
        weatherRequest.setUserSeniority(seniority);
        weatherRequest.setCash(cash);

        LOG.info("Calling SOAP service with URL: '" + clientUrl + "'");

        ForecastReturn forecastReturn = weatherServiceClient.getCityForecastByZIP(GetCityForecastByZIPOutMapper.mapWeatherRequestToForecastRequest(weatherRequest));

        LOG.info("Successfully called SOAP service!");

        return GetCityForecastByZIPInMapper.mapForecastResponseToWeatherResponse(forecastReturn);
    }


    @Value("${webservice.client.url}")
    private String clientUrl;

}
