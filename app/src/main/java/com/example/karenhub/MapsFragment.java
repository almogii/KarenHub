package com.example.karenhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.karenhub.databinding.FragmentMapsBinding;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final float DEFAULT_ZOOM = 15f;
    //Data.M
    private MapsFragmentModel mapsFragmentModel;
    private MapView mMapView;
    private SearchView searchView;
    private MaterialButton currentLocBtn;
    private GoogleMap map;
    Geocoder geocoder;
    private Boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private LatLng lastLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Bundle savedInstanceState;
    FragmentMapsBinding binding;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        geocoder = new Geocoder(getContext());
        init();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng));
                    map.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), 2000, null);
                }
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//     View   nView= inflater.inflate(R.layout.fragment_maps, container, false);

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View nView = binding.getRoot();

        mMapView = nView.findViewById(R.id.map);
        searchView = nView.findViewById(R.id.idSearchView);
        this.savedInstanceState = savedInstanceState;
        currentLocBtn = nView.findViewById(R.id.current_loc_btn);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            map.moveCamera(this.savedInstanceState.getParcelable(KEY_CAMERA_POSITION));
        }
        initGoogleMap(this.savedInstanceState);
        /*binding.idSearchView.setOnClickListener(view -> {
            NavDirections action = MapsFragmentDirections.actionMapsFragmentToAddNewPostFragment(lastLatLng);
            Navigation.findNavController(view).navigate(action);

        });*/
        return nView;
    }

    private void init() {

        map.setOnMapClickListener((map_click) -> {
            lastLatLng = new LatLng(map_click.latitude, map_click.longitude);
            String locationName = lastLatLng.toString();
            try {
                List<Address> address = geocoder.getFromLocation(map_click.latitude, map_click.longitude, 1);
                if (address.size() >= 1) {
                    locationName = address.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeMarker(locationName);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList;
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    if (addressList.size() >= 1) {
                        Address address = addressList.get(0);
                        lastLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                        changeMarker(address.getAddressLine(0));
                    } else {
                        Toast.makeText(getContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });
        currentLocBtn.setOnClickListener((btn_click) -> {
            getCurrentLocation();
            String locationName = lastLatLng.toString();
            try {
                List<Address> address = geocoder.getFromLocation(lastLatLng.latitude, lastLatLng.longitude, 1);
                if (address.size() >= 1) {
                    locationName = address.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeMarker(locationName);
        });
    }

    private void changeMarker(String title) {
        map.clear();
        map.addMarker(new MarkerOptions().position(lastLatLng).title(title));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, DEFAULT_ZOOM));
        MarkerOptions marker = new MarkerOptions()
                .position(lastLatLng)
                .title(title);
        map.clear();
        map.addMarker(marker);
        savedInstanceState = new Bundle();
        savedInstanceState.putParcelable("location", lastLatLng);
        savedInstanceState.putString("locationName", title);
        this.mapsFragmentModel.setSavedInstanceStateData(savedInstanceState);
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(getActivity());
        this.mapsFragmentModel = viewModelProvider.get(MapsFragmentModel.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastLatLng);
        }
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

}