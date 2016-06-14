package com.connorbowman.unote.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connorbowman.unote.BuildConfig;
import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.fragments.callbacks.SettingsCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.presenters.SettingsPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.common.TaskView;

public class SettingsFragment extends BaseFragment<BaseCallback, TaskView, SettingsPresenter>
        implements SettingsCallback {

    public static final String TAG = LogUtil.makeTag(SettingsFragment.class);
    public static final int TITLE = R.string.settings;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCallback((BaseCallback) getContext());
        //setView((TaskView) this);
        /*
        setPresenter((SettingsPresenter) PresenterManager.getInstance()
                .get(SettingsPresenter.TAG, new SettingsPresenter()));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    /*
    @Override
    public void showLoading(boolean loading) {

    }

    @Override
    public void onFinished() {

    }*/

    public void test() {
        Log.d(TAG, "this is from settingsfragment");
    }


    /**
     * SettingsPreferenceFragment
     */
    public static class SettingsPreferenceFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        private SettingsCallback mCallback;

        public SettingsPreferenceFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_settings);
            //SettingsUtil.registerOnSharedPreferenceChangeListener(getActivity(), this);

            // TODO DEBUG For debugging
            //if (BuildConfig.DEBUG) {
                addPreferencesFromResource(R.xml.preferences_debug);
            //}

            // Set callback to SettingsFragment
            mCallback = (SettingsCallback) getParentFragment();
            mCallback.test();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String tag) {
            EditTextPreference preferenceEmail = (EditTextPreference)
                    findPreference(getString(R.string.key_pref_email));
            if (preferenceEmail != null) {
                preferenceEmail.setSummary(preferenceEmail.getText());
            }
            /*
            PreferenceCategory debug = (PreferenceCategory)
                    findPreference(getString(R.string.key_pref_category_debug));
            debug.setVisible(false);*/

            Log.d(TAG, "tag is " + tag);

        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d(TAG, "sharedPref is " + sharedPreferences + " and key is " + key);



            //SessionUtil.store(getContext(), session);

            /*
            if (SettingsUtil.PREF_SYNC_CALENDAR.equals(key)) {
                Intent intent;
                if (SettingsUtils.shouldSyncCalendar(getActivity())) {
                    // Add all calendar entries
                    intent = new Intent(SessionCalendarService.ACTION_UPDATE_ALL_SESSIONS_CALENDAR);
                } else {
                    // Remove all calendar entries
                    intent = new Intent(SessionCalendarService.ACTION_CLEAR_ALL_SESSIONS_CALENDAR);
                }

                intent.setClass(getActivity(), SessionCalendarService.class);
                getActivity().startService(intent);
            }*/
        }

        /*

        @Override
        public void onResume() {
            super.onResume();

            // configure the fragment's top clearance to take our overlaid controls (Action Bar
            // and spinner box) into account.
            int actionBarSize = UIUtils.calculateActionBarSize(getActivity());
            DrawShadowFrameLayout drawShadowFrameLayout =
                    (DrawShadowFrameLayout) getActivity().findViewById(R.id.main_content);
            if (drawShadowFrameLayout != null) {
                drawShadowFrameLayout.setShadowTopOffset(actionBarSize);
            }
            setContentTopClearance(actionBarSize);
        }*/

        @Override
        public void onDestroy() {
            super.onDestroy();
            mCallback = null;
        }
    }
}
