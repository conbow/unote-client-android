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
import com.connorbowman.unote.fragments.callbacks.LoginCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.presenters.ForgotPasswordPresenter;
import com.connorbowman.unote.presenters.LoginPresenter;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.ForgotPasswordView;

public class ForgotPasswordFragment extends BaseFragment<BaseCallback, ForgotPasswordView,
        ForgotPasswordPresenter> implements ForgotPasswordView {

    public static final String TAG = LogUtil.makeTag(ForgotPasswordFragment.class);
    public static final int TITLE = R.string.forgot_password;

    public static final String KEY_EMAIL = "EMAIL";

    private TextInputLayout mInputEmailLayout;
    private TextInputEditText mInputEmail;
    private Button mButtonReset;
    private ProgressBar mProgressBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCallback((BaseCallback) getContext());
        setView(this);
        setPresenter((ForgotPasswordPresenter) PresenterManager.getInstance()
                .get(ForgotPasswordPresenter.TAG, new ForgotPasswordPresenter()));

        if (getArguments() != null) {
            mInputEmail.setText(getArguments().getString(KEY_EMAIL));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        mInputEmailLayout = (TextInputLayout) view.findViewById(R.id.input_email_layout);
        mInputEmail = (TextInputEditText) view.findViewById(R.id.input_email);
        mButtonReset = (Button) view.findViewById(R.id.button_reset);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (BuildConfig.DEBUG) {
            mInputEmail.setText("connorbowm@gmail.com");
        }

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.resetPassword(mInputEmail.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void clearErrors() {
        mInputEmailLayout.setError(null);
    }

    @Override
    public void setEmailError(int feedback) {
        mInputEmailLayout.setError(getString(feedback));
        mInputEmailLayout.requestFocus();
    }

    @Override
    public void showLoading(boolean loading) {
        if (loading) {
            mButtonReset.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mButtonReset.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinished() {
        mCallback.popFragment();
    }
}
