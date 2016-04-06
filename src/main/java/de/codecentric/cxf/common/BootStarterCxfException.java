package de.codecentric.cxf.common;

public class BootStarterCxfException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BootStarterCxfException(Throwable cause) {
		super(cause);
	}

	public BootStarterCxfException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BootStarterCxfException(String message) {
		super(message);
	}

}
