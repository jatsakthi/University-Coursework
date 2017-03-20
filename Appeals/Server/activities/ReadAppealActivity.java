package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.repositories.AppealRepository;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.RestbucksUri;
import com.appealing.model.Appeal;
public class ReadAppealActivity {
    public AppealRepresentation retrieveByUri(RestbucksUri appealUri) {
        Identifier identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealRepresentation.createResponseOrderRepresentation(appeal, appealUri);
    }
}
