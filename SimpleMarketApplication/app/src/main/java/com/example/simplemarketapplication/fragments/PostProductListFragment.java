package com.example.simplemarketapplication.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simplemarketapplication.AuthModule;
import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.adapters.PostProductListAdapter;
import com.example.simplemarketapplication.dialogFragments.EditPostDialogFragment;
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

public class PostProductListFragment extends Fragment {

    PostsModule postsModule = PostsModule.getInstance();
    AuthModule authModule = AuthModule.getInstance();

    public PostProductListFragment() {}

    private RecyclerView mRecyclerView;
    private PostProductListAdapter mPostAdapter;

    private final PostsModule.Listener postListener = new PostsModule.Listener() {
        @Override
        public void onStateChanged(PostsModule.State state) {
            //do nothing
        }

        @Override
        public void onPostCreatedSuccessfully(PostProduct postProduct) {
            mPostAdapter.setPost(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostCreationFailed(PostProduct postProduct, Throwable t) {
            Toast.makeText(getContext(), "Creating posts failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostsLoadedSuccessfully(Set<PostProduct> postProducts) {
            mPostAdapter.setPost(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostsLoadFailed(Throwable t) {
            Toast.makeText(getContext(), "Loading posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostDeletedSuccessfully(PostProduct postProduct) {
            mPostAdapter.setPost(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostDeletingFailed(PostProduct postProduct, Throwable t) {
            Toast.makeText(getContext(), "Deleting posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostUpdatedSuccessfully(PostProduct postProduct) {
            mPostAdapter.setPost(postsModule.getPosts().toArray());
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
            //do nothing
        }

        @Override
        public void onSBPostCreationFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            //do nothing
        }

        @Override
        public void onSBPostsLoadedSuccessfully(Set<PostShoppingBasket> postShoppingBasket) {
            //do nothing
        }

        @Override
        public void onSBPostDeletedSuccessfully(PostShoppingBasket postShoppingBasket) {
            //do nothing
        }

        @Override
        public void onSBPostDeletingFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            //do nothing
        }

        @Override
        public void onSBPostUpdatedSuccessfully(PostShoppingBasket postShoppingBasket) {
            //do nothing
        }

        @Override
        public void onSBPostUpdatingFailed(PostShoppingBasket postShoppingBasket, Throwable t) {
            //do nothing
        }
    };

    PostProductListAdapter.ClickListener mPostClickListener = new PostProductListAdapter.ClickListener() {
        @Override
        public void onPostClick(int position) {
            if (authModule.getState() == AuthModule.State.ADMINISTRATOR_PRODUCTS_LIST) {
                PostProduct editPostProduct = (PostProduct) postsModule.getPosts().toArray()[position];
                EditPostDialogFragment editPostDialogFragment = new EditPostDialogFragment();
                editPostDialogFragment.setPost(editPostProduct);
                editPostDialogFragment.show(getFragmentManager(), "EditPostDialogFragment");
            }
        }

        @Override
        public void onDeletePostButton(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setMessage(R.string.delete_confirm)
                    .setTitle(R.string.delete_title)
                    .setNegativeButton(R.string.cancel_button, null)
                    .setPositiveButton(R.string.ok_button, ((dialog, which) -> {
                        PostProduct removePostProduct = (PostProduct) postsModule.getPosts().toArray()[position];
                        postsModule.deleteProduct(removePostProduct);
                    }))
                    .create().show();
        }

        @Override
        public void onCreateUserSBPostButton(int position) {
            PostProduct copyPostProduct = (PostProduct) postsModule.getPosts().toArray()[position];
            new PostShoppingBasket(copyPostProduct);
            Toast.makeText(getContext(), R.string.copying_product, Toast.LENGTH_SHORT).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_recycler, container, false);
        mPostAdapter = new PostProductListAdapter(getContext());
        mRecyclerView = view.findViewById(R.id.post_view_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostAdapter.setDateFormat(getString(R.string.date_format));
        mPostAdapter.setOnItemClickListener(mPostClickListener);
        mRecyclerView.setAdapter(mPostAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        postsModule.addListener(postListener);
        mPostAdapter.setPost(postsModule.getPosts().toArray());
    }

    @Override
    public void onPause() {
        super.onPause();
        postsModule.removeListener(postListener);
    }
}
