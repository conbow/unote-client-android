package com.connorbowman.unote.presenters;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Account;
import com.connorbowman.unote.presenters.common.TaskPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.utils.ValidationUtil;
import com.connorbowman.unote.views.ForgotPasswordView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPresenter extends TaskPresenter<ForgotPasswordView> {

    public static final String TAG = LogUtil.makeTag(ForgotPasswordPresenter.class);

    private Call<Account> mCall;

    public void resetPassword(String email) {
        // Validate input
        getView().clearErrors();
        boolean hasErrors = false;
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

            mCall = getApi().accountForgotPost(email);
            mCall.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.code() == 409) {
                        setFeedback(R.string.email_is_not_registered);
                    } else if (!response.isSuccessful()) {
                        setFeedback(R.string.an_unkown_error_has_occurred);
                    } else {
                        setFeedback(R.string.password_reset_email_sent);
                    }
                    endTask(response.isSuccessful());
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    setFeedback(R.string.network_error);
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
