package com.microservices.spring.exceptions.models;

public class UsernameExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameExistException() {
		// TODO Auto-generated constructor stub
	}

	public UsernameExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UsernameExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UsernameExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UsernameExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
