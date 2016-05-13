package de.codecentric.cxf.common;

/**
 * Ids and Messages for generation of SoapFaults according to XML-Valdiation-Errors
 */
public enum FaultType {

	SCHEME_VALIDATION_ERROR("cxf_boot_error_001", "XML-Scheme-validiation failed."),
	SYNTACTICALLY_INCORRECT_XML_ERROR("cxf_boot_error_002", "Syntactically incorrect XML."),	
	BACKEND_PROCESSING_FAILED("cxf_boot_error_003", "Backend processing failed.");

	private String id;
	private String message;
	
	private FaultType(String id, String text) {
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
