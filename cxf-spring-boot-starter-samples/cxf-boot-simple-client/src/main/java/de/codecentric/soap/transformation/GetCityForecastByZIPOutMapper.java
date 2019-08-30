package de.codecentric.soap.transformation;

import de.codecentric.namespace.weatherservice.datatypes.ProductName;
import de.codecentric.namespace.weatherservice.general.ForecastCustomer;
import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.soap.internalmodel.WeatherRequest;

public final class GetCityForecastByZIPOutMapper {

	// private Constructor for Utility-Class
	private GetCityForecastByZIPOutMapper() {};

	public static ForecastRequest mapWeatherRequestToForecastRequest(WeatherRequest weatherRequest) {
	
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP(weatherRequest.getZipcode());
		forecastRequest.setProductName(ProductName.FORECAST_BASIC);
		forecastRequest.setFlagcolor("blue");
		ForecastCustomer forecastCustomer = new ForecastCustomer();
		forecastCustomer.setAge(weatherRequest.getUserSeniority());
		forecastCustomer.setContribution(weatherRequest.getCash());
		forecastCustomer.setMethodOfPayment("PayPal");
		forecastRequest.setForecastCustomer(forecastCustomer);
		return forecastRequest;
    }
	
}
