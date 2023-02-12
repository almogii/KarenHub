package com.example.karenhub;

import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {
    private Boolean activeState = false;

    public Boolean getActiveState() { return activeState; }

    public void setActiveState(Boolean active) {
        this.activeState = active;
    }
}