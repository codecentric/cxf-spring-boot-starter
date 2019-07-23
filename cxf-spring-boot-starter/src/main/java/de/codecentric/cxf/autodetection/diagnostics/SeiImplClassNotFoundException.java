package de.codecentric.cxf.autodetection.diagnostics;

import de.codecentric.cxf.common.BootStarterCxfException;

/**
 * Thrown when a SEI implementing class isn´t found.
 *
 * @author jonashackt
 */
public class SeiImplClassNotFoundException extends BootStarterCxfException {

    protected static final String MESSAGE = "The Service Endpoint Interface (SEI) implementing class couldn't be found";

    public SeiImplClassNotFoundException() {
        super(MESSAGE);
    }

    public static SeiImplClassNotFoundException build() {
        return new SeiImplClassNotFoundException();
    }

    private String notFoundClassName;

    private String scannedBasePackage;

    public String getNotFoundClassName() {
        return notFoundClassName;
    }

    public SeiImplClassNotFoundException setNotFoundClassName(String notFoundClassName) {
        this.notFoundClassName = notFoundClassName;
        return this;
    }

    public SeiImplClassNotFoundException setScannedBasePackage(String scannedBasePackage) {
        this.scannedBasePackage = scannedBasePackage;
        return this;
    }

    public String getScannedBasePackage() {
        return scannedBasePackage;
    }
}
