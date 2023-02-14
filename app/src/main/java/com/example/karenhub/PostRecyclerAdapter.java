package com.example.karenhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karenhub.model.Post;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


class PostViewHolder extends RecyclerView.ViewHolder{
    TextView nameTv;
    TextView idTv;
    List<Post> data;
    ImageView avatarImage;
    public PostViewHolder(@NonNull View itemView, PostRecyclerAdapter.OnItemClickListener listener, List<Post> data) {
        super(itemView);
        this.data = data;
        nameTv = itemView.findViewById(R.id.postlistrow_name_tv);
        idTv = itemView.findViewById(R.id.postlistrow_label_tv);
        avatarImage = itemView.findViewById(R.id.postlistrow_avatar_img);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Post post, int pos) {
        nameTv.setText(post.title);
        idTv.setText(post.label);
        //cb.setChecked(post.cb);
        //cb.setTag(pos);
        if (post.getImgUrl()  != "") {
            //Picasso.get().load(post.getImgUrl()).placeholder(R.drawable.avatar).into(avatarImage);
            Picasso.get()
                    .load(post.getImgUrl())
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(avatarImage);
            //.placeholder(R.drawable.avatar)
        }else{
            avatarImage.setImageResource(R.drawable.avatar);
        }
    }
}

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
    OnItemClickListener listener;
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Post> data;
    public void setData(List<Post> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public PostRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_list_row,parent,false);
        return new PostViewHolder(view,listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post st = data.get(position);
        holder.bind(st,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

