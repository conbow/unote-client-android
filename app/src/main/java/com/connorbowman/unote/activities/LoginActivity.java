package com.connorbowman.unote.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.ForgotPasswordFragment;
import com.connorbowman.unote.fragments.LoginFragment;
import com.connorbowman.unote.fragments.SignUpFragment;
import com.connorbowman.unote.fragments.callbacks.LoginCallback;
import com.connorbowman.unote.utils.SessionUtil;

public class LoginActivity extends BaseActivity implements LoginCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeToolbar();
        initializeAuth(false);
        navigateToLogin();
    }

    public void navigateToLogin() {
        LoginFragment fragment = (LoginFragment) findFragment(LoginFragment.TAG);
        if (fragment == null) {
            fragment = new LoginFragment();
            pushFragment(fragment, LoginFragment.TAG, null, false);
        }
    }

    @Override
    public void navigateToSignUp() {
        pushFragment(new SignUpFragment(), SignUpFragment.TAG, getString(SignUpFragment.TITLE), true);
    }

    @Override
    public void navigateToForgotPassword(String email) {
        Fragment fragment = new ForgotPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ForgotPasswordFragment.KEY_EMAIL, email);
        fragment.setArguments(bundle);
        pushFragment(fragment, ForgotPasswordFragment.TAG,
                getString(ForgotPasswordFragment.TITLE), true);
    }

    @Override
    public void navigateToNotes() {
        Bundle bundle = new Bundle();
        String name = SessionUtil.read(this).getName();
        if (name == null) {
            name = "";
        }
        bundle.putString(KEY_FEEDBACK, getString(R.string.welcome_back) + " " + name);
        pushActivity(new SecureActivity(), bundle);
    }
}
