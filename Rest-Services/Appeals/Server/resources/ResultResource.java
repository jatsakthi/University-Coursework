package com.appealing.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appealing.activities.CompleteAppealActivity;
import com.appealing.activities.NoSuchAppealException;
import com.appealing.activities.AppealAlreadyCompletedException;
import com.appealing.activities.AppealNotReviewedException;
import com.appealing.activities.ReadResultActivity;
import com.appealing.model.Identifier;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.ResultRepresentation;
import com.appealing.representations.RestbucksUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/result")
public class ResultResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(ResultResource.class);

    private @Context
    UriInfo uriInfo;

    public ResultResource() {
        LOG.info("Result Resource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public ResultResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    @GET
    @Path("/{appealId}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response getResult() {
        LOG.info("Retrieving a  Result Resource");
        
        Response response;
        
        try {
            ResultRepresentation responseRepresentation = new ReadResultActivity().read(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.debug("Appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (AppealNotReviewedException onpe) {
            LOG.debug("Appeal not paid");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        }
        
        LOG.debug("The responce for the retrieve Result request is {}", response);
        
        return response;
    }
    
    @DELETE
    @Path("/{appealId}")
    public Response completeAppeal(@PathParam("appealId")String identifier) {
        LOG.info("Retrieving a  Result Resource");
        
        Response response;
        
        try {
            AppealRepresentation finalizedAppealRepresentation = new CompleteAppealActivity().completeAppeal(new Identifier(identifier));
            response = Response.status(Status.OK).entity(finalizedAppealRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.debug("Appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such Appeal");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (AppealNotReviewedException onpe) {
            LOG.debug("Appeal not reviewed ");
            response = Response.status(Status.CONFLICT).build();
        }
        
        LOG.debug("The response for the delete result request is {}", response);
        
        return response;
    }
}
