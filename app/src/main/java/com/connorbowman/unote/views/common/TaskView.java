package com.connorbowman.unote.views.common;

public interface TaskView<C, P> extends BaseView<C, P> {

    void showLoading(boolean loading);
    void onFinished();
}
