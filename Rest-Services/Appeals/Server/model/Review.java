package com.appealing.model;

import org.joda.time.DateTime;

public class Review {

    private DateTime reviewDate;

    private String updateNote;
    public Review(String updateNote) {
        this.updateNote = updateNote;
        reviewDate = new DateTime();
    }
    public String getUpdateNote() {
        return updateNote;
    }

    public void setUpdateNote(String updateNote) {
        this.updateNote = updateNote;
    }
    public DateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(DateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
