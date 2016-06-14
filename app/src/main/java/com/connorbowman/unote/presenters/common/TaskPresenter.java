package com.connorbowman.unote.presenters.common;

import com.connorbowman.unote.views.common.TaskView;

public class TaskPresenter<V extends TaskView> extends BasePresenter<V> {

    private boolean mFinished;
    private boolean mRunning;
    private boolean mShowFeedback;
    private boolean mShowFeedbackAction;
    private int mFeedback;

    @Override
    public void onStart() {
        super.onStart();
        if (hasView()) {
            getView().showLoading(mRunning);
            if (mFinished) {
                getView().onFinished();
                mFinished = false;
            }
            if (mShowFeedback) {
                if (mShowFeedbackAction) {
                    getView().showFeedbackWithAction(mFeedback);
                } else {
                    getView().showFeedback(mFeedback);
                }
                mShowFeedback = false;
                mShowFeedbackAction = false;
            }
        }
    }

    protected void startTask() {
        mRunning = true;
        getView().showLoading(true);
    }

    protected void setFeedback(int feedback) {
        mFeedback = feedback;
        mShowFeedback = true;
    }

    protected void setFeedback(int feedback, boolean action) {
        mFeedback = feedback;
        mShowFeedback = true;
        mShowFeedbackAction = action;
    }

    protected void endTask(boolean success) {
        mRunning = false;
        mFinished = success;
        onStart();
    }
}
