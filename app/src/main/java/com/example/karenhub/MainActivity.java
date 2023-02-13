package com.example.karenhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    int fragment_state = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

        BottomNavigationView navView = findViewById(R.id.main_bottomNavigationView);
        NavigationUI.setupWithNavController(navView,navController);

        navController.addOnDestinationChangedListener((navController,navDestination,bundle)->{
            if(navDestination.getLabel().equals("Set Location")){
                fragment_state = 1;
                navView.getMenu().clear();
                navView.inflateMenu(R.menu.bar_map);
                MenuItem item = navView.getMenu().findItem(R.id.save_location);
                item.setOnMenuItemClickListener((i)->{
                    ViewModelProvider viewModelProvider = new ViewModelProvider(this);
                    MapsFragmentModel viewModel = viewModelProvider.get(MapsFragmentModel.class);
                    Bundle savedInstanceStateData = viewModel.getSavedInstanceStateData();
                    LatLng location = savedInstanceStateData.getParcelable("locationTemp");
                    String locationName  = savedInstanceStateData.getString("locationNameTemp");
                    savedInstanceStateData.putParcelable("location",location);
                    savedInstanceStateData.putString("locationName",locationName);
                    viewModel.setSavedInstanceStateData(savedInstanceStateData);
                    /*MapsFragmentDirections.ActionMapsFragmentToAddNewPostFragment action =
                            MapsFragmentDirections.actionMapsFragmentToAddNewPostFragment(location,locationName);*/
                    navController.popBackStack();
                    //navController.navigate((NavDirections) action);
                    return true;
                });
                NavigationUI.setupWithNavController(navView,navController);
            } else if(fragment_state == 1){
                fragment_state = 0;
                navView.getMenu().clear();
                navView.inflateMenu(R.menu.bar_menu);
                NavigationUI.setupWithNavController(navView,navController);
            } else {
                fragment_state = 0;
            }
        });
    }

    int fragmentMenuId = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if (fragmentMenuId != 0){
            menu.removeItem(fragmentMenuId);
        }
        fragmentMenuId = 0;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){

            navController.popBackStack();
        }else{
            fragmentMenuId = item.getItemId();
            return NavigationUI.onNavDestinationSelected(item,navController);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        int actionBarHeight = getSupportActionBar().getHeight();

        ViewGroup topLevelLayout = findViewById(R.id.main_navhost);
        topLevelLayout.setPadding(0, actionBarHeight + statusBarHeight, 0, 0);
        getSupportActionBar().hide();
    }
}