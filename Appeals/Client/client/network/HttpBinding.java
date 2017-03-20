package com.appealing.client.network;

import java.net.URI;

import com.appealing.client.activities.CannotCancelException;
import com.appealing.client.activities.CannotUpdateAppealException;
import com.appealing.client.activities.DuplicateReviewException;
import com.appealing.client.activities.InvalidReviewException;
import com.appealing.client.activities.MalformedAppealException;
import com.appealing.client.activities.NotFoundException;
import com.appealing.client.activities.ServiceFailureException;
import com.appealing.model.Appeal;
import com.appealing.model.Review;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.ReviewRepresentation;
import com.appealing.representations.ResultRepresentation;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class HttpBinding {

    private static final String RESTBUCKS_MEDIA_TYPE = "application/vnd.restbucks+xml";

    public AppealRepresentation createAppeal(Appeal appeal, URI orderUri) throws MalformedAppealException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(orderUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating Appeal resource [%s]", status, orderUri.toString()));
    }
    
    public AppealRepresentation retrieveAppeal(URI appealUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(RESTBUCKS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();

        if (status == 404) {
            throw new NotFoundException ();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response while retrieving order resource [%s]", appealUri.toString()));
    }

    public AppealRepresentation updateAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException, NotFoundException,
            CannotUpdateAppealException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 409) {
            throw new CannotUpdateAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while udpating Appeal resource [%s]", status, appealUri.toString()));
    }

    public AppealRepresentation deleteAppeal(URI appealUri) throws ServiceFailureException, CannotCancelException, NotFoundException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(RESTBUCKS_MEDIA_TYPE).delete(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new CannotCancelException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while deleting order resource [%s]", status, appealUri.toString()));
    }

    public ReviewRepresentation makeReview(Review review, URI reviewUri) throws InvalidReviewException, NotFoundException, DuplicateReviewException,
            ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(reviewUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).put(ClientResponse.class, new ReviewRepresentation(review));

        int status = response.getStatus();
        if (status == 400) {
            throw new InvalidReviewException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new DuplicateReviewException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(ReviewRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating review resource [%s]", status, reviewUri.toString()));
    }

    public ResultRepresentation retrieveResult(URI resultUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(resultUri).accept(RESTBUCKS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(ResultRepresentation.class);
        }
        
        throw new RuntimeException(String.format("Unexpected response [%d] while retrieving result resource [%s]", status, resultUri.toString()));
    }
}
