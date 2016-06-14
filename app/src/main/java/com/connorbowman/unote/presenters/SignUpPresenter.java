package com.connorbowman.unote.presenters;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Account;
import com.connorbowman.unote.presenters.common.TaskPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.utils.ValidationUtil;
import com.connorbowman.unote.views.SignUpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter extends TaskPresenter<SignUpView> {

    public static final String TAG = LogUtil.makeTag(SignUpPresenter.class);

    private Call<Account> mCall;

    public void signUp(String email, String password, String passwordConfirm) {
        // Validate input
        getView().clearErrors();
        boolean hasErrors = false;
        if (!password.matches(passwordConfirm)) {
            getView().setPasswordError(R.string.passwords_dont_match);
            getView().setPasswordConfirmError(R.string.passwords_dont_match);
            hasErrors = true;
        }
        if (passwordConfirm.isEmpty()) {
            getView().setPasswordConfirmError(R.string.confirm_password_is_required);
            hasErrors = true;
        } else if (!ValidationUtil.isPassword(passwordConfirm)) {
            getView().setPasswordConfirmError(R.string.password_requirements);
            hasErrors = true;
        }
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

            Account account = new Account(email, password);
            mCall = getApi().accountPost(account);
            mCall.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.code() == 409) {
                        setFeedback(R.string.email_is_already_in_use);
                    } else if (!response.isSuccessful()) {
                        setFeedback(R.string.an_unkown_error_has_occurred);
                    } else {
                        setFeedback(R.string.account_created);
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
