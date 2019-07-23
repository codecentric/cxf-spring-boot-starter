package de.codecentric.cxf.autodetection.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when a SEI implementing class isn´t found.
 *
 * @author jonashackt
 */
public class CxfSpringBootMavenPropertiesNotFoundException extends BootStarterCxfException {

    protected static final String MESSAGE = "The cxf-spring-boot-maven.properties could´nt be found";

    public CxfSpringBootMavenPropertiesNotFoundException(String message, Throwable cause) {
        super(message + MESSAGE, cause);
    }

}
