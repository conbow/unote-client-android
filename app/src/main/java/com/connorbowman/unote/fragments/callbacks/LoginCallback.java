package com.connorbowman.unote.fragments.callbacks;

public interface LoginCallback extends BaseCallback {

    void navigateToSignUp();
    void navigateToForgotPassword(String email);
    void navigateToNotes();
}
