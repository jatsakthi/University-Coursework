package com.appealing.model;

import javax.xml.bind.annotation.XmlTransient;

public class Appeal {
    
    private String content;

    private String item;

    @XmlTransient
    private AppealStatus status = AppealStatus.UNREVIEWED;

    public Appeal(String content, String item) {
      this(AppealStatus.UNREVIEWED, content, item);
    }
    

    public Appeal(AppealStatus status, String content, String item) {
        this.content = content;
        this.item = item;
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }    
    
    public void setStatus(AppealStatus status) {
        this.status = status;
    }

    public AppealStatus getStatus() {
        return status;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: " + item + "\n");
        sb.append("Content: " + content + "\n");
        sb.append("Status: " + status + "\n");
        return sb.toString();
    }
}