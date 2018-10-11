package exception;

public class IncorrectDateException extends Exception {

	public IncorrectDateException() {
		super("This date is incorrect, please, enter a correct date.");
	}
	
}
