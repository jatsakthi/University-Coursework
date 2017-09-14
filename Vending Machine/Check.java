package Vending;

public class Check {

	public static void main(String[] args) {
		try{
			VendingInterface vm = (VMImplementation) VMFactoryMethod.getVendingImplementation();
			vm.display();
			
			for(int i=0;i<10;i++){
				System.out.println(vm.selectItemAndGetPrice(Product.Coke));
				vm.insertCoin(Coin.Quarter);
				vm.collectItemAndChange().display();
				vm.display();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
