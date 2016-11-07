package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when a SEI implementing class isn´t found.
 *
 * @author jonashackt
 */
public class SeiImplementingClassNotFoundException extends BootStarterCxfException {

    private String notFoundClassName;

    public SeiImplementingClassNotFoundException(String message) {
        super(message);
    }

    public String getNotFoundClassName() {
        return notFoundClassName;
    }

    public void setNotFoundClassName(String notFoundClassName) {
        this.notFoundClassName = notFoundClassName;
    }
}
