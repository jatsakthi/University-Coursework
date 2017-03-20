package com.appealing.client.activities;

import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.ReviewRepresentation;

class RepresentationHypermediaProcessor {

    Actions extractNextActionsFromAppealRepresentation(AppealRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getReviewLink() != null) {
                actions.add(new ReviewActivity(representation.getReviewLink().getUri()));
            }

            if (representation.getUpdateLink() != null) {
                actions.add(new UpdateAppealActivity(representation.getUpdateLink().getUri()));
            }

            if (representation.getSelfLink() != null) {
                actions.add(new ReadAppealActivity(representation.getSelfLink().getUri()));
            }

            if (representation.getCancelLink() != null) {
                actions.add(new CancelAppealActivity(representation.getCancelLink().getUri()));
            }
        }

        return actions;
    }

    public Actions extractNextActionsFromReviewRepresentation(ReviewRepresentation representation) {
        Actions actions = new Actions();
        
        if(representation.getAppealLink() != null) {
            actions.add(new ReadAppealActivity(representation.getAppealLink().getUri()));
        }
        
        if(representation.getResultLink() != null) {
            actions.add(new GetResultActivity(representation.getResultLink().getUri()));
        }
        
        return actions;
    }

}
