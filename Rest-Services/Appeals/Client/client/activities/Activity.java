package com.appealing.client.activities;

import com.appealing.client.network.HttpBinding;

public abstract class Activity {
    
    protected final HttpBinding binding = new HttpBinding();
    protected Actions actions;
    
    protected Actions noFurtherActivities() {
        return new Actions();
    }
    
    protected Actions retryCurrentActivity() {
        Actions actions = new Actions();
        actions.add(this);
        return actions;
    }
    
    public Actions getActions() {
        return actions;
    }
}
