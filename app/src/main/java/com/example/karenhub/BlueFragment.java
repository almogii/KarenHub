package com.example.karenhub;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BlueFragment extends Fragment {
    TextView titleTv;
    String title;

    public static BlueFragment newInstance(String title){
        BlueFragment frag = new BlueFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE",title);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.title = bundle.getString("TITLE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blue, container, false);

        title = BlueFragmentArgs.fromBundle(getArguments()).getBlueTitle();

        TextView titleTv = view.findViewById(R.id.bluefrag_title_tv);
        if (title != null){
            titleTv.setText(title);
        }

        View button = view.findViewById(R.id.bluefrag_back_btn);
        button.setOnClickListener((view1)->{
            Navigation.findNavController(view1).popBackStack();
        });
        return view;
    }

    public void setTitle(String title) {
        this.title = title;
        if (titleTv != null){
            titleTv.setText(title);
        }
    }
}