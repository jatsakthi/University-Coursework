package Vending;

public class EmptyInventoryException extends RuntimeException{
	public EmptyInventoryException(String string) {
		super(string);
	}
}
