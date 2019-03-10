package Vending;

public enum Coin {
	Penny(1), Nickle(5), Dime(10), Quarter(25);
	private int value;
	Coin(int val){
		this.value = val;
	}
	public int getValue(){
		return value;
	}
}

class check{
	public static void main(String[] args){
		System.out.println(Coin.Dime.getValue());
		System.out.println(Coin.values());
	}
}
