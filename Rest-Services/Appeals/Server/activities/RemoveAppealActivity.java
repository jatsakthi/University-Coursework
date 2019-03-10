package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.Appeal;
import com.appealing.model.AppealStatus;
import com.appealing.repositories.AppealRepository;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.RestbucksUri;

public class RemoveAppealActivity {
    public AppealRepresentation delete(RestbucksUri appealUri) {
        // Discover the URI of the order that has been cancelled
        
        Identifier identifier = appealUri.getId();

        AppealRepository appealRepository = AppealRepository.current();

        if (appealRepository.appealNotPlaced(identifier)) {
            throw new NoSuchAppealException();
        }
        
        Appeal appeal = appealRepository.get(identifier);
        
        // Can't delete a ready or preparing order
        if (appeal.getStatus() == AppealStatus.REVIEWING) {
            throw new AppealDeletionException();
        }

        if(appeal.getStatus() == AppealStatus.UNREVIEWED) { // An unpaid order is being cancelled 
            appealRepository.remove(identifier);
        }

        return new AppealRepresentation(appeal);
    }

}
