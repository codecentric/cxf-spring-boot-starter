package de.codecentric.cxf.autodetection.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when the Service Endpoint Interface (SEI) itself isn´t found.
 *
 * @author jonashackt
 */
public class SeiNotFoundException extends BootStarterCxfException {

    protected static final String MESSAGE = "The Service Endpoint Interface (SEI) could´nt be found - an interface that´s annotated with javax.jws.WebService";

    public SeiNotFoundException() {
        super(MESSAGE);
    }
}
