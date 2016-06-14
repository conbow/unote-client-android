package com.connorbowman.unote.fragments.common;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.presenters.common.BasePresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.common.BaseView;

public abstract class BaseFragment<C extends BaseCallback, V extends BaseView,
        P extends BasePresenter<V>> extends Fragment implements BaseView<C, P> {

    protected C mCallback;
    private V mView;
    protected P mPresenter;
    private boolean mPresenterAutoStart = true;

    @Override
    public void setCallback(C callback) {
        mCallback = callback;
    }

    public void setView(V view) {
        mView = view;
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    protected void setPresenterAutoStart(boolean autoStart) {
        mPresenterAutoStart = autoStart;
    }

    @Override
    public void showFeedback(int feedback) {
        showFeedback(getString(feedback));
    }

    @Override
    public void showFeedback(String feedback) {
        if (feedback == null) {
            feedback = getString(R.string.an_unkown_error_has_occurred);
        }
        View view = getView();
        if (view != null) {
            Snackbar.make(view, feedback, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showFeedbackWithAction(int feedback) {}

    protected void showFeedback(String feedback, String action, View.OnClickListener listener) {
        if (feedback == null) {
            feedback = getString(R.string.an_unkown_error_has_occurred);
        }
        View view = getView();
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, feedback, Snackbar.LENGTH_LONG);
            if (listener != null) {
                snackbar.setAction(action, listener);
            }
            snackbar.show();
        }
    }

    @Override
    public void checkAuth() {
        mCallback.checkAuth();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.attachView(mView);
            if (mPresenterAutoStart) {
                mPresenter.onStart();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.detachView();
            if (isRemoving()) {
                mPresenter.onStop();
                PresenterManager.getInstance().remove(LogUtil.makeTag(mPresenter.getClass()));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRemoving()) {
            mCallback = null;
            mPresenter = null;
        }
    }
}
