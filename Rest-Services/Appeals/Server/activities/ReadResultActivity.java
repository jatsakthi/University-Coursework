package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.AppealStatus;
import com.appealing.model.Review;
import com.appealing.repositories.AppealRepository;
import com.appealing.repositories.ReviewRepository;
import com.appealing.representations.Link;
import com.appealing.representations.ResultRepresentation;
import com.appealing.representations.Representation;
import com.appealing.representations.RestbucksUri;

public class ReadResultActivity {

    public ResultRepresentation read(RestbucksUri resultUri) {
        Identifier identifier = resultUri.getId();
        if(!appealHasBeenReviewed(identifier)) {
            throw new AppealNotReviewedException();
        } else if (AppealRepository.current().has(identifier) && AppealRepository.current().get(identifier).getStatus() == AppealStatus.REVIEWED) {
            throw new AppealAlreadyCompletedException();
        }
        
        Review review = ReviewRepository.current().get(identifier);
        
        return new ResultRepresentation(review, new Link(Representation.RELATIONS_URI + "appeal", UriExchangeAppeal.appealForResult(resultUri)));
    }

    private boolean appealHasBeenReviewed(Identifier id) {
        return ReviewRepository.current().has(id);
    }

}
