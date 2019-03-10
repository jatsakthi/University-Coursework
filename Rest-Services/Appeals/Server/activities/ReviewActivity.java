package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.AppealStatus;
import com.appealing.model.Review;
import com.appealing.repositories.AppealRepository;
import com.appealing.repositories.ReviewRepository;
import com.appealing.representations.Link;
import com.appealing.representations.ReviewRepresentation;
import com.appealing.representations.Representation;
import com.appealing.representations.RestbucksUri;

public class ReviewActivity {
    public ReviewRepresentation review(Review review, RestbucksUri reviewUri) {
        Identifier identifier = reviewUri.getId();
        
        // Don't know the order!
        if(!AppealRepository.current().has(identifier)) {
            throw new NoSuchAppealException();
        }
        
        // Already paid
        if(ReviewRepository.current().has(identifier)) {
            throw new UpdateException();
        }
        
    
        // If we get here, let's create the payment and update the order status
        if(AppealRepository.current().get(identifier).getContent().contains("Please") || review.getUpdateNote().contains("Please")){
            AppealRepository.current().get(identifier).setStatus(AppealStatus.POSITIVE);
            ReviewRepository.current().store(identifier, review);
            return new ReviewRepresentation(review, new Link(Representation.RELATIONS_URI + "appeal", UriExchangeAppeal.appealForReview(reviewUri)),
                  new Link(Representation.RELATIONS_URI + "result", UriExchangeAppeal.resultForReview(reviewUri)));
        }
        else if(AppealRepository.current().get(identifier).getContent().contains("Not my Mistake") || review.getUpdateNote().contains("Not my Mistake")){
            AppealRepository.current().get(identifier).setStatus(AppealStatus.NEGATIVE);
            ReviewRepository.current().store(identifier, review);
            return new ReviewRepresentation(review, new Link(Representation.RELATIONS_URI + "appeal", UriExchangeAppeal.appealForReview(reviewUri)),
                new Link(Representation.RELATIONS_URI + "result", UriExchangeAppeal.resultForReview(reviewUri)));
        }
        else{
            AppealRepository.current().get(identifier).setStatus(AppealStatus.UNREVIEWED);
            return new ReviewRepresentation(review, new Link(Representation.RELATIONS_URI + "appeal", UriExchangeAppeal.appealForReview(reviewUri)),
                new Link(Representation.RELATIONS_URI + "review", reviewUri));
            //ReviewRepository.current().store(identifier, review);
        }
        
        
        
        
    }
}
