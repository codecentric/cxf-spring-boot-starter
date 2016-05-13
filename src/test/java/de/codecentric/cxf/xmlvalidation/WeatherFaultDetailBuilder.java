package de.codecentric.cxf.xmlvalidation;

import org.springframework.stereotype.Component;

import de.codecentric.cxf.common.FaultType;
import de.codecentric.namespace.weatherservice.exception.WeatherException;

@Component
public class WeatherFaultDetailBuilder implements CustomFaultDetailBuilder {
	
	private de.codecentric.namespace.weatherservice.exception.ObjectFactory objectFactoryDatatypes = new de.codecentric.namespace.weatherservice.exception.ObjectFactory();
	
	@Override
	public WeatherException createCustomFaultDetail(String originalFaultMessage, FaultType faultContent) {
		// Build SOAP-Fault detail <datatypes:WeatherException>
		WeatherException weatherException = objectFactoryDatatypes.createWeatherException();		
		weatherException.setBigBusinessErrorCausingMoneyLoss(true);
		weatherException.setBusinessErrorId(faultContent.getId());
		weatherException.setExceptionDetails(originalFaultMessage);
		weatherException.setUuid("ExtremeRandomNumber");
		return weatherException;
	}
}
