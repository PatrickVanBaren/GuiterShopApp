package com.example.simplemarketapplication.posts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.simplemarketapplication.db.Database;
import com.example.simplemarketapplication.db.Tables.Products;
import com.example.simplemarketapplication.db.Tables.ShoppingBasket;
import com.example.simplemarketapplication.db.Tables.Users;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.example.simplemarketapplication.posts.post.PostUser;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PostsModule {

    private static final int BACKEND_WAIT_TIME = 3000;
    private static PostsModule sInstance;
    private final Database mDatabase = Database.getInstance();

    private final Set<Listener> mListeners = Sets.newHashSet();
    private final Set<PostProduct> mPostsProduct = Sets.newTreeSet(new PostsComparator());
    private final Set<PostUser> mPostsUsers = Sets.newTreeSet(new UsersPostsComparator());
    private final Set<PostShoppingBasket> mPostsAdminShoppingBasket = Sets.newTreeSet(new SBPostsComparator());

    private State mState = State.IDLE;

    public static void createInstance() {
        sInstance = new PostsModule();
    }

    public static PostsModule getInstance() {
        return sInstance;
    }

    private PostsModule() {
        changeState(State.LOADING);
        new InitialProductsLoader().execute();
        new InitialUsersLoader().execute();
        new InitialShoppingBasketsLoader().execute();
    }

    private void changeState(final State newState) {
        //if (mState == newState) throw new RuntimeException("New state is equals to old state! (Default: \"Client\")");//?
        //Preconditions.checkState(mState != newState);

        mState = newState;
        for (final Listener listener : mListeners) {
            listener.onStateChanged(mState);
        }
    }

    public void createProduct(final PostProduct postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.CREATING);

        final Bundle params = new Bundle();
        params.putString("description", postProduct.getDescription());
        params.putString("price", postProduct.getPrice());

        new CreatePostProduct().execute(params, postProduct);
    }

    public void createUser(final PostUser postUser) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.CREATING);

        final Bundle params = new Bundle();
        params.putString("name", postUser.getUserName());
        params.putString("phone", postUser.getPhoneNumber());

        new CreatePostUser().execute();
    }

    public void createShoppingBasket(final PostProduct postProduct, final Users user) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.CREATING);

        final Bundle params = new Bundle();
        params.putString("productDescription", postProduct.getDescription());
        params.putString("productPrice", postProduct.getPrice());
        if (user == null) {
            params.putString("userName", "");
            params.putString("userPhone", "");
        } else {
            params.putString("userName", user.getUserName());
            params.putString("userPhone", user.getPhoneNumber());
        }

        new CreateUserPostSB().execute();
    }

    public void deleteProduct(final PostProduct postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.DELETING);

        new DeletePostProduct().execute(postProduct);
    }

    public void deleteProductSB(final PostShoppingBasket postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.DELETING);

        new DeletePostProductSB().execute(postProduct);
    }

    public void updateProduct(final PostProduct postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.UPDATING);

        new UpdatePostProduct().execute(postProduct.getId());
    }

    public void updateUsers(final PostUser postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.UPDATING);

        new UpdatePostUser().execute();
    }

    public void updateProductSB(final PostShoppingBasket postProduct) {
        //if (mState != State.IDLE) throw new RuntimeException();//?
        //Preconditions.checkState(mState == State.IDLE);

        changeState(State.UPDATING);

        new UpdatePostShoppingBasket().execute(postProduct.getSBId());
    }

    public void loadProduct() {
        loadProduct(false);
    }

    public void loadProductSB() {
        loadProductSB(false);
    }

    public void loadUsers() {
        loadUsers(false);
    }

    private void loadProduct(final boolean shouldIgnoreCurrentState) {
        if (!shouldIgnoreCurrentState) {
            //if (mState != State.IDLE) throw new RuntimeException(); //?
            //Preconditions.checkState(mState == State.IDLE);
        }

        changeState(State.LOADING);

        new LoadPostsProduct().execute();
    }

    private void loadUsers(final boolean shouldIgnoreCurrentState) {
        if (!shouldIgnoreCurrentState) {
            //if (mState != State.IDLE) throw new RuntimeException(); //?
            //Preconditions.checkState(mState == State.IDLE);
        }

        changeState(State.LOADING);

        new LoadPostsUsers().execute();
    }

    private void loadProductSB(final boolean shouldIgnoreCurrentState) {
        if (!shouldIgnoreCurrentState) {
            //if (mState != State.IDLE) throw new RuntimeException(); //?
            //Preconditions.checkState(mState == State.IDLE);
        }

        changeState(State.LOADING);

        new LoadPostsShoppingBasket().execute();
    }

    public void addListener(final Listener listener) {
        //if(mListeners.contains(listener)) throw new RuntimeException();//?
        //Preconditions.checkState(!mListeners.contains(listener));

        mListeners.add(listener);
        listener.onStateChanged(mState);
    }

    public void removeListener(final Listener listener) {
        //if(!mListeners.contains(listener)) throw new RuntimeException();//?
        //Preconditions.checkState(mListeners.contains(listener));
        mListeners.remove(listener);
    }

    public Set<PostProduct> getPosts() {
        return ImmutableSet.copyOf(mPostsProduct);
    }

    private class InitialProductsLoader extends AsyncTask<Void, Void, Set<PostProduct>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostProduct> doInBackground(Void... voids) {
            final Set<PostProduct> products = Sets.newHashSet();
            try {
                for (final Products product : mDatabase.getProductsList()) {
                    products.add(createPost(product));
                }
                return products;
            } catch (SQLException e) {
                mError = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Set<PostProduct> postProducts) {
            if (mError == null) {
                mPostsProduct.addAll(postProducts);
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadedSuccessfully(mPostsProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }

        private PostProduct createPost(final Products products) {
            final PostProduct postProduct = new PostProduct();
            postProduct.setId(products.getId());
            postProduct.setDescription(products.getTitle());
            postProduct.setPrice(products.getPrice());
            return postProduct;
        }
    }

    private class InitialUsersLoader extends AsyncTask<Void, Void, Set<PostUser>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostUser> doInBackground(Void... voids) {
            final Set<PostUser> users = Sets.newHashSet();
            try {
                for (final Users user : mDatabase.getUsersList()) {
                    users.add(createPost(user));
                }
                return users;
            } catch (SQLException e) {
                mError = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Set<PostUser> posts) {
            if (mError == null) {
                mPostsUsers.addAll(posts);
                for (final Listener listener : mListeners) {
                    listener.onUsersPostsLoadedSuccessfully(mPostsUsers);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }

        private PostUser createPost(final Users users) {
            final PostUser user = new PostUser();
            user.setUserId(users.getId());
            user.setUserName(users.getUserName());
            user.setPhoneNumber(users.getPhoneNumber());
            return user;
        }
    }

    private class InitialShoppingBasketsLoader extends AsyncTask<Void, Void, Set<PostShoppingBasket>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostShoppingBasket> doInBackground(Void... voids) {
            final Set<PostShoppingBasket> shoppingBaskets = Sets.newHashSet();
            try {
                for (final ShoppingBasket shoppingBasket : mDatabase.getShoppingBasketList()) {
                    shoppingBaskets.add(createPost(shoppingBasket));
                }
                return shoppingBaskets;
            } catch (SQLException e) {
                mError = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Set<PostShoppingBasket> postProducts) {
            if (mError == null) {
                mPostsAdminShoppingBasket.addAll(postProducts);
                for (final Listener listener : mListeners) {
                    listener.onSBPostsLoadedSuccessfully(mPostsAdminShoppingBasket);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }

        private PostShoppingBasket createPost(final ShoppingBasket shoppingBasket) {
            final PostShoppingBasket postProduct = new PostShoppingBasket();
            postProduct.setSBId(shoppingBasket.getId());
            postProduct.setProductDescription(shoppingBasket.getTitle());
            postProduct.setProductPrice(shoppingBasket.getPrice());
            if (shoppingBasket.getUser() == null || shoppingBasket.getPhone() == null) {
                postProduct.setUserName("");
                postProduct.setUserPhone("");
            } else {
                postProduct.setUserName(shoppingBasket.getUser());
                postProduct.setUserPhone(shoppingBasket.getPhone());
            }
            return postProduct;
        }
    }

    private Products createProd(final PostProduct postProduct) {
        final Products product = new Products();
        product.setId(postProduct.getId());
        product.setTitle(postProduct.getDescription());
        product.setPrice(postProduct.getPrice());
        return product;
    }

    private ShoppingBasket createProdSB(final PostShoppingBasket postProduct) {
        final ShoppingBasket product = new ShoppingBasket();
        product.setId(postProduct.getSBId());
        product.setTitle(postProduct.getProductDescription());
        product.setPrice(postProduct.getProductPrice());
        if (postProduct.getUserName() == null || postProduct.getUserPhone() == null) {
            product.setUser("");
            product.setPhone("");
        } else {
            product.setUser(postProduct.getUserName());
            product.setPhone(postProduct.getUserPhone());
        }
        return product;
    }

    private Users createUsers(final PostUser postUser) {
        final Users user = new Users();
        user.setId(postUser.getUserId());
        user.setUserName(postUser.getUserName());
        user.setPhoneNumber(postUser.getPhoneNumber());
        return user;
    }

    private class UpdatePostProduct extends AsyncTask<Object, Void, PostProduct> {

        private volatile Throwable mError;

        @Override
        protected PostProduct doInBackground(final Object... params) {
            final PostProduct postProduct = (PostProduct) params[0];

            try {
                mDatabase.updateProducts(createProd(postProduct));
            } catch (SQLException e) {
                mError = e;
                return postProduct;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(final PostProduct postProduct) {
            if (mError == null) {
                for (final Listener listener : mListeners) {
                    listener.onPostUpdatedSuccessfully(postProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostUpdatingFailed(postProduct, mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class UpdatePostUser extends AsyncTask<Object, Void, PostUser> {

        private Throwable mError;

        @Override
        protected PostUser doInBackground(Object... params) {
            final PostUser postProduct = (PostUser) params[0];
            try {
                mDatabase.updateUsers(createUsers(postProduct));
            } catch (SQLException e) {
                mError = e;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(PostUser postUser) {
            if (mError == null) {
                for (final Listener listener : mListeners) {
                    listener.onUsersPostUpdatedSuccessfully(postUser);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onUsersPostUpdatingFailed(postUser, mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class UpdatePostShoppingBasket extends AsyncTask<Object, Void, PostShoppingBasket> {

        private volatile Throwable mError;

        @Override
        protected PostShoppingBasket doInBackground(Object... params) {
            final PostShoppingBasket postProduct = (PostShoppingBasket) params[0];
            try {
                mDatabase.updateShoppingBasket(createProdSB(postProduct));
            } catch (SQLException e) {
                mError = e;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(PostShoppingBasket postProduct) {
            if (mError == null) {
                for (final Listener listener : mListeners) {
                    listener.onSBPostUpdatedSuccessfully(postProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onSBPostUpdatingFailed(postProduct, mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class DeletePostProduct extends AsyncTask<Object, Void, PostProduct> {

        private volatile Throwable mError;

        @Override
        protected PostProduct doInBackground(final Object... params) {
            final PostProduct postProduct = (PostProduct) params[0];

            try{
                mDatabase.deleteProduct(postProduct.getId());
            } catch (final Throwable t) {
                mError = t;
                return postProduct;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(final PostProduct postProduct) {
            if (mError == null) {
                mPostsProduct.remove(postProduct);
                for (final Listener listener : mListeners) {
                    listener.onPostDeletedSuccessfully(postProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostDeletingFailed(postProduct, mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class DeletePostProductSB extends AsyncTask<Object, Void, PostShoppingBasket> {

        private volatile Throwable mError;

        @Override
        protected PostShoppingBasket doInBackground(final Object... params) {
            final PostShoppingBasket postProduct = (PostShoppingBasket) params[0];

            try{
                mDatabase.deleteProductSB(postProduct.getSBId());
            } catch (final Throwable t) {
                mError = t;
                return postProduct;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(final PostShoppingBasket postProduct) {
            if (mError == null) {
                mPostsAdminShoppingBasket.remove(postProduct);
                for (final Listener listener : mListeners) {
                    listener.onSBPostDeletedSuccessfully(postProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onSBPostDeletingFailed(postProduct, mError);
                }
            }

            changeState(State.IDLE);
        }
    }

    private class CreatePostProduct extends AsyncTask<Object, Void, PostProduct> {

        private volatile Throwable mError;

        @Override
        protected PostProduct doInBackground(final Object... params) {
            final PostProduct postProduct = (PostProduct) params[1];

            Random rnd = new Random();
            try {
                String id = Integer.toString(rnd.nextInt(99999));
                postProduct.setId(id);
                postProduct.setCreatedTime(new Date());
                mDatabase.createProduct(createProd(postProduct));
            } catch (final Throwable t) {
                mError = t;
                return postProduct;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(final PostProduct postProduct) {
            if (mError == null) {
                mPostsProduct.add(postProduct);
                for (final Listener listener : mListeners) {
                    listener.onPostCreatedSuccessfully(postProduct);
                }
                new Handler(Looper.getMainLooper()).postDelayed(() -> loadProduct(true), BACKEND_WAIT_TIME);
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostCreationFailed(postProduct, mError);
                }
                changeState(State.IDLE);
            }
        }
    }

    private class CreatePostUser extends AsyncTask<Object, Void, PostUser> {

        private volatile Throwable mError;

        @Override
        protected PostUser doInBackground(final Object... params) {
            final PostUser postUser = (PostUser) params[0];

            Random rnd = new Random();
            try {
                String id = Integer.toString(rnd.nextInt(99999));
                postUser.setUserId(id);
                postUser.setUserCreatedTime(new Date());
                mDatabase.createUser(createUsers(postUser));
            } catch (final Throwable t) {
                mError = t;
                return postUser;
            }
            return postUser;
        }

        @Override
        protected void onPostExecute(final PostUser postUser) {
            if (mError == null) {
                mPostsUsers.add(postUser);
                for (final Listener listener : mListeners) {
                    listener.onUsersPostCreatedSuccessfully(postUser);
                }
                new Handler(Looper.getMainLooper()).postDelayed(() -> loadUsers(true), BACKEND_WAIT_TIME);
            } else {
                for (final Listener listener : mListeners) {
                    listener.onUsersPostCreationFailed(postUser, mError);
                }
                changeState(State.IDLE);
            }
        }
    }

    private class CreateUserPostSB extends AsyncTask<Object, Void, PostShoppingBasket> {

        private volatile Throwable mError;

        @Override
        protected PostShoppingBasket doInBackground(Object... params) {
            final PostShoppingBasket postProduct = (PostShoppingBasket) params[0];
            Random rnd = new Random();
            try{
                String id = Integer.toString(rnd.nextInt(99999));
                postProduct.setSBId(id);
                postProduct.setCreateTime(new Date());
                mDatabase.createShoppingBasket(createProdSB(postProduct));
            } catch (final Throwable t) {
                mError = t;
                return postProduct;
            }
            return postProduct;
        }

        @Override
        protected void onPostExecute(PostShoppingBasket posts) {
            if (mError == null) {
                mPostsAdminShoppingBasket.add(posts);
                for (final Listener listener : mListeners) {
                    listener.onSBPostCreatedSuccessfully(posts);
                }
                new Handler(Looper.getMainLooper()).postDelayed(() -> loadProductSB(true), BACKEND_WAIT_TIME);
            } else {
                for (final Listener listener : mListeners) {
                    listener.onSBPostCreationFailed(posts, mError);
                }
                changeState(State.IDLE);
            }
        }
    }

    private class LoadPostsProduct extends AsyncTask<Object, Void, Set<PostProduct>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostProduct> doInBackground(final Object... params) {
            try {
                final Set<PostProduct> postProducts = Sets.newHashSet();
                List<Products> postList = mDatabase.getProductsList();
                for(int i = 0; i < postList.size(); i++) {
                    final PostProduct postProduct = new PostProduct();
                    postProduct.setDescription(postList.get(i).getTitle());
                    postProduct.setId(postList.get(i).getId());
                    postProduct.setPrice(postList.get(i).getPrice());
                    postProduct.setCreatedTime(postList.get(i).getCreatedTime());
                    postProducts.add(postProduct);
                }
                return postProducts;
            } catch (final Throwable t) {
                mError = t;
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Set<PostProduct> postProducts) {
            if (mError == null) {
                mPostsProduct.clear();
                mPostsProduct.addAll(postProducts);
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadedSuccessfully(mPostsProduct);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class LoadPostsUsers extends AsyncTask<Object, Void, Set<PostUser>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostUser> doInBackground(final Object... params) {
            try {
                final Set<PostUser> postProducts = Sets.newHashSet();
                List<Users> postList = mDatabase.getUsersList();
                for(int i = 0; i < postList.size(); i++) {
                    final PostUser postUser = new PostUser();
                    postUser.setUserId(postList.get(i).getId());
                    postUser.setUserName(postList.get(i).getUserName());
                    postUser.setPhoneNumber(postList.get(i).getPhoneNumber());
                    postUser.setUserCreatedTime(postList.get(i).getCreateTime());
                    postProducts.add(postUser);
                }
                return postProducts;
            } catch (final Throwable t) {
                mError = t;
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Set<PostUser> postUsers) {
            if (mError == null) {
                mPostsUsers.clear();
                mPostsUsers.addAll(postUsers);
                for (final Listener listener : mListeners) {
                    listener.onUsersPostsLoadedSuccessfully(mPostsUsers);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private class LoadPostsShoppingBasket extends AsyncTask<Object, Void, Set<PostShoppingBasket>> {

        private volatile Throwable mError;

        @Override
        protected Set<PostShoppingBasket> doInBackground(final Object... params) {
            try {
                final Set<PostShoppingBasket> postProducts = Sets.newHashSet();
                List<ShoppingBasket> postList = mDatabase.getShoppingBasketList();
                for(int i = 0; i < postList.size(); i++) {
                    final PostShoppingBasket postProduct = new PostShoppingBasket();
                    postProduct.setSBId(postList.get(i).getId());
                    postProduct.setProductDescription(postList.get(i).getTitle());
                    postProduct.setProductPrice(postList.get(i).getPrice());
                    postProduct.setUserName(postList.get(i).getUser());
                    postProduct.setUserPhone(postList.get(i).getPhone());
                    postProduct.setCreateTime(postList.get(i).getCreatedTime());
                    postProducts.add(postProduct);
                }
                return postProducts;
            } catch (final Throwable t) {
                mError = t;
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Set<PostShoppingBasket> postProducts) {
            if (mError == null) {
                mPostsAdminShoppingBasket.clear();
                mPostsAdminShoppingBasket.addAll(postProducts);
                for (final Listener listener : mListeners) {
                    listener.onSBPostsLoadedSuccessfully(mPostsAdminShoppingBasket);
                }
            } else {
                for (final Listener listener : mListeners) {
                    listener.onPostsLoadFailed(mError);
                }
            }
            changeState(State.IDLE);
        }
    }

    private static class PostsComparator implements Comparator<PostProduct> {

        @Override
        public int compare(final PostProduct lhs, final PostProduct rhs) {
            final Date rhsCreatedTime = rhs.getCreatedTime();
            final Date lhsCreatedTime = lhs.getCreatedTime();

            if (rhsCreatedTime == null && lhsCreatedTime == null) {
                return 0;
            }

            if (rhsCreatedTime == null) {
                return -1;
            }

            if (lhsCreatedTime == null) {
                return 1;
            }
            return (int) Math.signum(rhsCreatedTime.getTime() - lhsCreatedTime.getTime());
        }
    }

    private static class UsersPostsComparator implements Comparator<PostUser> {

        @Override
        public int compare(final PostUser lhs, final PostUser rhs) {
            final Date rhsCreatedTime = rhs.getUserCreatedTime();
            final Date lhsCreatedTime = lhs.getUserCreatedTime();

            if (rhsCreatedTime == null && lhsCreatedTime == null) {
                return 0;
            }

            if (rhsCreatedTime == null) {
                return -1;
            }

            if (lhsCreatedTime == null) {
                return 1;
            }
            return (int) Math.signum(rhsCreatedTime.getTime() - lhsCreatedTime.getTime());
        }
    }

    private static class SBPostsComparator implements Comparator<PostShoppingBasket> {

        @Override
        public int compare(final PostShoppingBasket lhs, final PostShoppingBasket rhs) {
            final Date rhsCreatedTime = rhs.getCreateTime();
            final Date lhsCreatedTime = lhs.getCreateTime();

            if (rhsCreatedTime == null && lhsCreatedTime == null) {
                return 0;
            }

            if (rhsCreatedTime == null) {
                return -1;
            }

            if (lhsCreatedTime == null) {
                return 1;
            }
            return (int) Math.signum(rhsCreatedTime.getTime() - lhsCreatedTime.getTime());
        }
    }

    public enum State {
        IDLE, LOADING, CREATING, DELETING, UPDATING
    }

    public interface Listener {

        void onStateChanged(State state);
        //product
        void onPostCreatedSuccessfully(PostProduct postProduct);

        void onPostCreationFailed(PostProduct postProduct, Throwable t);

        void onPostsLoadedSuccessfully(Set<PostProduct> postProducts);

        void onPostsLoadFailed(Throwable t);

        void onPostDeletedSuccessfully(PostProduct postProduct);

        void onPostDeletingFailed(PostProduct postProduct, Throwable t);

        void onPostUpdatedSuccessfully(PostProduct postProduct);

        void onPostUpdatingFailed(PostProduct postProduct, Throwable t);
        //user
        void onUsersPostCreatedSuccessfully(PostUser post);

        void onUsersPostCreationFailed(PostUser post, Throwable t);

        void onUsersPostsLoadedSuccessfully(Set<PostUser> posts);

        void onUsersPostUpdatedSuccessfully(PostUser post);

        void onUsersPostUpdatingFailed(PostUser post, Throwable t);
        //shopping basket
        void onSBPostCreatedSuccessfully(PostShoppingBasket postShoppingBasket);

        void onSBPostCreationFailed(PostShoppingBasket postShoppingBasket, Throwable t);

        void onSBPostsLoadedSuccessfully(Set<PostShoppingBasket> postShoppingBasket);

        void onSBPostDeletedSuccessfully(PostShoppingBasket postShoppingBasket);

        void onSBPostDeletingFailed(PostShoppingBasket postShoppingBasket, Throwable t);

        void onSBPostUpdatedSuccessfully(PostShoppingBasket postShoppingBasket);

        void onSBPostUpdatingFailed(PostShoppingBasket postShoppingBasket, Throwable t);
    }
}
