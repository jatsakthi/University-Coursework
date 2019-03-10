/**
 * 
 */
package Vending;

/**
 * @author jatsakthi
 *
 */
public class Bucket<E1,E2> {
	private E1 val1;
	private E2 val2;
	public Bucket(E1 val1, E2 val2){
		this.val1 = val1;
		this.val2 = val2;
	}
	public void display(){
		System.out.println("-----Bucket-------");
		System.out.println(val1);
		System.out.println(val2);
		System.out.println("------------------");
	}
	
}
