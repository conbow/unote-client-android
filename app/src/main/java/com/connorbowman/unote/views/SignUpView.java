package com.connorbowman.unote.views;

import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.presenters.SignUpPresenter;
import com.connorbowman.unote.views.common.TaskView;

public interface SignUpView extends TaskView<BaseCallback, SignUpPresenter> {

    void clearErrors();
    void setEmailError(int feedback);
    void setPasswordError(int feedback);
    void setPasswordConfirmError(int feedback);
}
