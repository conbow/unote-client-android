package com.connorbowman.unote.presenters.common;

import com.connorbowman.unote.services.ApiService;
import com.connorbowman.unote.views.common.BaseView;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends BaseView> implements Presenter<V> {

    private WeakReference<V> mView;
    private ApiService.Api mApi;

    @Override
    public void attachView(V view) {
        mView = new WeakReference<>(view);
        mApi = ApiService.getInstance();
    }

    @Override
    public void onStart() {
        if (hasView()) {
            getView().checkAuth();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean hasView() {
        return mView != null;
    }

    @Override
    public V getView() {
        if (mView != null) {
            return mView.get();
        } else {
            return null;
        }
    }

    public ApiService.Api getApi() {
        return mApi;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        mApi = null;
    }
}
