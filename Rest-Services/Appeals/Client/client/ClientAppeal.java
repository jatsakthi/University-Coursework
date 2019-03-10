package com.appealing.client;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appealing.model.Appeal;
import com.appealing.model.AppealStatus;
import com.appealing.representations.Representation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "content", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String content;
    @XmlElement(name = "item", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String item;
    @XmlElement(name = "status", namespace = Representation.RESTBUCKS_NAMESPACE)
    private AppealStatus status;

    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.debug("Executing ClientAppeal constructor");
        this.content = appeal.getContent();
        this.item = appeal.getItem();
        this.status = appeal.getStatus();
    }
    
    public Appeal getAppeal() {
        LOG.debug("Executing ClientAppeal.getAppeal");
        return new Appeal(status, content, item);
    }
    
    public String getContent() {
        LOG.debug("Executing ClientAppeal.getContent");
        return content;
    }
    public String getItem() {
        LOG.debug("Executing ClientAppeal.getItem");
        return item;
    }
    
    public AppealStatus getStatus(){
        LOG.debug("Executing ClientAppeal.getStatus");
        return status;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}