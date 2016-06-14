package com.connorbowman.unote.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.connorbowman.unote.R;
import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.ForgotPasswordPresenter;
import com.connorbowman.unote.presenters.NotePresenter;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.NoteView;

public class NoteFragment extends BaseFragment<BaseCallback, NoteView, NotePresenter>
        implements NoteView {

    public static final String TAG = LogUtil.makeTag(NoteFragment.class);
    public static final String KEY_ID = "ID";

    private String mId;
    private TextInputLayout mInputTitleLayout;
    private TextInputEditText mInputTitle;
    private TextInputLayout mInputBodyLayout;
    private TextInputEditText mInputBody;
    private ProgressBar mProgressBar;
    private Menu mMenu;
    private MenuItem mMenuItemSave;
    private MenuItem mMenuItemDelete;
    private MenuItem mMenuItemProgress;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenterAutoStart(false);
        setHasOptionsMenu(true);

        setCallback((BaseCallback) getContext());
        setView(this);
        setPresenter((NotePresenter) PresenterManager.getInstance()
                .get(NotePresenter.TAG, new NotePresenter()));

        if (getArguments() != null) {
            mId = getArguments().getString(KEY_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mId != null && mMenu != null) {
            mPresenter.loadNote(mId);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);

        mMenu = menu;
        mMenuItemSave = menu.findItem(R.id.action_save);
        mMenuItemDelete = menu.findItem(R.id.action_delete);
        mMenuItemProgress = menu.findItem(R.id.action_progress);
        mMenuItemDelete.setVisible(mId != null);
        super.onCreateOptionsMenu(menu, inflater);

        mPresenter.onStart();
        if (mId != null) {
            mPresenter.loadNote(mId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mPresenter.saveNote(mId, mInputTitle.getText().toString(), mInputBody.getText().toString());
                return true;
            case R.id.action_delete:
                deleteNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mInputTitleLayout = (TextInputLayout) view.findViewById(R.id.input_title_layout);
        mInputTitle = (TextInputEditText) view.findViewById(R.id.input_title);
        mInputBodyLayout = (TextInputLayout) view.findViewById(R.id.input_body_layout);
        mInputBody = (TextInputEditText) view.findViewById(R.id.input_body);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        return view;
    }

    private void deleteNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_note);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mPresenter.deleteNote(mId);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public void clearErrors() {
        mInputTitleLayout.setError(null);
        mInputBodyLayout.setError(null);
    }

    @Override
    public void setTitleError(int feedback) {
        mInputTitleLayout.setError(getString(feedback));
        mInputTitleLayout.requestFocus();
    }

    @Override
    public void setBodyError(int feedback) {
        mInputBodyLayout.setError(getString(feedback));
        mInputBodyLayout.requestFocus();
    }

    @Override
    public void showLoading(boolean active) {
        if (active) {
            mInputTitleLayout.setVisibility(View.GONE);
            mInputBodyLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mMenuItemDelete.setVisible(false);
            mMenuItemSave.setVisible(false);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mInputTitleLayout.setVisibility(View.VISIBLE);
            mInputBodyLayout.setVisibility(View.VISIBLE);
            mMenuItemDelete.setVisible(mId != null);
            mMenuItemSave.setVisible(true);
        }
    }

    @Override
    public void onFinished() {
        mCallback.popFragment();
    }

    @Override
    public void onFinished(Note note) {
        mInputTitle.setText(note.getTitle());
        mInputBody.setText(note.getBody());
    }
}
