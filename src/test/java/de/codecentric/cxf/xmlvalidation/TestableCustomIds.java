package de.codecentric.cxf.xmlvalidation;

/**
 * Custom Ids and Messages for WeatherException-Detail
 */
public enum TestableCustomIds {

	EVERYTHING_FINE("01", "The Weather-Call was successfully processed by the backend."),
	NON_XML_COMPLIANT("71", "XML-Scheme-validiation failed."),
	COMPLETE_USELESS_XML("72", "Syntactically incorrect XML."),
	SOMETHING_ELSE_WENT_TERRIBLY_WRONG("99", "Backend processing failed.");

	private String id;
	private String message;

	private TestableCustomIds(String id, String text) {
		this.id = id;
		this.message = text;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getId() {
		return id;
	}
	
}
