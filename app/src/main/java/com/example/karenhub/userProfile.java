package com.example.karenhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class userProfile extends Fragment {

    private UserProfileViewModel mViewModel;
    private BottomNavigationView bottomNavigationView;

    public static userProfile newInstance() {
        return new userProfile();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        bottomNavigationView = getActivity().findViewById(R.id.main_bottomNavigationView);
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.userProfile);
        menuItem.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.userProfile);
        menuItem.setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.userProfile);
        menuItem.setEnabled(true);
    }
}