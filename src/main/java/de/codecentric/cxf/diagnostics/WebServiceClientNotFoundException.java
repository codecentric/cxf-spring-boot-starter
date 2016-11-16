package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when the WebServiceClient class isn´t found.
 *
 * @author jonashackt
 */
public class WebServiceClientNotFoundException extends BootStarterCxfException {

    public WebServiceClientNotFoundException(String message) {
        super(message);
    }
}
