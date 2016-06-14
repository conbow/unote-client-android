package com.connorbowman.unote.presenters;

import android.util.Log;

import com.connorbowman.unote.presenters.common.Presenter;

import java.util.HashMap;

public class PresenterManager {

    private static PresenterManager sInstance;

    private static final String KEY_PRESENTER_ID = "PRESENTER_ID";

    private HashMap<String, Presenter<?>> mPresenters = new HashMap<>();

    public static PresenterManager getInstance() {
        if (sInstance == null) {
            sInstance = new PresenterManager();
        }
        return sInstance;
    }

    public Presenter add(String tag, Presenter presenter) {
        return mPresenters.put(tag, presenter);
    }

    public Presenter get(String tag, Presenter presenter) {
        Log.d("PresenterManager", "there are currently " + mPresenters.size() + " presenters");
        Log.d("PresenterManager", "they are " + mPresenters.get(LoginPresenter.TAG) + " and " + mPresenters.get(NotesPresenter.TAG));
        Log.d("PresenterManager", "get(" + tag + ", " + presenter + ")");
        if (mPresenters.get(tag) != null) {
            // Presenter already exists, return it
            return mPresenters.get(tag);
        } else {
            // Create new presenter
            mPresenters.put(tag, presenter);
            return presenter;
        }
    }

    public void remove(String tag) {
        mPresenters.remove(tag);
    }

    public void remove(Presenter presenter) {
        mPresenters.remove(presenter);
    }

    public void clear() {
        mPresenters.clear();
    }
}
