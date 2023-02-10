package com.example.karenhub;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

public class BlueFragment extends Fragment {
    TextView titleTv,detailsTv,locationTV;
    String title;
    String details;
    String  location;
    String imgUrl;
    ImageView image;

    public static BlueFragment newInstance(String title,String details,String location,String ImgUrl){
        BlueFragment frag = new BlueFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE",title);
        bundle.putString("DETAILS",details);
        bundle.putString("LOCATION",location);
        bundle.putString("IMAGE",ImgUrl);

        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.title = bundle.getString("TITLE");
            this.details=bundle.getString("DETAILS");
            this.location=bundle.getString("LOCATION");
            this.imgUrl=bundle.getString("IMAGE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blue, container, false);
        //show post details
        title = BlueFragmentArgs.fromBundle(getArguments()).getPostTitle();
        details=BlueFragmentArgs.fromBundle(getArguments()).getPostDetails();
        location=BlueFragmentArgs.fromBundle(getArguments()).getPostLocInfo();
        imgUrl=BlueFragmentArgs.fromBundle(getArguments()).getPostImgUrl();

        TextView titleTv = view.findViewById(R.id.bluefrag_title_tv);
        if (title != null){titleTv.setText(title);}
        detailsTv=view.findViewById(R.id.postDetails_tv);
        if(details!=null){detailsTv.setText(details);}
        locationTV=view.findViewById(R.id.postLocation);
        if(location!=null){locationTV.setText(location);}
        image=view.findViewById(R.id.postUrl_blueFrag);
        if(!imgUrl.isEmpty()){
            Picasso.get().load(imgUrl).into(image);
        }


        View button = view.findViewById(R.id.bluefrag_back_btn);
        button.setOnClickListener((view1)->{Navigation.findNavController(view1).popBackStack();});
        return view;
    }
    public void setTitle(String title) {
        this.title = title;
        if (titleTv != null){
            titleTv.setText(title);
        }
    }
}