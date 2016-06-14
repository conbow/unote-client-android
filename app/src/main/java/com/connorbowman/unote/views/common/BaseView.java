package com.connorbowman.unote.views.common;

public interface BaseView<C, P> {

    void setCallback(C callback);
    void setPresenter(P presenter);
    void showFeedback(int feedback);
    void showFeedback(String feedback);
    void showFeedbackWithAction(int feedback);
    void checkAuth();
}
