package com.connorbowman.unote.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.presenters.common.Presenter;
import com.connorbowman.unote.utils.SessionUtil;

public class BaseActivity extends AppCompatActivity implements BaseCallback {

    public static final String KEY_FEEDBACK = "FEEDBACK";

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected NavigationView mNavigationView;
    protected DrawerLayout mDrawerLayout;
    protected View mNavigationHeader;
    private boolean mRequireAuth = false;
    protected Snackbar mSnackbar;

    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    updateToolbar();
                }
            };

    @Override
    public void onStart() {
        super.onStart();

        // Check for feedback to be displayed from intent bundle
        View view = findViewById(R.id.content);
        String feedback = getIntent().getStringExtra(KEY_FEEDBACK);
        if (view != null && feedback != null) {
            Snackbar.make(view, feedback, Snackbar.LENGTH_LONG).show();
            getIntent().removeExtra(KEY_FEEDBACK);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);

        // Update toolbar
        if (mToolbar != null) {
            updateToolbar();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation drawer toggle click
        if (getSupportFragmentManager().getBackStackEntryCount() == 0
                && mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle Toolbar back button click
        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close drawer if open
            mDrawerLayout.closeDrawers();
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            // Go back to fragment in back stack if needed
            getSupportFragmentManager().popBackStack();
        } else {
            // Otherwise use system back behaviour
            super.onBackPressed();
        }
    }

    protected void initializeToolbar() {
        initializeToolbar(null);
    }

    protected void initializeToolbar(NavigationView.OnNavigationItemSelectedListener listener) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Initialize Drawer, if exists
        if (mDrawerLayout != null) {
            mNavigationView = (NavigationView) findViewById(R.id.nav_view);
            if (mNavigationView != null) {
                mNavigationHeader = mNavigationView.getHeaderView(0);
                mNavigationView.setNavigationItemSelectedListener(listener);
            }
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(mDrawerListener);
        }

        // Initialize Toolbar
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        mActionBar = getSupportActionBar();
    }

    protected void updateToolbar() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        boolean isRoot = backStackEntryCount == 0;

        // Disable drawer if not in root fragment
        if (mDrawerLayout != null) {
            if (isRoot) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }

        // Toggle toolbar back button, animate the toggle if SDK supports it
        if (mDrawerToggle != null && Build.VERSION.SDK_INT >= 11) {
            ObjectAnimator.ofFloat(mToolbar.getNavigationIcon(), "progress", isRoot ? 0 : 1).start();
        } else {
            if (mDrawerToggle != null) {
                mDrawerToggle.setDrawerIndicatorEnabled(isRoot);
            }
            if (mActionBar != null) {
                mActionBar.setDisplayShowHomeEnabled(!isRoot);
                mActionBar.setDisplayHomeAsUpEnabled(!isRoot);
                mActionBar.setHomeButtonEnabled(!isRoot);
            }
        }
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

        if (mActionBar != null) {
            String title;
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
            //if (fragment != null) {
                title = fragment.getArguments().getString(KEY_TITLE);
            //}
            if (title != null) {
                mActionBar.setTitle(title);
            }
            //mActionBar.setTitle(getCurrentFragment().getArguments().getString(BaseFragment.KEY_TITLE));
        }
    }

    private final DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerClosed(View drawerView) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerStateChanged(newState);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerOpened(drawerView);
        }
    };

    @Override
    public void pushActivity(Activity activity) {
        pushActivity(activity, null);
    }

    @Override
    public void pushActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(this, activity.getClass());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public Fragment findFragment(String tag) {
        if (tag == null || tag.isEmpty()) {
            tag = getString(R.string.app_name);
        }
        return getSupportFragmentManager().findFragmentByTag(tag);
    }


    @Override
    public void pushFragment(Fragment fragment, String tag, int title, boolean addToBackStack) {
        pushFragment(fragment, tag, getString(title), addToBackStack);
    }

    @Override
    public void pushFragment(Fragment fragment, String tag, String title, boolean addToBackStack) {

        // Find any fragment with same tag to ensure uniqueness
        if (findFragment(tag) == null) {

            // If title is empty set it to app name
            if (title == null || title.isEmpty()) {
                title = getString(R.string.app_name);
            }

            // Store title in fragment arguments
            Bundle bundle;
            if (fragment.getArguments() != null) {
                bundle = fragment.getArguments();
            } else {
                bundle = new Bundle();
            }
            bundle.putString(KEY_TITLE, title);
            fragment.setArguments(bundle);

            // Fragment transaction
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            if (addToBackStack) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.replace(R.id.content, fragment, tag);
            fragmentTransaction.commit();
            if (mActionBar != null) {
                mActionBar.setTitle(title);
            }
        }
    }

    @Override
    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void checkAuth() {
        if (mRequireAuth && !SessionUtil.isAuthenticated(this)) {
            SessionUtil.destroy(this);
            Bundle bundle = new Bundle();
            bundle.putString(KEY_FEEDBACK, getString(R.string.your_session_has_expired));
            pushActivity(new LoginActivity(), bundle);
        }
    }

    public void initializeAuth(boolean requireAuth) {
        mRequireAuth = requireAuth;
        boolean hasAuth = SessionUtil.isAuthenticated(this);
        Bundle bundle = new Bundle();
        if (!mRequireAuth && hasAuth) {
            pushActivity(new SecureActivity());
        } else if (!hasAuth && mRequireAuth) {
            bundle.putString(KEY_FEEDBACK, getString(R.string.your_session_has_expired));
            pushActivity(new LoginActivity());
        }
    }
}
