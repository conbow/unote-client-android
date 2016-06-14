package com.connorbowman.unote.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.connorbowman.unote.BuildConfig;
import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.LoginCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.models.Session;
import com.connorbowman.unote.presenters.LoginPresenter;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.utils.SessionUtil;
import com.connorbowman.unote.views.LoginView;

public class LoginFragment extends BaseFragment<LoginCallback, LoginView, LoginPresenter>
        implements LoginView {

    public static final String TAG = LogUtil.makeTag(LoginFragment.class);

    private TextInputLayout mInputEmailLayout;
    private TextInputEditText mInputEmail;
    private TextInputLayout mInputPasswordLayout;
    private TextInputEditText mInputPassword;
    private Button mButtonLogin;
    private ProgressBar mProgressBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        setCallback((LoginCallback) getContext());
        setView(this);
        setPresenter((LoginPresenter) PresenterManager.getInstance()
                .get(LoginPresenter.TAG, new LoginPresenter()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mInputEmailLayout = (TextInputLayout) view.findViewById(R.id.input_email_layout);
        mInputEmail = (TextInputEditText) view.findViewById(R.id.input_email);
        mInputPasswordLayout = (TextInputLayout) view.findViewById(R.id.input_password_layout);
        mInputPassword = (TextInputEditText) view.findViewById(R.id.input_password);
        mButtonLogin = (Button) view.findViewById(R.id.button_login);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (BuildConfig.DEBUG) {
            mInputEmail.setText("connorbowm@gmail.com");
            mInputPassword.setText("Test1234");
        }

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(mInputEmail.getText().toString(), mInputPassword.getText().toString());
            }
        });

        Button buttonSignUp = (Button) view.findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.navigateToSignUp();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_login, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_forgot_password) {
            mCallback.navigateToForgotPassword(mInputEmail.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clearErrors() {
        mInputEmailLayout.setError(null);
        mInputPasswordLayout.setError(null);
    }

    @Override
    public void setEmailError(int feedback) {
        mInputEmailLayout.setError(getString(feedback));
        mInputEmailLayout.requestFocus();
    }

    @Override
    public void setPasswordError(int feedback) {
        mInputPasswordLayout.setError(getString(feedback));
        mInputPasswordLayout.requestFocus();
    }

    @Override
    public void showLoading(boolean loading) {
        if (loading) {
            mButtonLogin.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mButtonLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinished() {
        mCallback.navigateToNotes();
    }

    @Override
    public void onFinished(Session session) {
        SessionUtil.store(getContext(), session);
    }

    @Override
    public void showFeedbackWithAction(int feedback) {
        showFeedback(getString(feedback), getString(R.string.forgot), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.navigateToForgotPassword(mInputEmail.getText().toString());
            }
        });
    }
}
