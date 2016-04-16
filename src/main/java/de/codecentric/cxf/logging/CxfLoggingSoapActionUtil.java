package de.codecentric.cxf.logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tries to extract methodname from SOAP webservice based on the SOAPAction HTTP-Header. Only works with SOAP 1.1, cause in 1.2 this Header is gone.
 * 
 * @author Jonas Hecht
 *
 */
public final class CxfLoggingSoapActionUtil {

    // (?<=SOAPAction=\["urn:)[a-zA-Z]+(?=\"]) 
    private static final String REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URN = "(?<=SOAPAction=\\[\"urn:)[a-zA-Z]+(?=\"])";
    // (?<=SOAPAction=\[urn:)[a-zA-Z]+(?=\]) 
    private static final String REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URN_WITHOUT_QUOTES = "(?<=SOAPAction=\\[urn:)[a-zA-Z]+(?=\\])";
    // (?<=SOAPAction=\[")[:./a-zA-Z]+(?=\"]) 
    private static final String REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URL = "(?<=SOAPAction=\\[\")[:./a-zA-Z]+(?=\"])";
     
    public static String extractSoapMethodNameFromHttpHeader(String header) {
        Matcher matcher = buildMatcher(header, REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URN);
        if (matcher.find()) {
            return matcher.group(0);
        }
        matcher = buildMatcher(header, REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URN_WITHOUT_QUOTES);
        if (matcher.find()) {
        	 return matcher.group(0);
        }
        matcher = buildMatcher(header, REGEX_FIND_SOAP_METHOD_NAME_IN_HTTP_HEADER_WITH_URL);
        if (matcher.find()) {
            return eliminateStartingUrl(matcher.group(0));
        }
        return ""; // This should´nt happen in reality, because the SOAP-Spec demands the SOAP-Service-Method as SOAPAction
    }

    private static String eliminateStartingUrl(String methodWithUrl) {
        int lastSlash = methodWithUrl.lastIndexOf('/');
        return methodWithUrl.substring(lastSlash + 1);
    }

    private static Matcher buildMatcher(String string2SearchIn, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string2SearchIn);
    }
}
