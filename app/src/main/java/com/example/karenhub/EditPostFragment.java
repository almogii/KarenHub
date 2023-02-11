package com.example.karenhub;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karenhub.databinding.FragmentEditPostBinding;
import com.example.karenhub.model.Model;
import com.example.karenhub.model.Post;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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


    public static EditPostFragment newInstance() {
        EditPostFragment newEditPostFragment = new EditPostFragment();
        Bundle bundle = new Bundle();
        newEditPostFragment.setArguments(bundle);
        return newEditPostFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //location btn
        binding.addLoctionEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.mapsFragment,savedInstanceState);
            }
        });
        //show previous post details
        if(title!=null){
            binding.editpostTitle.setText(title);
        }
        if (details!=null){
            binding.editpostDescription.setText(details);
        }
        if (!imgUrl.isEmpty()){
            Picasso.get().load(imgUrl).into(binding.avatarImgEditPost);
        }


        //save btn
        binding.saveEditPost.setOnClickListener(view1 -> {
            String editedTitle=binding.editpostTitle.getText().toString();
            String editedDetails=binding.editpostDescription.getText().toString();
            String editedLocation=binding.addresseditpost.getText().toString();
            String editedLabel=sp.getString("label","");
                Log.d("title",editedTitle);
                Log.d("id",id);

            if(editedTitle!=null && editedTitle!=title){
                updates.put("title",editedTitle);
            }
            if(editedDetails!=null&&editedDetails!=details){
                updates.put("details",editedDetails);
            }
//            if(!editedLocation.equals(locationName)){
//                updates.put("location",editedLocation);
//            }
            updatePostByid(id);
        });


        return view;
    }

    public void updatePostByid(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("posts");
        collRef.whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference docRef = documentSnapshot.getReference();
//                            Map<String, Object> updates = new HashMap<>();
//                            updates.put("field_name", newValue);
                            docRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG1", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG2", "Error updating document", e);
                                        }
                                    });
                        }
                    }
                });

    }
}
