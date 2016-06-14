package com.connorbowman.unote.views;

import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.presenters.ForgotPasswordPresenter;
import com.connorbowman.unote.views.common.TaskView;

public interface ForgotPasswordView extends TaskView<BaseCallback, ForgotPasswordPresenter> {

    void clearErrors();
    void setEmailError(int feedback);
}
