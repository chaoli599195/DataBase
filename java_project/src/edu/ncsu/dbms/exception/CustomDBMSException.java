package edu.ncsu.dbms.exception;

public class CustomDBMSException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomDBMSException(String message, Throwable cause) {

		super(message + ". Original exception message - " + cause.getMessage()
				+ ". Cause - " + cause.getCause(), cause);
	}

	public CustomDBMSException(String message) {

		super(message);
	}

}
