package com.appealing.model;

public class ReviewBuilder {
    
    private double amount = 10.0f;
    private String cardholderName = "A. N. Other";
    private String cardNumber = "123456789";
    private int expiryMonth = 12;
    private int expiryYear = 12;
    
    public static ReviewBuilder payment() {
        return new ReviewBuilder();
    }
    
    public ReviewBuilder withAmount(double amount) {
        if(amount >= 0.0f) {
            this.amount = amount;
        }
        return this;
    }
    
    public ReviewBuilder withCardholderName(String name) {
        this.cardholderName = name;
        return this;
    }
    
    public ReviewBuilder withCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }
    
    public ReviewBuilder withExpiryMonth(int month) {
        if(month > 0 && month < 13) {
            this.expiryMonth= month;
        }
        return this;
    }
    
    public ReviewBuilder withExpiryYear(int year) {
        if(year >= 0) {
            this.expiryYear= year;
        }
        return this;
    }

    public Review build() {
        return new Review("Review Note");
    }
}
