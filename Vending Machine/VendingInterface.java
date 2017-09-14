/**
 * 
 */
package Vending;

import java.util.List;

/**
 * @author jatsakthi
 *
 */
public interface VendingInterface {
	public void display();
	public void reset();
	public void insertCoin(Coin coin);
	public List<Coin> refund();
	public Bucket<Product,List<Coin>> collectItemAndChange();
	public int selectItemAndGetPrice(Product item);
}
