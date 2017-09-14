/**
 * 
 */
package Vending;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author jatsakthi
 *
 */
public class VMImplementation implements VendingInterface{

	private Inventory<Product> products;
	private Inventory<Coin> coins;
	private List<Coin> insertedCoins;
	private int credit;
	private Product selectedItem;
	public VMImplementation(){
		products = new Inventory<>("Products");
		coins = new Inventory<>("Coins");
		insertedCoins = new ArrayList<Coin>();
		credit = 0;
		fillUp();
	}
	public void fillUp(){
		
		for(Coin coin: Coin.values())
			for(int i=0;i<5;i++)	
				coins.increment(coin);
		for(Product product: Product.values())
			for(int i=0;i<5;i++)	
				products.increment(product);
	}
	
	public void stats(){
		display();
		coins.display();
	}
	@Override
	public void display() {
		products.display();
	}

	@Override
	public void reset() {
		clearAll();
		fillUp();
	}
	public void clearAll(){
		coins.clearInventory();
		products.clearInventory();
	}

	@Override
	public void insertCoin(Coin coin) {
		credit += coin.getValue();
		insertedCoins.add(coin);
	}

	@Override
	public List<Coin> refund() {
		selectedItem = null;
		credit = 0;
		List<Coin> toReturnCoins = insertedCoins;
		insertedCoins = null;
		return toReturnCoins;
	}

	@Override
	public Bucket<Product, List<Coin>> collectItemAndChange(){
		if(selectedItem == null){
			refund();
			throw new ItemNotSelectedException("Item not Selected");
		}
		if(!products.isAvailable(selectedItem)){
			refund();
			throw new ItemNotAvailableException("Item not available");
		}
		if(credit==0 || credit < selectedItem.getValue()){
			refund();
			throw new NotSufficientMoneyPaid("Amount not Paid");
		}
		List<Coin> change = null;
		try{
			change = getChange();
		}catch(Exception e){
			refund();
			throw e;
		}
		products.decrement(selectedItem);
		coins.increment(insertedCoins);
		coins.decrement(change);
		Product item = selectedItem;
		selectedItem = null;
		credit = 0;
		insertedCoins.clear();
		return new Bucket<Product, List<Coin>>(item,change);
	}

	private List<Coin> getChange() {
		List<Coin> change = new ArrayList<Coin>();
		int reqd = credit - selectedItem.getValue();
		Coin[] coins = Coin.values();
		Arrays.sort(coins,new Comparator<Coin>(){
			public int compare(Coin a, Coin b){
				return -1*(a.getValue()-b.getValue());
			}
		});
		for(Coin coin: coins){
			if(this.coins.isAvailable(coin)){
				int count = this.coins.getCount(coin);
				count = Math.min(count, reqd/coin.getValue());
				reqd -= (count*coin.getValue());
				for(int j=0;j<count;j++){
					change.add(coin);
				}
				if(reqd==0) break;
			}
		}
		if(reqd!=0) throw new InsufficientFundsException("Not Enough Funds");
		return change;
	}
	@Override
	public int selectItemAndGetPrice(Product item) {
		selectedItem = item;
		return item.getValue();
	}

}
