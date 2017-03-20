package com.appealing.client;

import java.net.URI;
import java.net.URISyntaxException;

import com.appealing.client.activities.Actions;
import com.appealing.client.activities.GetResultActivity;
import com.appealing.client.activities.ReviewActivity;
import com.appealing.client.activities.PlaceAppealActivity;
import com.appealing.client.activities.ReadAppealActivity;
import com.appealing.client.activities.UpdateAppealActivity;
import com.appealing.model.Appeal;
import com.appealing.model.AppealBuilder;
import com.appealing.model.AppealStatus;
import com.appealing.model.Review;
import com.appealing.representations.Link;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.ReviewRepresentation;
import com.appealing.representations.ResultRepresentation;
import com.appealing.representations.RestbucksUri;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String APPEAL_MEDIA_TYPE = "application/vnd-cse564-appeals+xml";
    private static final long ONE_MINUTE = 60000; 
    
    private static final String ENTRY_POINT_URI = "http://localhost:8080/HATEOAS-Appeals-amuthuk3-Server/webresources/appeal";

    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        happyCaseTest(serviceUri);
        AbandonCaseTest(serviceUri);
        ForgottenCaseTest(serviceUri);
        BadStartCaseTest(serviceUri);
        BadIdCaseTest(serviceUri);
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyCaseTest(URI serviceUri) throws Exception {
        LOG.info(String.format("\n\nStarting HAPPY PATH TEST with Service URI [%s]....................", serviceUri));
        // Place the appeal
        System.out.println("Step 1. Create an appeal");
        LOG.info(String.format("About to start happy case test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal("Please consider my request Sir","CSE564");
        
        LOG.info("Created Appeal:\n{}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEAL_MEDIA_TYPE).type(APPEAL_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info(String.format("Created appealRepresentation:\n{%s}\nDenoted by the URI:\n{%s}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString()));
        LOG.info(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
       
        
        // Submit a review request 
        System.out.println("\n\nStep 2. Submit a review request for the appeal");
        LOG.info(String.format("About to create a review resource at [%s] via PUT", appealRepresentation.getReviewLink().getUri().toString()));
        Link reviewLink = appealRepresentation.getReviewLink();
        LOG.info("Created review link\n{}\nfor the appeal representation\n{}", reviewLink, appealRepresentation);
        LOG.info("reviewLink.getRelValue() = {}", reviewLink.getRelValue());
        LOG.info("reviewLink.getUri() = {}", reviewLink.getUri());
        LOG.info("reviewLink.getMediaType() = {}", reviewLink.getMediaType());
        Review review = new Review("1st submission for Review");
        LOG.info("Created new review object\n{}", review);
        ReviewRepresentation reviewRepresentation = client.resource(reviewLink.getUri()).accept(reviewLink.getMediaType()).type(reviewLink.getMediaType()).put(ReviewRepresentation.class, new ReviewRepresentation(review));
        LOG.info("Created new review representation\n{}", reviewRepresentation);
        LOG.info(String.format("Review submission made, result can be seen at [%s]", reviewRepresentation.getResultLink().getUri().toString()));

        
        System.out.println("\n\nStep 3. Get Result");
        LOG.info(String.format("About to request a result from [%s] via GET", reviewRepresentation.getResultLink().getUri().toString()));
        Link resultLink = reviewRepresentation.getResultLink();
        LOG.info("Retrieved the result link {} for review represntation {}", resultLink, reviewRepresentation);
        ResultRepresentation resultRepresentation = client.resource(resultLink.getUri()).get(ResultRepresentation.class);
        //        LOG.info(String.format("Review Done, Result: [%f]", resultRepresentation.));

        System.out.println("\n\nStep 4. Check on the Appeal status");
        LOG.info(String.format("About to check appeal status at [%s] via GET", resultRepresentation.getAppealLink().getUri().toString()));
        Link orderLink = resultRepresentation.getAppealLink();
        AppealRepresentation finalAppealRepresentation = client.resource(orderLink.getUri()).accept(APPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
        LOG.info(String.format("Finally Reviewed, current status [%s]", finalAppealRepresentation.getStatus()));

        
        System.out.println("\n\nStep 5. See the result");
        LOG.info(String.format("Trying to take the result from [%s] via DELETE", resultRepresentation.getAppealLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(orderLink.getUri()).delete(ClientResponse.class);
        LOG.info(String.format("Tried to see the final result, HTTP status [%d]", finalResponse.getStatus()));
        if (finalResponse.getStatus() == 200) {
            LOG.info(String.format("Appeal status [%s], Have a Nice Day", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }

    }    
    
private static void BadIdCaseTest(URI serviceUri) throws Exception {
        LOG.info("\n\nStarting BAD ID CASE TEST with Service URI {}....................", serviceUri);
        // Place the order
        System.out.println("Step 1. Create an appeal");
        LOG.info(String.format("About to start forgotten case test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal("Consider my request Sir","CSE564");
        
        LOG.info("Created base appeal:\n{}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEAL_MEDIA_TYPE).type(APPEAL_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation:\n{}Denoted by the URI:\n{}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        LOG.info(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));

        // Submit a review request 
        System.out.println("\n\nStep 2. Submit a review request for the appeal");
        LOG.info(String.format("About to create a review resource at [%s] via PUT", appealRepresentation.getReviewLink().getUri().toString()));
        Link reviewLink = appealRepresentation.getReviewLink();
        LOG.info("Created review link {} for the appeal representation {}", reviewLink, appealRepresentation);
        LOG.info("reviewLink.getRelValue() = {}", reviewLink.getRelValue());
        LOG.info("reviewLink.getUri() = {}", reviewLink.getUri());
        LOG.info("reviewLink.getMediaType() = {}", reviewLink.getMediaType());
        Review review = new Review("1st submission for Review");
        LOG.info("Created new review object {}", review);
        ReviewRepresentation reviewRepresentation = client.resource(reviewLink.getUri()).accept(reviewLink.getMediaType()).type(reviewLink.getMediaType()).put(ReviewRepresentation.class, new ReviewRepresentation(review));
        LOG.info("Created new review representation {}", reviewRepresentation);
        if(reviewRepresentation.getResultLink()!= null)
        LOG.info(String.format("Review submission made, result can be seen at [%s]", reviewRepresentation.getResultLink().getUri().toString()));
        else
        LOG.info("You cannot view the result");
        hangAround(7);
        LOG.info("\n\n Waited for 7 days");
        System.out.println("\n\nStep 3. Check on the Appeal status");
        LOG.info(String.format("About to check appeal status at [%s] via GET", reviewRepresentation.getAppealLink().getUri().toString()));
        Link orderLink = reviewRepresentation.getAppealLink();
        AppealRepresentation finalAppealRepresentation = client.resource(orderLink.getUri()).accept(APPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
        LOG.info(String.format("Current status [%s]", finalAppealRepresentation.getStatus()));
        
        System.out.println("\n\nStep 4. Submitting again the review request");
        review.setUpdateNote("Please Sir!");
        ClientResponse badUpdateResponse = client.resource(reviewLink.getUri()).accept(reviewLink.getMediaType()).type(reviewLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created Bad Update Response {}", badUpdateResponse);
        LOG.info(String.format("outcome [%d],Tried to update with bad ID at [%s] via POST",badUpdateResponse.getStatus(), reviewLink.getUri().toString()));
     
}    
    
private static void BadStartCaseTest(URI serviceUri) throws Exception {
        LOG.info("\n\nStarting BAD START CASE TEST with Service URI {}....................", serviceUri+"/badUri");
        // Place the order
        System.out.println("\nStep 1. Create an appeal");
        LOG.info(String.format("About to start Bad Start Case case test. Placing Appeal at [%s]/badUri via POST", serviceUri.toString()));
        Appeal appeal = new Appeal("Consider my request Sir","CSE564");
        
        LOG.info("Created base appeal:\n{}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        Link badLink = new Link("bad", new RestbucksUri("http://localhost:8080/HATEOAS-Appeals-amuthuk3-Server/webresources/appeal/bad-uri"), APPEAL_MEDIA_TYPE);
        LOG.info("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created Bad Create Response {}", badUpdateResponse);
        LOG.info(String.format("outcome [%d],Tried to start with bad URI at [%s] via POST",badUpdateResponse.getStatus(), badLink.getUri().toString()));
     
}    
    
    
private static void ForgottenCaseTest(URI serviceUri) throws Exception {
    LOG.info(String.format("\n\nStarting FORGOTTEN CASE TEST with Service URI {%s}....................", serviceUri));
        // Place the order
        System.out.println("Step 1. Create an appeal");
        LOG.info(String.format("About to start forgotten case test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal("Consider my request Sir","CSE564");
        
        LOG.info("Created base appeal:\n{}", appeal);
        Client client = Client.create();
        LOG.info("Created client: {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEAL_MEDIA_TYPE).type(APPEAL_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation:\n{}Denoted by the URI:\n{}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        LOG.info(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));

        // Submit a review request 
        System.out.println("\n\nStep 2. Submit a review request for the appeal");
        LOG.info(String.format("About to create a review resource at [%s] via PUT", appealRepresentation.getReviewLink().getUri().toString()));
        Link reviewLink = appealRepresentation.getReviewLink();
        LOG.info("Created review link {} for the appeal representation:\n{}", reviewLink, appealRepresentation);
        LOG.info("reviewLink.getRelValue() = {}", reviewLink.getRelValue());
        LOG.info("reviewLink.getUri() = {}", reviewLink.getUri());
        LOG.info("reviewLink.getMediaType() = {}", reviewLink.getMediaType());
        Review review = new Review("1st submission for Review");
        LOG.info("Created new review object {}", review);
        ReviewRepresentation reviewRepresentation = client.resource(reviewLink.getUri()).accept(reviewLink.getMediaType()).type(reviewLink.getMediaType()).put(ReviewRepresentation.class, new ReviewRepresentation(review));
        LOG.info("Created new review representation {}", reviewRepresentation);
        if(reviewRepresentation.getResultLink()!= null)
        LOG.info(String.format("Review submission made, result can be seen at [%s]", reviewRepresentation.getResultLink().getUri().toString()));
        else
        LOG.info("You cannot view the result as Status: UNREVIEWED");
        hangAround(7);
        LOG.info("\n\n Waited for 7 days");
        System.out.println("\n\nStep 3. Check on the Appeal status");
        LOG.info(String.format("About to check appeal status at [%s] via GET", reviewRepresentation.getAppealLink().getUri().toString()));
        Link orderLink = reviewRepresentation.getAppealLink();
        AppealRepresentation finalAppealRepresentation = client.resource(orderLink.getUri()).accept(APPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
        LOG.info(String.format("Current status [%s]", finalAppealRepresentation.getStatus()));
        
        System.out.println("\n\nStep 4. Submitting again the review request");
        review.setUpdateNote("Please Sir!");
        reviewRepresentation = client.resource(reviewLink.getUri()).accept(reviewLink.getMediaType()).type(reviewLink.getMediaType()).put(ReviewRepresentation.class, new ReviewRepresentation(review));
        if(reviewRepresentation.getResultLink()!= null)
        LOG.info(String.format("Review submission made, result can be seen at [%s]", reviewRepresentation.getResultLink().getUri().toString()));
        else
        LOG.info("You cannot view the result");
        
        System.out.println("\n\nStep 5. Get Result");
        LOG.info(String.format("About to request a result from [%s] via GET", reviewRepresentation.getResultLink().getUri().toString()));
        Link resultLink = reviewRepresentation.getResultLink();
        LOG.info("Retrieved the result link {} for payment represntation {}", resultLink, reviewRepresentation);
        ResultRepresentation resultRepresentation = client.resource(resultLink.getUri()).get(ResultRepresentation.class);
        //        LOG.info(String.format("Review Done, Result: [%f]", resultRepresentation.));
        
        System.out.println("\n\nStep 6. See the result");
        LOG.info(String.format("Trying to view the final result from [%s] via DELETE.", resultRepresentation.getAppealLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(orderLink.getUri()).delete(ClientResponse.class);
        LOG.info(String.format("Tried to view the final result, HTTP status [%d]", finalResponse.getStatus()));
        if (finalResponse.getStatus() == 200) {
            LOG.info(String.format("Appeal status [%s], Have a Nice Day", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
        
}    
    
private static void AbandonCaseTest(URI serviceUri) throws Exception {
 
    LOG.info(String.format("\n\nStarting ABANDON CASE TEST with Service URI [%s]....................", serviceUri));
        // Place the order
        System.out.println("\nStep 1. Create an appeal");
        LOG.info(String.format("About to start abandon case test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = new Appeal("Please consider my request Sir","CSE564");
        
        LOG.info("Created base appeal:\n{}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEAL_MEDIA_TYPE).type(APPEAL_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation:\n{}Denoted by the URI:\n{}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        LOG.info(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));

        System.out.println("\nStep 2. Delete the appeal");
        LOG.info("About to abandon");
        Link cancelLink = appealRepresentation.getCancelLink();
        ClientResponse finalResponse = client.resource(cancelLink.getUri()).delete(ClientResponse.class);
         if (finalResponse.getStatus() == 200) {
            LOG.info(String.format("Appeal Abandoned [%s], Have a Nice Day", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
    
        
}
    

    
}
