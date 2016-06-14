package com.connorbowman.unote.presenters;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Session;
import com.connorbowman.unote.presenters.common.TaskPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.utils.ValidationUtil;
import com.connorbowman.unote.views.LoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends TaskPresenter<LoginView> {

    public static final String TAG = LogUtil.makeTag(LoginPresenter.class);

    private Call<Session> mCall;

    private Session mSession;

    @Override
    public void onStart() {
        if (hasView()) {
            if (mSession != null) {
                getView().onFinished(mSession);
                mSession = null;
            }
        }
        super.onStart();
    }

    public void login(String email, String password) {
        // Validate input
        getView().clearErrors();
        boolean hasErrors = false;
        if (password.isEmpty()) {
            getView().setPasswordError(R.string.password_is_required);
            hasErrors = true;
        } else if (!ValidationUtil.isPassword(password)) {
            getView().setPasswordError(R.string.password_requirements);
            hasErrors = true;
        }
        if (email.isEmpty()) {
            getView().setEmailError(R.string.email_is_required);
            hasErrors = true;
        } else if (!ValidationUtil.isEmail(email)) {
            getView().setEmailError(R.string.email_is_invalid);
            hasErrors = true;
        }

        // Start task if no errors
        if (!hasErrors) {
            startTask();

            mCall = getApi().sessionPost(email, password);
            mCall.enqueue(new Callback<Session>() {
                @Override
                public void onResponse(Call<Session> call, Response<Session> response) {
                    if (response.isSuccessful()) {
                        mSession = response.body();
                    } else {
                        if (response.code() == 422) {
                            setFeedback(R.string.incorrect_login_information, true);
                        } else {
                            setFeedback(R.string.an_unkown_error_has_occurred);
                        }
                    }
                    endTask(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<Session> call, Throwable t) {
                    if (!call.isCanceled()) {
                        setFeedback(R.string.network_error);
                    }
                    endTask(false);
                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }
}
