package com.connorbowman.unote.presenters;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.common.TaskPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.NotesView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesPresenter extends TaskPresenter<NotesView> {

    public static final String TAG = LogUtil.makeTag(NotesPresenter.class);

    private Call<List<Note>> mCall;

    private NotesView.SortBy mSortBy;
    private List<Note> mNotes = null;

    @Override
    public void onStart() {
        if (hasView()) {
            getView().onFinished(mNotes);
        }
        super.onStart();
    }

    /**
     * TODO
     * @param type
     */
    public void setSortBy(NotesView.SortBy type) {
        mSortBy = type;
        switch (mSortBy) {
            case TITLE:
                break;
            case DATE_CREATED:
                break;
            case DATE_MODIFIED:
                break;
        }
        if (mNotes != null) {
            Collections.sort(mNotes, new Comparator<Note>() {
                @Override
                public int compare(Note note1, Note note2) {
                    return note1.getTitle().compareToIgnoreCase(note2.getTitle());
                }
            });
        }
    }

    public void loadNotes() {
        loadNotes(false);
    }

    public void loadNotes(boolean forceUpdate) {

        if (mNotes != null && !forceUpdate) {
            endTask(true);

        } else {
            startTask();

            mCall = getApi().notesGet();
            mCall.enqueue(new Callback<List<Note>>() {
                @Override
                public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                    if (!response.isSuccessful()) {
                        setFeedback(R.string.an_unkown_error_has_occurred, true);
                    } else {
                        mNotes = response.body();
                    }
                    endTask(true);
                }

                @Override
                public void onFailure(Call<List<Note>> call, Throwable t) {
                    setFeedback(R.string.network_error);
                    endTask(true);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }
}
