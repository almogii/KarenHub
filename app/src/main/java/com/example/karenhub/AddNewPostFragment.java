package com.example.karenhub;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karenhub.databinding.FragmentAddPostBinding;
import com.example.karenhub.model.Model;
import com.example.karenhub.model.Post;
import com.google.android.gms.maps.model.LatLng;

public class AddNewPostFragment extends Fragment {
    FragmentAddPostBinding binding;
    LatLng location;
    String locationName;
    Double x, y;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    SharedPreferences sp;
    Boolean isAvatarSelected = false;

    public static AddNewPostFragment newInstance(LatLng location, String locationName) {
        AddNewPostFragment newPostFragment = new AddNewPostFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", location);
        bundle.putString("locationName", locationName);
        newPostFragment.setArguments(bundle);
        return newPostFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
        Bundle bundle = getArguments();
        if (!bundle.isEmpty()) {
            this.location = bundle.getParcelable("location");
            this.locationName = bundle.getString("locationName");
        }

        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addNewPostFragment);
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
                    binding.avatarImg.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    binding.avatarImg.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (this.locationName != null) {
            binding.address.setText(locationName);
        }

        binding.addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.mapsFragment, savedInstanceState);
            }
        });
        binding.saveBtn.setOnClickListener(view1 -> {

            String title = binding.postTitle.getText().toString();
            String details = binding.postDes.getText().toString();
            String location = binding.address.getText().toString();

            String label=sp.getString("label","");

            Post post = new Post(title,title, "", details,  location,label);
            if (details.equals("") || title.equals("")) {
                Toast.makeText(getContext(), "missing title or details ", Toast.LENGTH_LONG).show();
            } else {
                if (isAvatarSelected) {
                    binding.avatarImg.setDrawingCacheEnabled(true);
                    binding.avatarImg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.avatarImg.getDrawable()).getBitmap();
                    Model.instance().uploadImage(title, bitmap, url -> {
                        if (url != null) {
                            post.setImgUrl(url);
                        }
                        Model.instance().addPost(post, (unused) -> {
                            Navigation.findNavController(view1).popBackStack();
                        });
                    });
                } else {
                    Model.instance().addPost(post, (unused) -> {
                        Navigation.findNavController(view1).navigate(R.id.postsListFragment);
                    });
                }
            }
        });
        binding.cancellBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.postsListFragment, false));
        binding.cameraButton.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });
        binding.galleryButton.setOnClickListener(view1 -> {
            galleryLauncher.launch("media/*");
        });

        return view;
    }

}