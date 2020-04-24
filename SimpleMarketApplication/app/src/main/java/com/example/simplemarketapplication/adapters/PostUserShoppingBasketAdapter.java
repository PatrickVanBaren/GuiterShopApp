package com.example.simplemarketapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostUserShoppingBasketAdapter extends RecyclerView.Adapter<PostUserShoppingBasketAdapter.ViewHolder> {

    private ClickListener mClickListener;
    private LayoutInflater mInflater;
    private Object[] postsArray;
    private String mDate;

    public PostUserShoppingBasketAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostUserShoppingBasketAdapter.ViewHolder(mInflater.inflate(R.layout.users_orders_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostShoppingBasket postProduct = (PostShoppingBasket) postsArray[position];
        holder.mProductName.setText(postProduct.getProductDescription());
        holder.mPrice.setText(postProduct.getProductPrice() + R.string.sing);
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

    public void setOnItemClickListener(PostUserShoppingBasketAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mProductName, mPrice;
        private Button mCheckoutProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProductName = itemView.findViewById(R.id.user_order_product_view);
            mPrice = itemView.findViewById(R.id.user_order_price_view);
            mCheckoutProduct = itemView.findViewById(R.id.user_order_button_view);
            mCheckoutProduct.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onBuyProduct(getAdapterPosition());
            });
        }
    }

    public interface ClickListener {
        void onBuyProduct(int position);
    }
}
