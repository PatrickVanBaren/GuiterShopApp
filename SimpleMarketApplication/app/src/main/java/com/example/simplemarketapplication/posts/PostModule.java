package com.example.simplemarketapplication.posts;

import com.example.simplemarketapplication.db.Database;
import com.example.simplemarketapplication.db.Tables.Products;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.example.simplemarketapplication.posts.post.PostShoppingBasket;
import com.example.simplemarketapplication.posts.post.PostUser;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PostModule {

    private final Database mDatabase = Database.getInstance();

    private final Set<PostModule.Listener> mListeners = Sets.newHashSet();
    private final Set<PostProduct> mPostsProduct = Sets.newTreeSet(new PostsComparator());
    private final Set<PostUser> mPostsUsers = Sets.newTreeSet(new UsersPostsComparator());
    private final Set<PostShoppingBasket> mPostsAdminShoppingBasket = Sets.newTreeSet(new SBPostsComparator());

    private State mState = State.IDLE;

    public void createProduct(final PostProduct postProduct) {
        createProductMethod(postProduct).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public Observable<PostProduct> createProductMethod(PostProduct product) {
        return Observable.create(subscriber -> {
            Random rnd = new Random();
            String id = Integer.toString(rnd.nextInt(99999));
            product.setId(id);
            product.setCreatedTime(new Date());
            mDatabase.createProduct(createProd(product));
            mPostsProduct.add(product);
            subscriber.onNext(product);
        });
    }

    private Products createProd(final PostProduct postProduct) {
        final Products product = new Products();
        product.setId(postProduct.getId());
        product.setTitle(postProduct.getDescription());
        product.setPrice(postProduct.getPrice());
        return product;
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
    }
}
