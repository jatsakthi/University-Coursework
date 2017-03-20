package com.appealing.model;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppealBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealBuilder.class);
    
    public static AppealBuilder appeal() {
        return new AppealBuilder();
    }
    private String content = "";
    private String item = "";
    private AppealStatus status = AppealStatus.UNREVIEWED;
    
    private void defaultItems() {
        LOG.debug("Executing AppealBuilder.defaultItems");
        this.item = "";
    }
    
    
    public Appeal build() {
        LOG.debug("Executing OrderBuilder.build");
        return new Appeal(status,content,item);
    }

    public AppealBuilder withItem(String item) {
        LOG.debug("Executing OrderBuilder.withItem");
        this.item = item;
        return this;
    }
    
    public AppealBuilder withStatus(AppealStatus status) {
        LOG.debug("Executing AppealBuilder.withRandomItems");
        this.status = status;
        return this;
    }

    public AppealBuilder withRandomItems() {
        LOG.debug("Executing AppealBuilder.withRandomItems");
        return this;
    }

}
