package com.example.simplemarketapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostProduct;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostProductListAdapter extends RecyclerView.Adapter<PostProductListAdapter.ViewHolder> {

    private ClickListener mClickListener;
    private LayoutInflater mInflater;
    private Object[] postsArray;
    private String mDate;

    public PostProductListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostProductListAdapter.ViewHolder(mInflater.inflate(R.layout.product_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostProduct postProduct = (PostProduct) postsArray[position];
        holder.mProductName.setText(postProduct.getDescription());
        holder.mPrice.setText(postProduct.getPrice() + holder.itemView.getContext().getString(R.string.sing));
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

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mProductName, mPrice;
        private ImageButton mDeleteButton, mBasketButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPrice = itemView.findViewById(R.id.product_price_view);
            mProductName = itemView.findViewById(R.id.product_name_view);
            mProductName.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onPostClick(getAdapterPosition());
            });

            mDeleteButton = itemView.findViewById(R.id.delete_button_view);
            mDeleteButton.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onDeletePostButton(getAdapterPosition());
            });

            mBasketButton = itemView.findViewById(R.id.basket_button_view);
            mBasketButton.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onCreateUserSBPostButton(getAdapterPosition());
            });
        }
    }

    public interface ClickListener {
        void onPostClick(int position);
        void onDeletePostButton(int position);
        void onCreateUserSBPostButton(int position);
    }
}
