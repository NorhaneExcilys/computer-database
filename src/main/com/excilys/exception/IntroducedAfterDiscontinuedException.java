package com.excilys.exception;

public class IntroducedAfterDiscontinuedException extends Exception {

	public IntroducedAfterDiscontinuedException() {
		super("The introduced date must be before the discontinued date.");
	}
	
}
