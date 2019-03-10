package com.appealing.activities;

import com.appealing.representations.RestbucksUri;

public class UriExchangeAppeal {

    public static RestbucksUri reviewForAppeal(RestbucksUri appealUri) {
        checkForValidAppealUri(appealUri);
        return new RestbucksUri(appealUri.getBaseUri() + "/review/" + appealUri.getId().toString());
    }
    
    public static RestbucksUri appealForReview(RestbucksUri reviewUri) {
        checkForValidReviewUri(reviewUri);
        return new RestbucksUri(reviewUri.getBaseUri() + "/appeal/" + reviewUri.getId().toString());
    }

    public static RestbucksUri resultForReview(RestbucksUri reviewUri) {
        checkForValidReviewUri(reviewUri);
        return new RestbucksUri(reviewUri.getBaseUri() + "/result/" + reviewUri.getId().toString());
    }
    
    public static RestbucksUri appealForResult(RestbucksUri resultUri) {
        checkForValidResultUri(resultUri);
        return new RestbucksUri(resultUri.getBaseUri() + "/appeal/" + resultUri.getId().toString());
    }

    private static void checkForValidAppealUri(RestbucksUri appealUri) {
        if(!appealUri.toString().contains("/appeal/")) {
            throw new RuntimeException("Invalid Appeal URI");
        }
    }
    
    private static void checkForValidReviewUri(RestbucksUri review) {
        if(!review.toString().contains("/review/")) {
            throw new RuntimeException("Invalid Review URI");
        }
    }
    
    private static void checkForValidResultUri(RestbucksUri result) {
        if(!result.toString().contains("/result/")) {
            throw new RuntimeException("Invalid Result URI");
        }
    }
}
