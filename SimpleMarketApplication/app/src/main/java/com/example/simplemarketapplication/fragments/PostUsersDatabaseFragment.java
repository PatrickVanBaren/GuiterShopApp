package com.example.simplemarketapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemarketapplication.R;
import com.example.simplemarketapplication.adapters.PostUsersDatabaseAdapter;
import com.example.simplemarketapplication.dialogFragments.EditUserPostDialogFragment;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.example.simplemarketapplication.posts.PostsModule;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;
import com.example.simplemarketapplication.posts.post.PostUser;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostUsersDatabaseFragment extends Fragment {

    PostsModule postsModule = PostsModule.getInstance();

    private RecyclerView mRecyclerView;
    private PostUsersDatabaseAdapter mPostAdapter;
    private TextView mUserName, mUserPhone;

    public PostUsersDatabaseFragment() {}

    private final PostsModule.Listener postListener = new PostsModule.Listener() {
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
            mPostAdapter.setPost(postsModule.getPostsUsers().toArray());
        }

        @Override
        public void onUsersPostCreationFailed(PostUser post, Throwable t) {
            Toast.makeText(getContext(), "Creating posts failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUsersPostsLoadedSuccessfully(Set<PostUser> posts) {
            mPostAdapter.setPost(postsModule.getPostsUsers().toArray());
        }

        @Override
        public void onUsersPostUpdatedSuccessfully(PostUser post) {
            mPostAdapter.setPost(postsModule.getPostsUsers().toArray());
        }

        @Override
        public void onUsersPostUpdatingFailed(PostUser post, Throwable t) {
            Toast.makeText(getContext(), "Updating posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    PostUsersDatabaseAdapter.ClickListener mPostClickListener = position -> {
        PostUser user = (PostUser) postsModule.getPostsUsers().toArray()[position];
        EditUserPostDialogFragment editPost = new EditUserPostDialogFragment();
        editPost.setPostUser(user);
        editPost.show(getFragmentManager(), "EditUserDialogFragment");
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_recycler, container, false);
        mPostAdapter = new PostUsersDatabaseAdapter(getContext());
        mRecyclerView = view.findViewById(R.id.post_view_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostAdapter.setDateFormat(getString(R.string.date_format));
        mPostAdapter.setOnClickListener(mPostClickListener);
        mRecyclerView.setAdapter(mPostAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        postsModule.addListener(postListener);
        mPostAdapter.setPost(postsModule.getPostsUsers().toArray());
    }

    @Override
    public void onPause() {
        super.onPause();
        postsModule.removeListener(postListener);
    }
}
