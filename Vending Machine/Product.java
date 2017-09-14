package Vending;

public enum Product {
	Coke("Coke",25), Pepsi("Pepsi",35), Soda("Soda",45);
	private int value;
	private String name;
	Product(String name, int val){
		this.value = val;
		this.name = name;
	}
	public int getValue(){
		return value;
	}
	public String getName(){
		return name;
	}
}

class check1{
	public static void main(String[] args){
		System.out.println(Product.Coke);
	}
}