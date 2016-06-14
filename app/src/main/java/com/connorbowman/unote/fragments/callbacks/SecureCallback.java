package com.connorbowman.unote.fragments.callbacks;

public interface SecureCallback extends BaseCallback {

    void navigateToNotes();
    void navigateToNote(String id);
    void navigateToSettings();
    void navigateToLogout();
}
