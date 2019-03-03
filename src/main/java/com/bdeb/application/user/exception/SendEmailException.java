package com.bdeb.application.user.exception;

public class SendEmailException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SendEmailException (String msg) {
		super(msg);
	}
}
