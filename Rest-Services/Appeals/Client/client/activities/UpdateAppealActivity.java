package com.appealing.client.activities;

import java.net.URI;

import com.appealing.client.ClientAppeal;
import com.appealing.model.Appeal;
import com.appealing.representations.AppealRepresentation;

public class UpdateAppealActivity extends Activity {

    private final URI updateUri;
    private AppealRepresentation updatedAppealRepresentation;

    public UpdateAppealActivity(URI updateUri) {
        this.updateUri = updateUri;
    }

    public void updateOrder(Appeal appeal) {
        try {
            updatedAppealRepresentation = binding.updateAppeal(appeal, updateUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromAppealRepresentation(updatedAppealRepresentation);
        } catch (MalformedAppealException e) {
            actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (CannotUpdateAppealException e) {
            actions = noFurtherActivities();
        }
    }
    
    public ClientAppeal getOrder() {
        return new ClientAppeal(updatedAppealRepresentation.getAppeal());
    }
}
