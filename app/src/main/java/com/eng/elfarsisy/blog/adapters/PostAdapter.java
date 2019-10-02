package com.eng.elfarsisy.blog.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eng.elfarsisy.blog.R;
import com.eng.elfarsisy.blog.data.Post;
import com.eng.elfarsisy.blog.ui.activities.HomeActivity;
import com.eng.elfarsisy.blog.ui.activities.PostDetailActivity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.show_posts, parent, false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.postPublisher.setText(mData.get(position).getTitle());
        holder.title.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.postPhoto);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.profileImage);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postPublisher;
        TextView title;
        ImageView profileImage;
        ImageView postPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postPublisher = itemView.findViewById(R.id.show_post_profile_name);
            title = itemView.findViewById(R.id.show_post_title);
            profileImage = itemView.findViewById(R.id.show_posts_profile_image);
            postPhoto = itemView.findViewById(R.id.show_post_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailIntent = new Intent(mContext, PostDetailActivity.class);
                    int postion = getAdapterPosition();

                    detailIntent.putExtra("title", mData.get(postion).getTitle());
                    detailIntent.putExtra("description", mData.get(postion).getDescription());
                    detailIntent.putExtra("postphoto", mData.get(postion).getPicture());

                    detailIntent.putExtra("publisherPhoto", mData.get(postion).getUserPhoto());
                    detailIntent.putExtra("postKey", mData.get(postion).getPostKey());

                    long timestamp = (long) mData.get(postion).getTimeStamp();
                    detailIntent.putExtra("postDate", timestamp);
                    mContext.startActivity(detailIntent);

                }
            });


        }
    }
}
