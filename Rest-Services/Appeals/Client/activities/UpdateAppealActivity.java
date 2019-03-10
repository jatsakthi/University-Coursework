package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.Appeal;
import com.appealing.model.AppealStatus;
import com.appealing.repositories.AppealRepository;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.RestbucksUri;

public class UpdateAppealActivity {
    public AppealRepresentation update(Appeal appeal, RestbucksUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the order
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedappeal = repository.get(appealIdentifier);
        
        storedappeal.setStatus(storedappeal.getStatus());

        return AppealRepresentation.createResponseOrderRepresentation(storedappeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.UNREVIEWED;
    }
}
