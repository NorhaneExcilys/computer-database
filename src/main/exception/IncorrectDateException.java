package exception;

public class IncorrectDateException extends Exception {

	public IncorrectDateException(String message) {
		super(message);
	}
	
	public IncorrectDateException() {
		super("Please, enter a correct date with a correct format");
	}
	
}
