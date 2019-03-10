package com.appealing.client.activities;

import java.net.URI;

public class CancelAppealActivity extends Activity {

    private final URI cancelUri;

    public CancelAppealActivity(URI cancelUri) {
        this.cancelUri = cancelUri;
    }

    public void cancelAppeal() {
        try {           
            binding.deleteAppeal(cancelUri);
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        } catch (CannotCancelException e) {
            actions = noFurtherActivities();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        }
    }
}
