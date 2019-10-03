package com.eng.elfarsisy.blog.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eng.elfarsisy.blog.R;
import com.eng.elfarsisy.blog.data.Comment;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    Context mContext;
    List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.show_comment, parent, false);

        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.commentTxt.setText(mData.get(position).getComment());
        holder.publishername.setText(mData.get(position).getUserName());
        Glide.with(mContext).load(mData.get(position).getUserImage()).into(holder.commentImage);
        holder.commentdata.setText(holder.parseTimeStamp((Long) mData.get(position).getTimeStamp()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView publishername;
        TextView commentTxt;
        CircleImageView commentImage;
        TextView commentdata;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            publishername = itemView.findViewById(R.id.comment_user_name);
            commentTxt = itemView.findViewById(R.id.comment_content);
            commentImage = itemView.findViewById(R.id.comment_user_photo);
            commentdata = itemView.findViewById(R.id.comment_data);
        }


        private String parseTimeStamp(long postDate) {

            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(postDate);
            String date = DateFormat.format("dd-MM-yyy ,hh:mm", calendar).toString();


            return date;
        }
    }
}
