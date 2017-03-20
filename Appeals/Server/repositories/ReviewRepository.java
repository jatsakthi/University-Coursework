package com.appealing.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.appealing.model.Identifier;
import com.appealing.model.Review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReviewRepository.class);

    private static final ReviewRepository theRepository = new ReviewRepository();
    private HashMap<String, Review> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static ReviewRepository current() {
        return theRepository;
    }
    
    private ReviewRepository(){
        LOG.debug("ReviewRepository Constructor");
    }
    
    public Review get(Identifier identifier) {
        LOG.debug("Retrieving Review object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
    }
    
    public Review take(Identifier identifier) {
        LOG.debug("Removing the Review object for identifier {}", identifier);
        Review review = backingStore.get(identifier.toString());
        remove(identifier);
        return review;
    }

    public Identifier store(Review review) {
        LOG.debug("Storing a new Review object");
        
        Identifier id = new Identifier();
        LOG.debug("New review object's id is {}", id);
        
        backingStore.put(id.toString(), review);
        return id;
    }
    
    public void store(Identifier identifier, Review review) {
        LOG.debug("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), review);
    }

    public boolean has(Identifier identifier) {
        LOG.debug("Checking to see if there is a review object associated with the id {} in the Review store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.debug("Removing from storage the Payment object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Review> entry : backingStore.entrySet()) {
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
}
