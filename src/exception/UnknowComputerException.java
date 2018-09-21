package exception;

public class UnknowComputerException extends Exception {

	public UnknowComputerException() {
		super("This identifier is unknow, please, enter a correct identifier of a computer.");
	}
	
}
