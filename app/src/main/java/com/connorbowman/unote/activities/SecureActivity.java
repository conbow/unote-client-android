package com.connorbowman.unote.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.NoteFragment;
import com.connorbowman.unote.fragments.NotesFragment;
import com.connorbowman.unote.fragments.SettingsFragment;
import com.connorbowman.unote.fragments.callbacks.SecureCallback;
import com.connorbowman.unote.models.Session;
import com.connorbowman.unote.utils.SessionUtil;

public class SecureActivity extends BaseActivity implements SecureCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFab;
    private MenuItem mPreviousMenuItem;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
        initializeToolbar(this);
        initializeAuth(true);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNote(null);
            }
        });

        // Setup nav drawer information
        ImageView navDrawerPhoto = (ImageView) mNavigationHeader.findViewById(R.id.nav_drawer_photo);
        TextView navDrawerName = (TextView) mNavigationHeader.findViewById(R.id.nav_drawer_name);
        TextView navDrawerEmail = (TextView) mNavigationHeader.findViewById(R.id.nav_drawer_email);
        Session session = SessionUtil.read(this);
        navDrawerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSettings();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        navDrawerName.setText(session.getName());
        navDrawerEmail.setText(session.getEmail());

        // Show initial fragment
        if (savedInstanceState == null) {
            navigateToNotes();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        int id = item.getItemId();

        // TODO Temporary fix for issue with item selecting
        item.setCheckable(true);
        item.setChecked(true);
        if (mPreviousMenuItem != null) {
            mPreviousMenuItem.setChecked(false);
        }
        mPreviousMenuItem = item;

        if (id == R.id.nav_notes) {
            navigateToNotes();
        } else if (id == R.id.nav_settings) {
            navigateToSettings();
        } else if (id == R.id.nav_logout) {
            navigateToLogout();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void navigateToNotes() {

        // Select option in nav drawer
        // TODO Temporary fix for issue with item selecting
        mNavigationView.post(new Runnable() {
            @Override
            public void run() {
                mNavigationView.setCheckedItem(R.id.nav_notes);
            }
        });

        NotesFragment fragment = (NotesFragment) findFragment(NotesFragment.TAG);
        fragment = new NotesFragment();
        pushFragment(new NotesFragment(), NotesFragment.TAG, null, false);
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mFab.show();
        }
    }

    @Override
    public void navigateToNote(String id) {
        mFab.hide();
        String title;
        NoteFragment fragment = new NoteFragment();
        if (id != null) {
            title = getString(R.string.edit_note);
            Bundle bundle = new Bundle();
            bundle.putString(NoteFragment.KEY_ID, id);
            fragment.setArguments(bundle);
        } else {
            title = getString(R.string.create_note);
        }
        pushFragment(fragment, NoteFragment.TAG, title, true);
    }

    @Override
    public void navigateToSettings() {
        mFab.hide();
        pushFragment(new SettingsFragment(), SettingsFragment.TAG, SettingsFragment.TITLE, false);
    }

    @Override
    public void navigateToLogout() {
        SessionUtil.destroy(this);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FEEDBACK, getString(R.string.successfully_logged_out));
        pushActivity(new LoginActivity(), bundle);
    }

    private void toggleFab() {
        if (findFragment(NotesFragment.TAG) != null
                && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mFab.show();
        } else {
            mFab.hide();
        }
    }

    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    toggleFab();
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
    }
}
