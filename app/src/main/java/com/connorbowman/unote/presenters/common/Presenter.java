package com.connorbowman.unote.presenters.common;

import com.connorbowman.unote.views.common.BaseView;

public interface Presenter<V extends BaseView> {

    void attachView(V view);
    void onStart();
    boolean hasView();
    V getView();
    void detachView();
    void onStop();
}
