package com.excilys.exception;

public class UnknowCompanyException extends Exception {

	public UnknowCompanyException() {
		super("This identifier is unknow, please, enter a correct identifier of a company.");
	}
	
}