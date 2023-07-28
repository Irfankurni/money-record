package com.irfan.moneyrecord.exception;

public class InvalidLoginException extends RuntimeException{

	private static final long serialVersionUID = -2395781983461194L;

	public InvalidLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLoginException(String message) {
		super(message);
	}

	public InvalidLoginException(Throwable cause) {
		super(cause);
	}

}
