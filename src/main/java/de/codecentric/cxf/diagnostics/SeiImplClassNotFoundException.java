package de.codecentric.cxf.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when a SEI implementing class isn´t found.
 *
 * @author jonashackt
 */
public class SeiImplClassNotFoundException extends BootStarterCxfException {

    public SeiImplClassNotFoundException(String message) {
        super(message);
    }

    private String notFoundClassName;

    public String getNotFoundClassName() {
        return notFoundClassName;
    }

    public void setNotFoundClassName(String notFoundClassName) {
        this.notFoundClassName = notFoundClassName;
    }
}
