package com.example.karenhub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karenhub.databinding.FragmentPostsListBinding;
import com.example.karenhub.model.Model;
import com.example.karenhub.model.Post;

public class PostsListFragment extends Fragment {
    FragmentPostsListBinding binding;
    PostRecyclerAdapter adapter;
    PostsListFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Post st = viewModel.getData().get(pos);
                PostsListFragmentDirections.ActionPostsListFragmentToBlueFragment action = PostsListFragmentDirections.actionPostsListFragmentToBlueFragment(st.title);
                Navigation.findNavController(view).navigate((NavDirections) action);
            }
        });

        View addButton = view.findViewById(R.id.btnAdd);
        NavDirections action = PostsListFragmentDirections.actionGlobalAddPostFragment();
        addButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().getAllPosts((stList)->{
            viewModel.setData(stList);
            adapter.setData(viewModel.getData());
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}