package com.microservices.spring.exceptions.models;

public class EmailNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
