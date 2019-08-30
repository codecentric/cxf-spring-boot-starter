package de.codecentric.soap.transformation;

import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.soap.internalmodel.WeatherResponse;

public class GetCityForecastByZIPInMapper {
    public static WeatherResponse mapForecastResponseToWeatherResponse(ForecastReturn forecastReturn) {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCity(forecastReturn.getCity());
        return weatherResponse;
    }
}
