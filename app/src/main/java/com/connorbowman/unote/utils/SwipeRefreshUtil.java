package com.connorbowman.unote.utils;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewTreeObserver;

public class SwipeRefreshUtil {

    /**
     * FIXME
     * Temporary fix for possible SwipeRefreshLayout.setRefreshing bug
     * See https://code.google.com/p/android/issues/detail?id=77712
     * Via http://stackoverflow.com/v
     */
    @SuppressWarnings("deprecation")
    public static void setRefreshing(final SwipeRefreshLayout view, final boolean show) {
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                view.setRefreshing(show);
                            }
                        });
    }
}
