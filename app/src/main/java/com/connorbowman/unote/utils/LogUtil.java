package com.connorbowman.unote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LogUtil {

    private static final String LOG_PREFIX = "unote_";

    public static final String KEY_DEBUG = "DEBUG";

    public static boolean LOGGING_ENABLED = true;

    public static String makeTag(Class cls) {
        return LOG_PREFIX + cls.getSimpleName();
    }

    public static void LOGD(final String tag, String message) {
        if (LOGGING_ENABLED){
            if (Log.isLoggable(tag, Log.DEBUG)) {
                Log.d(tag, message);
            }
        }
    }

    public static boolean debugEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_DEBUG, false);
    }
}
