package com.appealing.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appealing.activities.RemoveAppealActivity;
import com.appealing.activities.CreateAppealActivity;
import com.appealing.activities.InvalidAppealException;
import com.appealing.activities.NoSuchAppealException;
import com.appealing.activities.AppealDeletionException;
import com.appealing.activities.ReadAppealActivity;
import com.appealing.activities.UpdateException;
import com.appealing.activities.UpdateAppealActivity;
import com.appealing.representations.AppealRepresentation;
import com.appealing.representations.RestbucksUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appealing.representations.AppealRepresentation;
@Path("/appeal")
public class AppealResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealResource.class);

    private @Context UriInfo uriInfo;

    public AppealResource() {
        LOG.info("AppealResource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public AppealResource(UriInfo uriInfo) {
        LOG.info("AppealResource constructor with mock uriInfo {}", uriInfo);
        this.uriInfo = uriInfo;  
    }
    
    @GET
    @Path("/{appealId}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response getAppeal() {
        LOG.info("Retrieving an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new ReadAppealActivity().retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch(NoSuchAppealException nsoe) {
            LOG.debug("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong retriveing the appeal");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Retrieved the appeal resource", response);
        
        return response;
    }
    
    @POST
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response createAppeal(String appealRepresentation) {
        LOG.info("Creating an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new CreateAppealActivity().create(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getUpdateLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid Order - Problem with the appealRepresentation {}", appealRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.debug("Someting went wrong creating the appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Resulting response for creating the appeal resource is {}", response);
        
        return response;
    }

    @DELETE
    @Path("/{orderId}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response removeOrder() {
        LOG.info("Removing an Order Reource");
        
        Response response;
        
        try {
            AppealRepresentation removedOrder = new RemoveAppealActivity().delete(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(removedOrder).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such Appeal resource to delete");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(AppealDeletionException ode) {
            LOG.debug("Problem deleting Appeal resource");
            response = Response.status(Status.METHOD_NOT_ALLOWED).header("Allow", "GET").build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong deleting the Appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.debug("Resulting response for deleting the Appeal resource is {}", response);
        
        return response;
    }

    @POST
    @Path("/{orderId}")
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response updateAppeal(String appealRepresentation) {
        LOG.info("Updating an Order Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new UpdateAppealActivity().update(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid Appeal in the XML representation {}", appealRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such Appeal resource to update");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the Appeal resource");
            response = Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong updating the Appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } 
        
        LOG.debug("Resulting response for updating the Appeal resource is {}", response);
        
        return response;
     }
}
