package com.appealing.client.activities;

import java.net.URI;

import com.appealing.model.Review;
import com.appealing.representations.ReviewRepresentation;

public class ReviewActivity extends Activity {

    private final URI reviewUri;
    private Review review;

    public ReviewActivity(URI reviewUri) {
        this.reviewUri = reviewUri;
    }

    public void reviewForAppeal(Review Review) {        
        try {
            ReviewRepresentation reviewRepresentation = binding.makeReview(review, reviewUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromReviewRepresentation(reviewRepresentation);
            review = reviewRepresentation.getReview();
        } catch (InvalidReviewException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (DuplicateReviewException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();            
        }
    }
    
    public Review getReview() {
        return review;
    }
}
