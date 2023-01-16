package com.example.karenhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karenhub.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;


class StudentViewHolder extends RecyclerView.ViewHolder{
    TextView nameTv;
    TextView idTv;
    List<Post> data;
    ImageView avatarImage;
    public StudentViewHolder(@NonNull View itemView, StudentRecyclerAdapter.OnItemClickListener listener, List<Post> data) {
        super(itemView);
        this.data = data;
        nameTv = itemView.findViewById(R.id.studentlistrow_name_tv);
        idTv = itemView.findViewById(R.id.studentlistrow_id_tv);
        avatarImage = itemView.findViewById(R.id.studentlistrow_avatar_img);
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
        idTv.setText(post.id);
        //cb.setChecked(post.cb);
        //cb.setTag(pos);
        if (post.getImgUrl()  != "") {
            Picasso.get().load(post.getImgUrl()).placeholder(R.drawable.avatar).into(avatarImage);
        }else{
            avatarImage.setImageResource(R.drawable.avatar);
        }
    }
}

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentViewHolder>{
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
    public StudentRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_list_row,parent,false);
        return new StudentViewHolder(view,listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Post st = data.get(position);
        holder.bind(st,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

