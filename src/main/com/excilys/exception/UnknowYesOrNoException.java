package com.excilys.exception;

public class UnknowYesOrNoException extends Exception {

	public UnknowYesOrNoException() {
		super("Incorrect input. Please enter yes or no.");
	}
	
}
