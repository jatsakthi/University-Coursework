package Vending;

public class ItemNotAvailableException extends RuntimeException {
	public ItemNotAvailableException(String string) {
		super(string);
	}
}
