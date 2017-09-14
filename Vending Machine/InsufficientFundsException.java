package Vending;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(String string) {
		super(string);
	}
}
