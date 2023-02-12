package com.example.karenhub;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class PostFragment extends Fragment {
    TextView titleTv,detailsTv,locationTV,labelTV;
    String title;
    String details;
    String  location;
    String imgUrl;
    String label;
    ImageView image;
    String id;
    SharedPreferences sp;

    public static PostFragment newInstance(String title, String details, String location, String ImgUrl, String label,String id){
        PostFragment frag = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE",title);
        bundle.putString("DETAILS",details);
        bundle.putString("LOCATION",location);
        bundle.putString("IMAGE",ImgUrl);
        bundle.putString("LABEL",label);
        bundle.putString("ID",id);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.title = bundle.getString("TITLE");
            this.details=bundle.getString("DETAILS");
            this.location=bundle.getString("LOCATION");
            this.imgUrl=bundle.getString("IMAGE");
            this.label=bundle.getString("LABEL");
            this.id=bundle.getString("ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        View button = view.findViewById(R.id.editBtn_postFrag);
        button.setVisibility(View.INVISIBLE);
        //show post details
        title = PostFragmentArgs.fromBundle(getArguments()).getPostTitle();
        details=PostFragmentArgs.fromBundle(getArguments()).getPostDetails();
        location=PostFragmentArgs.fromBundle(getArguments()).getPostLocInfo();
        imgUrl=PostFragmentArgs.fromBundle(getArguments()).getPostImgUrl();
       label=PostFragmentArgs.fromBundle(getArguments()) .getPostLabel();
       id=PostFragmentArgs.fromBundle(getArguments()).getPostId();

        TextView titleTv = view.findViewById(R.id.postfrag_title_tv);
        if (title != null){titleTv.setText(title);}
        detailsTv=view.findViewById(R.id.postDetails_tv);
        if(details!=null){detailsTv.setText(details);}
        locationTV=view.findViewById(R.id.postLocation);
        if(location!=null){locationTV.setText(location);}
        image=view.findViewById(R.id.postUrl_blueFrag);
        if(!imgUrl.isEmpty()){
            Picasso.get().load(imgUrl).into(image);
        }
        labelTV=view.findViewById(R.id.labelTv);
        if(label!=null){
            labelTV.setText(label);
        }

        //check if user has permissions
        String currUserLabel= sp.getString("label","");
       if(currUserLabel.equals(label)){
           button.setVisibility(View.VISIBLE);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   PostFragmentDirections.ActionPostFragmentToEditPostFragment action=PostFragmentDirections.actionPostFragmentToEditPostFragment( new LatLng(0,0),location,id,title,details,label,imgUrl);
                   Navigation.findNavController(view).navigate((NavDirections) action);
               }
           });
       }
        return view;
    }
    public void setTitle(String title) {
        this.title = title;
        if (titleTv != null){
            titleTv.setText(title);
        }
 }
}