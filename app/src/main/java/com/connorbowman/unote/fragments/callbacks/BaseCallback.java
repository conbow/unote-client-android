package com.connorbowman.unote.fragments.callbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface BaseCallback {

    String KEY_TITLE = "TITLE";

    void pushActivity(Activity activity);
    void pushActivity(Activity activity, Bundle bundle);
    Fragment findFragment(String tag);
    void pushFragment(Fragment fragment, String tag, int title, boolean addToBackStack);
    void pushFragment(Fragment fragment, String tag, String title, boolean addToBackStack);
    void popFragment();
    void checkAuth();
}
