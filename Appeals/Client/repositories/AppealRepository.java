package com.appealing.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.appealing.model.Identifier;
import com.appealing.model.Appeal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppealRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepository.class);

    private static final AppealRepository theRepository = new AppealRepository();
    private HashMap<String, Appeal> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static AppealRepository current() {
        return theRepository;
    }
    
    private AppealRepository(){
        LOG.debug("OrderRepository Constructor");
    }
    
    public Appeal get(Identifier identifier) {
        LOG.debug("Retrieving Appeal object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
     }
    
    public Appeal take(Identifier identifier) {
        LOG.debug("Removing the Appeal object for identifier {}", identifier);
        Appeal appeal = backingStore.get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(Appeal appeal) {
        LOG.debug("Storing a new Order object");
                
        Identifier id = new Identifier();
        LOG.debug("New appeal object id is {}", id);
                
        backingStore.put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier identifier, Appeal appeal) {
        LOG.debug("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        LOG.debug("Checking to see if there is an Appeal object associated with the id {} in the Order store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        LOG.debug("The result of the search is {}", result);
        
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.debug("Removing from storage the Appeal object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    public boolean appealPlaced(Identifier identifier) {
        LOG.debug("Checking to see if the appeal with id = {} has been place", identifier);
        return AppealRepository.current().has(identifier);
    }
    
    public boolean appealNotPlaced(Identifier identifier) {
        LOG.debug("Checking to see if the order with id = {} has not been place", identifier);
        return !appealPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Appeal> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<>();
    }

    public int size() {
        return backingStore.size();
    }
}
