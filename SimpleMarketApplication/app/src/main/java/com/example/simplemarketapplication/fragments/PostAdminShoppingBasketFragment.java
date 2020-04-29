package com.example.simplemarketapplication.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.adapters.PostAdminShoppingBasketAdapter;
import com.example.simplemarketapplication.posts.PostsModule;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;
import com.example.simplemarketapplication.posts.post.PostUser;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdminShoppingBasketFragment extends Fragment {

    PostsModule postsModule = PostsModule.getInstance();

    private RecyclerView mRecyclerView;
    private PostAdminShoppingBasketAdapter mPostAdapter;

    public PostAdminShoppingBasketFragment(){}

    private final PostsModule.Listener postsListener = new PostsModule.Listener() {
        @Override
        public void onStateChanged(PostsModule.State state) {
            //do nothing
        }

        @Override
        public void onPostCreatedSuccessfully(PostProduct postProduct) {
            //do nothing
        }

        @Override
        public void onPostCreationFailed(PostProduct postProduct, Throwable t) {
            //do nothing
        }

        @Override
        public void onPostsLoadedSuccessfully(Set<PostProduct> postProducts) {
            //do nothing
        }

        @Override
        public void onPostsLoadFailed(Throwable t) {
            Toast.makeText(getContext(), "Loading posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostDeletedSuccessfully(PostProduct postProduct) {
            //do nothing
        }

        @Override
        public void onPostDeletingFailed(PostProduct postProduct, Throwable t) {
            //do nothing
        }

        @Override
        public void onPostUpdatedSuccessfully(PostProduct postProduct) {
            //do nothing
        }

        @Override
        public void onPostUpdatingFailed(PostProduct postProduct, Throwable t) {
            //do nothing
        }

        @Override
        public void onUsersPostCreatedSuccessfully(PostUser post) {
            //do nothing
        }

        @Override
        public void onUsersPostCreationFailed(PostUser post, Throwable t) {
            //do nothing
        }

        @Override
        public void onUsersPostsLoadedSuccessfully(Set<PostUser> posts) {
            //do nothing
        }

        @Override
        public void onUsersPostUpdatedSuccessfully(PostUser post) {
            //do nothing
        }

        @Override
        public void onUsersPostUpdatingFailed(PostUser post, Throwable t) {
            //do nothing
        }

        @Override
        public void onSBPostCreatedSuccessfully(PostShoppingBasket postShoppingBasket) {
            mPostAdapter.setPost(postsModule.getPostsSB().toArray());
        }

        @Override
        public void onSBPostCreationFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            Toast.makeText(getContext(), "Creating posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSBPostsLoadedSuccessfully(Set<PostShoppingBasket> postShoppingBasket) {
            mPostAdapter.setPost(postsModule.getPostsSB().toArray());
        }

        @Override
        public void onSBPostDeletedSuccessfully(PostShoppingBasket postShoppingBasket) {
            mPostAdapter.setPost(postsModule.getPostsSB().toArray());
        }

        @Override
        public void onSBPostDeletingFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            Toast.makeText(getContext(), "Deleting posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSBPostUpdatedSuccessfully(PostShoppingBasket postShoppingBasket) {
            mPostAdapter.setPost(postsModule.getPostsSB().toArray());
        }

        @Override
        public void onSBPostUpdatingFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            Toast.makeText(getContext(), "Updating posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    PostAdminShoppingBasketAdapter.ClickListener mPostClickListener = position -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(R.string.delete_confirm)
                .setTitle(R.string.delete_title)
                .setNegativeButton(R.string.cancel_button, null)
                .setPositiveButton(R.string.ok_button, ((dialog, which) -> {
                    PostShoppingBasket removePostProduct = (PostShoppingBasket) postsModule.getPostsSB().toArray()[position];
                    postsModule.deleteProductSB(removePostProduct);
                }))
                .create().show();
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_recycler, container, false);
        mPostAdapter = new PostAdminShoppingBasketAdapter(getContext());
        mRecyclerView = view.findViewById(R.id.post_view_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostAdapter.setDate(getString(R.string.date_format));
        mPostAdapter.setOnItemClickListener(mPostClickListener);
        mRecyclerView.setAdapter(mPostAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        postsModule.addListener(postsListener);
        mPostAdapter.setPost(postsModule.getPostsSB().toArray());
    }

    @Override
    public void onPause() {
        super.onPause();
        postsModule.removeListener(postsListener);
    }
}
