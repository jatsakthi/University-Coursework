package Vending;

public class NotSufficientMoneyPaid extends RuntimeException {
	public NotSufficientMoneyPaid(String string) {
		super(string);
	}
}
