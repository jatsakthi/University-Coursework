package com.appealing.client.activities;

import java.net.URI;

import com.appealing.representations.ResultRepresentation;

public class GetResultActivity extends Activity {
    private final URI receiptUri;
    private ResultRepresentation representation;

    public GetResultActivity(URI receiptUri) {
        this.receiptUri = receiptUri;
    }

    public void getResultForAppeal() {
        try {
          
            representation = binding.retrieveResult(receiptUri);
            actions = new Actions();
            if(representation.getAppealLink() != null) {
                actions.add(new ReadAppealActivity(representation.getAppealLink().getUri()));
            } else {
                actions =  noFurtherActivities();
            }
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        }
    }

    public ResultRepresentation getResult() {
        return representation;
    }
}
