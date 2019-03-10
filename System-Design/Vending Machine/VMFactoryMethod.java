package Vending;

public class VMFactoryMethod {
	public static VendingInterface getVendingImplementation(){
		return new VMImplementation();
	}
}
