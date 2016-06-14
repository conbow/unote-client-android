package com.connorbowman.unote.views;

import com.connorbowman.unote.fragments.callbacks.LoginCallback;
import com.connorbowman.unote.models.Session;
import com.connorbowman.unote.presenters.LoginPresenter;
import com.connorbowman.unote.views.common.TaskView;

public interface LoginView extends TaskView<LoginCallback, LoginPresenter> {

    void showFeedbackWithAction(int feedback);
    void clearErrors();
    void setEmailError(int feedback);
    void setPasswordError(int feedback);
    void onFinished(Session session);
}
