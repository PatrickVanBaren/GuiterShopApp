package com.example.simplemarketapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostUsersDatabaseAdapter extends RecyclerView.Adapter<PostUsersDatabaseAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ClickListener mClickListener;
    private Object[] postsArray;
    private String mDate;

    public PostUsersDatabaseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostUsersDatabaseAdapter.ViewHolder(mInflater.inflate(R.layout.users_db_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostUser user = (PostUser) postsArray[position];
        holder.mUserName.setText(user.getUserName());
        holder.mUserPhone.setText(user.getPhoneNumber());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mDate, Locale.US);
        holder.mDateRegistration.setText(simpleDateFormat.format(user.getUserCreatedTime()));
    }

    @Override
    public int getItemCount() {
        return postsArray == null ? 0 : postsArray.length;
    }

    public void setPost(Object[] postsArray) {
        this.postsArray = postsArray;
        notifyDataSetChanged();
    }

    public String getDateFormat() {
        return mDate;
    }

    public void setDateFormat(String mDate) {
        this.mDate = mDate;
    }

    public void setOnClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mUserName, mUserPhone, mDateRegistration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mUserName = itemView.findViewById(R.id.user_db_name_view);
            mUserName.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onUserPostEdit(getAdapterPosition());
            });
            mUserPhone = itemView.findViewById(R.id.user_db_phone_view);
            mUserPhone.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onUserPostEdit(getAdapterPosition());
            });
            mDateRegistration = itemView.findViewById(R.id.user_db_date_view);
        }
    }

    public interface ClickListener {
        void onUserPostEdit(int position);
    }
}
