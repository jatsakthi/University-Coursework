package com.appealing.activities;

import com.appealing.model.Identifier;
import com.appealing.model.Appeal;
import com.appealing.model.AppealStatus;
import com.appealing.repositories.AppealRepository;
import com.appealing.representations.Link;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.Representation;
import com.appealing.representations.RestbucksUri;

public class CreateAppealActivity {
    public AppealRepresentation create(Appeal appeal, RestbucksUri requestUri) {
        appeal.setStatus(AppealStatus.UNREVIEWED);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        RestbucksUri appealUri = new RestbucksUri(requestUri.getBaseUri() + "/appeal/" + identifier.toString());
        RestbucksUri reviewUri = new RestbucksUri(requestUri.getBaseUri() + "/review/" + identifier.toString());
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "cancel", appealUri), 
                new Link(Representation.RELATIONS_URI + "review", reviewUri), 
                new Link(Representation.RELATIONS_URI + "update", appealUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
}
