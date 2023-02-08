package com.example.karenhub;

import android.os.Bundle;
import androidx.lifecycle.ViewModel;

public class MapsFragmentModel extends ViewModel {
    private Bundle savedInstanceStateData;

    public Bundle getSavedInstanceStateData() {
        return savedInstanceStateData;
    }

    public void setSavedInstanceStateData(Bundle savedInstanceStateData) {
        this.savedInstanceStateData = savedInstanceStateData;
    }
}
