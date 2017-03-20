package com.appealing.representations;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.DateTime;

import com.appealing.model.Review;

@XmlRootElement(name = "result", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ResultRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(ResultRepresentation.class);

    @XmlElement(name = "note", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String resultNote;
    @XmlElement(name = "date", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String resultDate;
    
    ResultRepresentation(){
        LOG.debug("In ReceiptRepresentation Constrictor");
    } // For JAXB :-(
    
    public ResultRepresentation(Review review, Link orderLink) {
        LOG.info("Creating an Result Representation with the request = {} and links = {}", review, links);
        this.resultNote = review.getUpdateNote();
        this.resultDate = review.getReviewDate().toString();
        
        this.links = new ArrayList<Link>();
        links.add(orderLink);
        
        LOG.debug("Created the Receipt Representation {}", this);
    }

    public DateTime getresultDate() {
        return new DateTime(resultDate);
    }
    
    public String getresultNote() {
        return resultNote;
    }

    public Link getAppealLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeal");
    }
    
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(ResultRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
