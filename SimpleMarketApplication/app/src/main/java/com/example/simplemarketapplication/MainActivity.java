package com.example.simplemarketapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemarketapplication.dialogFragments.CreatePostDialogFragment;
import com.example.simplemarketapplication.dialogFragments.CreateUserDialogFragment;
import com.example.simplemarketapplication.dialogFragments.EditPostDialogFragment;
import com.example.simplemarketapplication.dialogFragments.EditUserPostDialogFragment;
import com.example.simplemarketapplication.fragments.PostAdminShoppingBasketFragment;
import com.example.simplemarketapplication.fragments.PostProductListFragment;
import com.example.simplemarketapplication.fragments.PostUserShoppingBasketFragment;
import com.example.simplemarketapplication.fragments.PostUsersDatabaseFragment;
import com.example.simplemarketapplication.posts.PostsModule;
import com.example.simplemarketapplication.posts.post.PostProduct;
import com.example.simplemarketapplication.posts.post.PostUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements CreatePostDialogFragment.Listener,
        EditPostDialogFragment.Listener, CreateUserDialogFragment.Listener,
        NavigationView.OnNavigationItemSelectedListener, EditUserPostDialogFragment.Listener {

    private static final String ADMIN_PASSWORD = "12";

    private final AuthModule mAuthModule = AuthModule.getInstance();
    private final PostsModule mPostModule = PostsModule.getInstance();

    private final AuthModule.Listener mAuthListener = new AuthModule.Listener() {
        @Override
        public void onStateChanged(AuthModule.State state) {
            switch (state) {
                case CLIENT_PRODUCTS_LIST:
                    mUserBasketMenuItem.setEnabled(true);
                    mProductListMenuItem.setEnabled(false);
                    mAdminAuthMenuItem.setEnabled(true);
                    mAdminBasketMenuItem.setEnabled(false);
                    mUsersTableMenuItem.setEnabled(false);
                    mUserAuthMenuItem.setEnabled(false);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mDeleteProductButton.setVisibility(View.GONE);
                    mUserAuth.setText(R.string.buyer_auth);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_product_view, new PostProductListFragment())
                            .commit();
                    break;
                case ADMINISTRATOR_PRODUCTS_LIST:
                    mUserBasketMenuItem.setEnabled(false);
                    mProductListMenuItem.setEnabled(false);
                    mAdminAuthMenuItem.setEnabled(false);
                    mAdminBasketMenuItem.setEnabled(true);
                    mUsersTableMenuItem.setEnabled(true);
                    mUserAuthMenuItem.setEnabled(true);
                    mFloatingActionButton.setVisibility(View.VISIBLE);
                    mDeleteProductButton.setVisibility(View.VISIBLE);
                    mUserAuth.setText(R.string.admin_auth);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_product_view, new PostProductListFragment())
                            .commit();
                    break;
                case CLIENT_ORDER_LIST:
                    mUserBasketMenuItem.setEnabled(false);
                    mProductListMenuItem.setEnabled(true);
                    mAdminAuthMenuItem.setEnabled(true);
                    mAdminBasketMenuItem.setEnabled(false);
                    mUsersTableMenuItem.setEnabled(false);
                    mUserAuthMenuItem.setEnabled(false);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mUserAuth.setText(R.string.buyer_auth);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_product_view, new PostUserShoppingBasketFragment())
                            .commit();
                    break;
                case ADMINISTRATOR_ORDER_LIST:
                    mUserBasketMenuItem.setEnabled(false);
                    mProductListMenuItem.setEnabled(true);
                    mAdminAuthMenuItem.setEnabled(false);
                    mAdminBasketMenuItem.setEnabled(false);
                    mUsersTableMenuItem.setEnabled(true);
                    mUserAuthMenuItem.setEnabled(true);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mUserAuth.setText(R.string.admin_auth);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_product_view, new PostAdminShoppingBasketFragment())
                            .commit();
                    break;
                case ADMINISTRATOR_USERS_LIST:
                    mUserBasketMenuItem.setEnabled(false);
                    mProductListMenuItem.setEnabled(true);
                    mAdminAuthMenuItem.setEnabled(false);
                    mAdminBasketMenuItem.setEnabled(true);
                    mUsersTableMenuItem.setEnabled(false);
                    mUserAuthMenuItem.setEnabled(true);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mUserAuth.setText(R.string.admin_auth);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_product_view, new PostUsersDatabaseFragment())
                            .commit();
            }
        }
    };

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFloatingActionButton;
    private MenuItem mUserBasketMenuItem, mProductListMenuItem, mAdminAuthMenuItem,
    mAdminBasketMenuItem, mUsersTableMenuItem, mUserAuthMenuItem;
    private ImageButton mDeleteProductButton, mCreateUserBasketButton;
    private TextView mUserAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.view_drawer_layout);

        mDeleteProductButton = findViewById(R.id.delete_button_view);
        //mCreateUserBasketButton = findViewById(R.id.basket_button_view);

        final Toolbar toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mFloatingActionButton = findViewById(R.id.view_fab);
        mFloatingActionButton.setOnClickListener(v ->
                new CreatePostDialogFragment().show(getSupportFragmentManager(),"CreateSomePost"));

        final NavigationView navigationView = findViewById(R.id.view_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        View mHeaderView = navigationView.getHeaderView(0);
        mUserAuth = mHeaderView.findViewById(R.id.view_authenticated);

        mUserBasketMenuItem = navigationView.getMenu().findItem(R.id.nav_basket_client);
        mProductListMenuItem = navigationView.getMenu().findItem(R.id.nav_product_list);
        mAdminAuthMenuItem = navigationView.getMenu().findItem(R.id.nav_admin);
        mAdminBasketMenuItem = navigationView.getMenu().findItem(R.id.nav_basket_admin);
        mUsersTableMenuItem = navigationView.getMenu().findItem(R.id.nav_users_admin);
        mUserAuthMenuItem = navigationView.getMenu().findItem(R.id.nav_client);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mAuthModule.addListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthModule.removeListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.nav_basket_client) {
            mAuthModule.stateClientBasket();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_product_view, new PostUserShoppingBasketFragment())
                    .commit();
        } else if (id == R.id.nav_product_list) {
            if (mAuthModule.getState() == AuthModule.State.CLIENT_ORDER_LIST) {
                mAuthModule.stateClientProduct();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_product_view, new PostProductListFragment())
                        .commit();
            } else {
                mAuthModule.stateAdminProduct();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_product_view, new PostProductListFragment())
                        .commit();
            }
        } else if (id == R.id.nav_admin) {
            warningDialog().show();
        } else if (id == R.id.nav_basket_admin) {
            mAuthModule.stateAdminBasket();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_product_view, new PostAdminShoppingBasketFragment())
                    .commit();
        } else if (id == R.id.nav_users_admin) {
            mAuthModule.stateAdminUsers();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_product_view, new PostAdminShoppingBasketFragment())
                    .commit();
        } else if (id == R.id.nav_client) {
            mAuthModule.stateClientProduct();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_product_view, new PostProductListFragment())
                    .commit();
        } else if (id == R.id.nav_exit) finish();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreatePost(final CreatePostDialogFragment fragment, final String name, final String price) {
        PostProduct postProduct = new PostProduct(name, price);
        mPostModule.createProduct(postProduct);
    }

    @Override
    public void onCreateUser(CreateUserDialogFragment fragment, String name, String phone) {
        PostUser postUser = new PostUser(name, phone);
        mPostModule.createUser(postUser);
    }

    @Override
    public void onEditPost(EditPostDialogFragment fragment, String name, String price) {
        PostProduct post = fragment.getPost();
        post.setDescription(name);
        post.setPrice(price);
        mPostModule.updateProduct(post);
    }

    @Override
    public void onEditUserPost(EditUserPostDialogFragment fragment, String name, String phone) {
        PostUser post = fragment.getPostUser();
        post.setUserName(name);
        post.setPhoneNumber(phone);
        mPostModule.updateUsers(post);
    }

    private AlertDialog warningDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View passwordInputView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.fragment_admin_password, null, false);

        TextInputEditText passwordInput = passwordInputView.findViewById(R.id.admin_password_input_view);

        builder
                .setMessage(R.string.password_request)
                .setPositiveButton(R.string.ok_button, (dialog, which) -> {
                    final String password = passwordInput.getText().toString();
                    if (password.equals(ADMIN_PASSWORD)) {
                        mAuthModule.stateAdminProduct();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_product_view, new PostProductListFragment())
                                .commit();
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel())
                .setView(passwordInputView);
        return builder.create();
    }
}
