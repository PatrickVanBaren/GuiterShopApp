package com.example.simplemarketapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdminShoppingBasketAdapter extends RecyclerView.Adapter<PostAdminShoppingBasketAdapter.ViewHolder> {

    private ClickListener mClickListener;
    private LayoutInflater mInflater;
    private Object[] postArray;

    private String mDate;

    public PostAdminShoppingBasketAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdminShoppingBasketAdapter.ViewHolder(mInflater.inflate(R.layout.admin_orders_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostShoppingBasket postProduct = (PostShoppingBasket) postArray[position];
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mDate, Locale.US);
        holder.mDateRegistration.setText(simpleDateFormat.format(postProduct.getCreateTime()));
        holder.mUserName.setText(postProduct.getUserName());
        holder.mUserPhone.setText(postProduct.getUserPhone());
        holder.mProduct.setText(postProduct.getProductDescription());
        holder.mPrice.setText(postProduct.getProductPrice() + R.string.sing);
    }

    public void setOnItemClickListener(PostAdminShoppingBasketAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return postArray == null ? 0 : postArray.length;
    }

    public void setPost(Object[] postArray) {
        this.postArray = postArray;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateRegistration, mUserName, mProduct, mPrice, mUserPhone;
        private Button mDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateRegistration = itemView.findViewById(R.id.admin_order_date_view);
            mUserName = itemView.findViewById(R.id.admin_user_name);
            mUserPhone = itemView.findViewById(R.id.admin_order_phone_view);
            mProduct = itemView.findViewById(R.id.admin_order_product_view);
            mPrice = itemView.findViewById(R.id.admin_order_price_view);
            mDeleteButton = itemView.findViewById(R.id.admin_order_button_view);
            mDeleteButton.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onDeleteClick(getAdapterPosition());
            });
        }
    }

    public interface ClickListener {
        void onDeleteClick(int position);
    }
}
