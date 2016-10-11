package de.codecentric.cxf.xmlvalidation;

import org.springframework.stereotype.Component;

import de.codecentric.cxf.common.FaultType;
import de.codecentric.namespace.weatherservice.exception.WeatherException;

@Component
public class WeatherFaultBuilder implements CustomFaultBuilder {
	
	private de.codecentric.namespace.weatherservice.exception.ObjectFactory objectFactoryDatatypes = new de.codecentric.namespace.weatherservice.exception.ObjectFactory();

	@Override
	public String createCustomFaultMessage(FaultType faultType) {
		if(FaultType.SCHEME_VALIDATION_ERROR.equals(faultType)) {
			return TestableCustomIds.NON_XML_COMPLIANT.getMessage();
		}
		else if(FaultType.SYNTACTICALLY_INCORRECT_XML_ERROR.equals(faultType)) {
			return TestableCustomIds.COMPLETE_USELESS_XML.getMessage();
		}
		else {
			return TestableCustomIds.SOMETHING_ELSE_WENT_TERRIBLY_WRONG.getMessage();
		}
	}

	@Override
	public WeatherException createCustomFaultDetail(String originalFaultMessage, FaultType faultType) {
		// Build SOAP-Fault detail <datatypes:WeatherException>
		WeatherException weatherException = objectFactoryDatatypes.createWeatherException();		
		weatherException.setBigBusinessErrorCausingMoneyLoss(true);
		setIdBasedUponFaultContent(faultType, weatherException);
		weatherException.setExceptionDetails(originalFaultMessage);
		weatherException.setUuid("ExtremeRandomNumber");
		return weatherException;
	}

	private void setIdBasedUponFaultContent(FaultType faultType, WeatherException weatherException) {
		if(FaultType.SCHEME_VALIDATION_ERROR.equals(faultType)) {
			weatherException.setBusinessErrorId(TestableCustomIds.NON_XML_COMPLIANT.getId());
		}
		else if(FaultType.SYNTACTICALLY_INCORRECT_XML_ERROR.equals(faultType)) {
			weatherException.setBusinessErrorId(TestableCustomIds.COMPLETE_USELESS_XML.getId());
		}
		else {
			weatherException.setBusinessErrorId(TestableCustomIds.SOMETHING_ELSE_WENT_TERRIBLY_WRONG.getId());
		}
	}
}
