package com.appealing.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appealing.model.Review;
@XmlRootElement(name = "review", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ReviewRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReviewRepresentation.class);
       
    @XmlElement(name="reviewNote",namespace = ReviewRepresentation.RESTBUCKS_NAMESPACE) private String updateNote;
     
    
    /**
     * For JAXB :-(
     */
     ReviewRepresentation(){
        LOG.debug("In ReviewRepresentation Constructor");
     }
    
    public ReviewRepresentation(Review review, Link...links) {
        LOG.info("Creating an Review Representation with the request = {} and links = {}", review, links);
        
        updateNote = review.getUpdateNote();
        this.links = java.util.Arrays.asList(links);
        
        LOG.debug("Created the Review Representation {}", this);
    }

    public Review getReview() {
        return new Review(updateNote);
    }
    
    public Link getResultLink() {
        return getLinkByName(Representation.RELATIONS_URI + "result");
    }
    
    public Link getAppealLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeal");
    }
}
