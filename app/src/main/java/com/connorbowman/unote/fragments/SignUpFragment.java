package com.connorbowman.unote.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.connorbowman.unote.BuildConfig;
import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.presenters.ForgotPasswordPresenter;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.presenters.SignUpPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.SignUpView;

public class SignUpFragment extends BaseFragment<BaseCallback, SignUpView, SignUpPresenter>
        implements SignUpView {

    public static final String TAG = LogUtil.makeTag(SignUpFragment.class);
    public static final int TITLE = R.string.sign_up;

    private TextInputLayout mInputEmailLayout;
    private TextInputEditText mInputEmail;
    private TextInputLayout mInputPasswordLayout;
    private TextInputEditText mInputPassword;
    private TextInputLayout mInputPasswordConfirmLayout;
    private TextInputEditText mInputPasswordConfirm;
    private Button mButtonSignUp;
    private ProgressBar mProgressBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCallback((BaseCallback) getContext());
        setView(this);
        setPresenter((SignUpPresenter) PresenterManager.getInstance()
                .get(SignUpPresenter.TAG, new SignUpPresenter()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mInputEmailLayout = (TextInputLayout) view.findViewById(R.id.input_email_layout);
        mInputEmail = (TextInputEditText) view.findViewById(R.id.input_email);
        mInputPasswordLayout = (TextInputLayout) view.findViewById(R.id.input_password_layout);
        mInputPassword = (TextInputEditText) view.findViewById(R.id.input_password);
        mInputPasswordConfirmLayout = (TextInputLayout) view.findViewById(R.id.input_password_confirm_layout);
        mInputPasswordConfirm = (TextInputEditText) view.findViewById(R.id.input_password_confirm);
        mButtonSignUp = (Button) view.findViewById(R.id.button_sign_up);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (BuildConfig.DEBUG) {
            mInputEmail.setText("connorbowm@gmail.com");
            mInputPassword.setText("Test1234");
            mInputPasswordConfirm.setText("Test1234");
        }

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.signUp(mInputEmail.getText().toString(), mInputPassword.getText().toString(),
                        mInputPasswordConfirm.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void clearErrors() {
        mInputEmailLayout.setError(null);
        mInputPasswordLayout.setError(null);
        mInputPasswordConfirmLayout.setError(null);
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
    public void setPasswordConfirmError(int feedback) {
        mInputPasswordConfirmLayout.setError(getString(feedback));
        mInputPasswordConfirmLayout.requestFocus();
    }

    @Override
    public void showLoading(boolean loading) {
        if (loading) {
            mButtonSignUp.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mButtonSignUp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinished() {
        mCallback.popFragment();
    }
}
