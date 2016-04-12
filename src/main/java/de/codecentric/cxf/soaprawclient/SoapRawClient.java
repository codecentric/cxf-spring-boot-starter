package de.codecentric.cxf.soaprawclient;

import java.io.InputStream;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.common.XmlUtils;

@Component
public class SoapRawClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoapRawClient.class);
	private String soapAction;
	
	private String soapServiceUrl;
	
	public <T> SoapRawClient(String soapServiceUrl, Class<T> jaxWsServiceInterfaceClass) throws BootStarterCxfException {
	    this.soapAction = XmlUtils.getSoapActionFromJaxWsServiceInterface(jaxWsServiceInterfaceClass);
	    this.soapServiceUrl = soapServiceUrl;
	}
	
	// Invisible Constructor, because soapServiceUrl is mandatory
	private SoapRawClient() {};
	
	public SoapRawClientResponse callSoapService(InputStream xmlFile) throws BootStarterCxfException {
		SoapRawClientResponse easyRawSoapResponse = new SoapRawClientResponse();
		
		LOGGER.debug("Calling SoapService with POST on Apache HTTP-Client and configured URL: {}", soapServiceUrl);
		
		try {
			Response httpResponseContainer = Request
					.Post(soapServiceUrl)
					.bodyStream(xmlFile, contentTypeTextXmlUtf8())
					.addHeader("SOAPAction", "\"" + soapAction + "\"")
					.execute();
			
			HttpResponse httpResponse = httpResponseContainer.returnResponse();			
			easyRawSoapResponse.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
			easyRawSoapResponse.setHttpResponseBody(XmlUtils.parseFileStream2Document(httpResponse.getEntity().getContent()));
			
		} catch (Exception exception) {
			throw new BootStarterCxfException("Some Error accured while trying to Call SoapService for test: " + exception.getMessage());
		}		
		return easyRawSoapResponse;
	}

	private ContentType contentTypeTextXmlUtf8() {
		return ContentType.create(ContentType.TEXT_XML.getMimeType(), Consts.UTF_8);
	}
	
}
