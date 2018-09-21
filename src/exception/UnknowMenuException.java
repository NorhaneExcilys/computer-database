package exception;

public class UnknowMenuException extends Exception {

	public UnknowMenuException() {
		super("Incorrect input. Please enter a number between 1 and 7.");
	}
	
}
