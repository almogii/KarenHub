package com.example.karenhub;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karenhub.databinding.FragmentEditPostBinding;
import com.example.karenhub.model.Model;
import com.example.karenhub.model.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class EditPostFragment extends Fragment {
    FragmentEditPostBinding binding;
    LatLng location;
    String locationName;
    String title;
    String details;
    String imgUrl;
    String label;
    String id;
    Map<String, Object> updates;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    SharedPreferences sp;
    Boolean isAvatarSelected = false;
    private BottomNavigationView bottomNavigationView;
    ViewModelProvider viewModelProvider;
    MapsFragmentModel viewModel;
    UserProfileViewModel userViewModel;


    public static EditPostFragment newInstance() {
        EditPostFragment newEditPostFragment = new EditPostFragment();
        Bundle bundle = new Bundle();
        newEditPostFragment.setArguments(bundle);
        return newEditPostFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = getActivity().findViewById(R.id.main_bottomNavigationView);
        sp = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        updates=  new HashMap<>();
        Bundle bundle = getArguments();
        if (!bundle.isEmpty()) {
            this.location = bundle.getParcelable("location");
            this.locationName = bundle.getString("locationName");
        }
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.editPostFragment);
            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    viewModelProvider = new ViewModelProvider(getActivity());
                    MapsFragmentModel viewModel = viewModelProvider.get(MapsFragmentModel.class);
                    binding.avatarImgEditPost.setImageBitmap(result);
                    Bundle bundle = new Bundle();
                    if(viewModel.getSavedInstanceStateData() != null){
                        bundle = viewModel.getSavedInstanceStateData();
                    }
                    bundle.putParcelable("imgBitmap",result);
                    viewModel.setSavedInstanceStateData(bundle);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    binding.avatarImgEditPost.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentEditPostBinding.inflate(inflater,container,false);
        id=requireArguments().getString("postId");
        imgUrl=requireArguments().getString("editImgUrl");
        location=requireArguments().getParcelable("location");
        locationName=requireArguments().getString("locationName");
        label=requireArguments().getString("editLabel");
        title=requireArguments().getString("EditTitle");
        details=requireArguments().getString("Editdetails");
        View view=binding.getRoot();
        viewModelProvider = new ViewModelProvider(getActivity());
        viewModel = viewModelProvider.get(MapsFragmentModel.class);
        if (this.locationName != null) {
            binding.addresseditpost.setText(locationName);
        }
        //show previous post details
        if(title!=null){
            binding.editpostTitle.setText(title);
        }
        if (details!=null){
            binding.editpostDescription.setText(details);
        }
        if (imgUrl != null && !imgUrl.equals("")){
            Picasso.get().load(imgUrl).into(binding.avatarImgEditPost);
        }
        if(locationName!=null){
            binding.addresseditpost.setText(locationName);
        }
        binding.addLoctionEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.mapsFragment,savedInstanceState);
            }
        });

        //save btn
        binding.saveEditPost.setOnClickListener(view1 -> {
            String editedTitle=binding.editpostTitle.getText().toString();
            String editedDetails=binding.editpostDescription.getText().toString();
            String editedLocation=binding.addresseditpost.getText().toString();

            if(editedTitle!=null && editedTitle!=title){
                updates.put("title",editedTitle);
            }
            if(editedDetails!=null&&editedDetails!=details){
                updates.put("details",editedDetails);
            }
            if(!editedLocation.isEmpty() ||locationName!=null){
                updates.put("location",editedLocation);
            }
            if (isAvatarSelected) {
                binding.avatarImgEditPost.setDrawingCacheEnabled(true);
                binding.avatarImgEditPost.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.avatarImgEditPost.getDrawable()).getBitmap();
                Model.instance().uploadImage(id, bitmap, url -> {
                    if (url != null) {
                        updates.put("image",url);
                        this.imgUrl = url;
                        Model.instance().updatePostById(id,updates);
                    }
                });
            } else {
                Model.instance().updatePostById(id,updates);
            }

            viewModelProvider = new ViewModelProvider(getActivity());
            userViewModel = viewModelProvider.get(UserProfileViewModel.class);
            getActivity().finish();
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                            getContext(), android.R.anim.fade_in, android.R.anim.fade_out)
                    .toBundle();
            startActivity(getActivity().getIntent(),bundle);
            if (userViewModel.getActiveState()){
                Navigation.findNavController(view).navigate(R.id.userProfile);
            }
        });
        binding.cancelBtnEditPost.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.postFragment, false));
        binding.imageBtnEditPost.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });
        binding.galleryBtnEditPost.setOnClickListener(view1 -> {
            galleryLauncher.launch("media/*");
        });
    return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStop() {
        super.onStop();
        bottomNavigationView.setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(true);
        viewModel.setSavedInstanceStateData(new Bundle());
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewModelProvider viewModelProvider = new ViewModelProvider(getActivity());
        MapsFragmentModel viewModel = viewModelProvider.get(MapsFragmentModel.class);
        Bundle savedInstanceStateData = viewModel.getSavedInstanceStateData();
        if(savedInstanceStateData != null) {
            this.location = viewModel.getSavedInstanceStateData().getParcelable("location");
            this.locationName = viewModel.getSavedInstanceStateData().getString("locationName");
            if(locationName != null) {
                binding.addresseditpost.setText(locationName);
            }
            Bitmap bitmap = viewModel.getSavedInstanceStateData().getParcelable("imgBitmap");
            if (bitmap != null){
                binding.avatarImgEditPost.setImageBitmap(bitmap);
            }
        } else {
            viewModel.setSavedInstanceStateData(new Bundle());
        }
    }
}
