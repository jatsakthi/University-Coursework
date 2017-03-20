package com.appealing.client.activities;

import java.net.URI;

import com.appealing.client.ClientAppeal;
import com.appealing.model.Appeal;
import com.appealing.representations.AppealRepresentation;

public class PlaceAppealActivity extends Activity {

    private Appeal appeal;

    public void placeAppeal(Appeal appeal, URI appealingUri) {
        
        try {
            AppealRepresentation createdAppealRepresentation = binding.createAppeal(appeal, appealingUri);
            this.actions = new RepresentationHypermediaProcessor().extractNextActionsFromAppealRepresentation(createdAppealRepresentation);
            this.appeal = createdAppealRepresentation.getAppeal();
        } catch (MalformedAppealException e) {
            this.actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            this.actions = retryCurrentActivity();
        }
    }
    
    public ClientAppeal getAppeal() {
        return new ClientAppeal(appeal);
    }
}
