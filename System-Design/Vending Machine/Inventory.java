/**
 * 
 */
package Vending;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jatsakthi
 *
 */
public class Inventory<T> {
	private Map<T,Integer> items;
	private String name;
	public Inventory(String name){
		this.name = name;
		items = new HashMap<T,Integer>();
	}
	public Inventory(String name, List<T> items){
		this(name);
		increment(items);
	}
	public Map<T,Integer> getItems(){
		return items;
	}
	public int getCount(T item){
		return this.items.getOrDefault(item,0);
	}
	public boolean isAvailable(T item){
		return getCount(item)>0;
	}
	public boolean decrement(T item){
		if(!isAvailable(item)) return false;
		int count = getCount(item);
		this.items.put(item, count-1);
		return true;
	}
	public boolean decrement(List<T> items){
		for(T item : items){
			if(!decrement(item)) return false;
		}
		return true;
	}
	public void increment(T item){
		int count = getCount(item);
		this.items.put(item, count+1);
		
	}
	public void increment(List<T> items){
		for(T item : items){
			increment(item);
		}
	}
	public void display(){
		System.out.println("Inventory Details for "+this.name+" ....");
		for(Map.Entry<T, Integer> e: this.items.entrySet()){
			System.out.println(e.getKey()+":"+e.getValue());
		}
		System.out.println("-----------------------------------------");
	}
	public void clearInventory(){
		items.clear();
	}
}
