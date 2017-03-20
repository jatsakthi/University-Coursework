package com.appealing.representations;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appealing.activities.UriExchangeAppeal;
import com.appealing.model.AppealStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appealing.model.Appeal;
import com.appealing.activities.InvalidAppealException;

@XmlRootElement(name = "appeal", namespace = Representation.RESTBUCKS_NAMESPACE)
public class AppealRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "content", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String content;
    @XmlElement(name = "item", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String item;
    @XmlElement(name = "status", namespace = Representation.RESTBUCKS_NAMESPACE)
    private AppealStatus status;

    /**
     * For JAXB :-(
     */
    AppealRepresentation() {
        LOG.debug("In AppealRepresentation Constructor");
    }

    public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseOrderRepresentation(Appeal appeal, RestbucksUri appealUri) {
        LOG.info("Creating a Response Order for order = {} and order URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation;     
        
        RestbucksUri reviewUri = new RestbucksUri(appealUri.getBaseUri() + "/review/" + appealUri.getId().toString());
        LOG.debug("Review URI = {}", reviewUri);
        
        if(appeal.getStatus() == AppealStatus.UNREVIEWED) {
            LOG.debug("The appeal status is {}", AppealStatus.UNREVIEWED);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "cancel", appealUri), 
                    new Link(RELATIONS_URI + "review", reviewUri), 
                    new Link(RELATIONS_URI + "update", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.REVIEWING) {
            LOG.debug("The appeal status is {}", AppealStatus.REVIEWING);
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.POSITIVE || appeal.getStatus() == AppealStatus.NEGATIVE) {
            LOG.debug("The appeal status is {}", appeal.getStatus());
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.RELATIONS_URI + "result", UriExchangeAppeal.resultForReview(reviewUri)));
        }
        else{
            LOG.debug("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown Appeal Status");
        }
        
        LOG.debug("The order representation created for the Create Response Order Representation is {}", appealRepresentation);
        
        return appealRepresentation;
    }

    public AppealRepresentation(Appeal appeal, Link... links) {
        LOG.info("Creating an Appeal Representation for appeal = {} and links = {}", appeal.toString(), links.toString());
        
        try {
            this.content = appeal.getContent();
            this.item = appeal.getItem();
            this.status = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
        LOG.debug("Created the ReviewRepresentation {}", this);
    }

    @Override
    public String toString() {
        LOG.info("Converting Appeal Representation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        LOG.info("Retrieving the Appeal Representation");
        LOG.debug("Content = {}", content);
        LOG.debug("Item = {}", item);
        LOG.debug("Status = {}", status);
        if (content == null || item == null) {
            throw new InvalidAppealException();
        }
       
        Appeal appeal = new Appeal(content, item);
        
        LOG.debug("Retrieving the Appeal Representation {}", appeal);

        return appeal;
    }

    public Link getCancelLink() {
        LOG.info("Retrieving the Cancel link ");
        return getLinkByName(RELATIONS_URI + "cancel");
    }

    public Link getReviewLink() {
        LOG.info("Retrieving the Review link ");
        return getLinkByName(RELATIONS_URI + "review");
    }

    public Link getUpdateLink() {
        LOG.info("Retrieving the Update link ");
        return getLinkByName(RELATIONS_URI + "update");
    }

    public Link getSelfLink() {
        LOG.info("Retrieving the Self link ");
        return getLinkByName("self");
    }
    
    public AppealStatus getStatus() {
        LOG.info("Retrieving the appeal status {}", status);
        return status;
    }

}
