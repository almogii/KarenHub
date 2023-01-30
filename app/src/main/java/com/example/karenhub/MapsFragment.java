//package com.example.karenhub;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MapsFragment extends Fragment  implements OnMapReadyCallback{
//    private MapView mMapView;
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//     View   nView= inflater.inflate(R.layout.fragment_maps, container, false);
//        mMapView = nView.findViewById(R.id.map);
//        return nView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mapView=(MapView) nView.findViewById(R.id.map);
//        if(mapView!=null){
//            mapView.onCreate(null);
//            mapView.onResume();
//            mapView.getMapAsync(this);
//
//        }
//    }
//
//    @Override
//    public void onMapReady( GoogleMap googleMap) {
//        MapsInitializer.initialize(getContext());
//        googlemap=googleMap;
//
//
//    }
//}