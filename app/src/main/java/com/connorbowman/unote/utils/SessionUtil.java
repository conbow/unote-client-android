package com.connorbowman.unote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Session;

public class SessionUtil {

    private static final String KEY_DEBUG = "DEBUG";
    private static final String KEY_SESSION = "SESSION";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_NAME = "NAME";

    public static void store(Context context, Session session) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, session.getToken());
        editor.putString(KEY_EMAIL, session.getEmail());
        editor.putString(KEY_NAME, session.getName());
        editor.apply();
        Log.d("TEST", "Storing session with email of " + session.getEmail() + " and token of " + session.getToken());


        // TODO
        // Testing storage to default
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.key_pref_session_token), session.getToken());
        editor.putString(context.getString(R.string.key_pref_email), session.getEmail());
        editor.putString(context.getString(R.string.key_pref_name), session.getName());
        editor.apply();
    }

    public static void destroy(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static Session read(Context context) {
        //TODO debugging
        if (isDebugging(context)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return new Session(
                    sharedPreferences.getString(context.getString(R.string.key_pref_session_token), null),
                    sharedPreferences.getString(context.getString(R.string.key_pref_email), null),
                    sharedPreferences.getString(context.getString(R.string.key_pref_name), null)
            );

        } else {
            SharedPreferences sharedpreferences = context.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE);
            return new Session(
                    sharedpreferences.getString(KEY_TOKEN, null),
                    sharedpreferences.getString(KEY_EMAIL, null),
                    sharedpreferences.getString(KEY_NAME, null)
            );
        }
    }

    public static boolean isAuthenticated(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE);
        return sharedpreferences.getString(KEY_TOKEN, null) != null;
    }

    public static boolean isDebugging(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.key_pref_debug), false);
    }
}
