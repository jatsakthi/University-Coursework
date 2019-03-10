package com.appealing.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appealing.activities.InvalidReviewException;
import com.appealing.activities.NoSuchAppealException;
import com.appealing.activities.ReviewActivity;
import com.appealing.activities.UpdateException;
import com.appealing.model.Identifier;
import com.appealing.representations.Link;
import com.appealing.representations.ReviewRepresentation;
import com.appealing.representations.Representation;
import com.appealing.representations.RestbucksUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/review/{reviewId}")
public class ReviewResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReviewResource.class);
    
    private @Context UriInfo uriInfo;
    
    public ReviewResource(){
        LOG.info("Review Resource Constructor");
    }
    
    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * @param uriInfo
     */
    public ReviewResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @PUT
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response review(ReviewRepresentation reviewRepresentation) {
        LOG.info("Making a new review");
        
        Response response;
        
        try {
            response = Response.created(uriInfo.getRequestUri()).entity(
                    new ReviewActivity().review(reviewRepresentation.getReview(), 
                            new RestbucksUri(uriInfo.getRequestUri()))).build();
        } catch(NoSuchAppealException nsoe) {
            LOG.debug("No appeal for review {}", reviewRepresentation);
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Invalid update to review {}", reviewRepresentation);
            Identifier identifier = new RestbucksUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(Representation.SELF_REL_VALUE, new RestbucksUri(uriInfo.getBaseUri().toString() + "appeal/" + identifier));
            response = Response.status(Status.FORBIDDEN).entity(link).build();
        } catch(InvalidReviewException ipe) {
            LOG.debug("Invalid Review for Appeal");
            response = Response.status(Status.BAD_REQUEST).build();
        } catch(Exception e) {
            LOG.debug("Someting when wrong with processing review");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Created the new Review activity {}", response);
        
        return response;
    }
}
