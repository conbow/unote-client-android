package com.connorbowman.unote.presenters;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.common.TaskPresenter;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.views.NoteView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotePresenter extends TaskPresenter<NoteView> {

    public static final String TAG = LogUtil.makeTag(NotePresenter.class);

    private Call<Note> mCall;

    private Note mNote;

    @Override
    public void onStart() {
        if (hasView()) {
            if (mNote != null) {
                getView().onFinished(mNote);
            }
        }
        super.onStart();
    }

    public void loadNote(String id) {

        // Return note if it already exists
        if (mNote != null) {
            onStart();
            endTask(false);

        } else {

            startTask();

            mCall = getApi().noteGet(id);
            mCall.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if (!response.isSuccessful()) {
                        setFeedback(R.string.note_not_found, true);
                    } else {
                        mNote = response.body();
                    }
                    endTask(false);
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    setFeedback(R.string.network_error);
                    endTask(false);
                }
            });
        }
    }

    public void saveNote(String id, String title, String body) {
        // Validate input
        getView().clearErrors();
        boolean hasErrors = false;
        if (body.isEmpty()) {
            getView().setBodyError(R.string.body_is_required);
            hasErrors = true;
        }
        if (title.isEmpty()) {
            getView().setTitleError(R.string.title_is_required);
            hasErrors = true;
        }

        // Start task if no errors
        if (!hasErrors) {
            startTask();

            Note note = new Note(title, body);
            if (id == null) {
                mCall = getApi().notePost(note);
            } else {
                mCall = getApi().notePut(id, note);
            }
            mCall.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if (response.isSuccessful()) {
                        setFeedback(R.string.note_saved);
                    } else if (response.code() == 404) {
                        setFeedback(R.string.note_not_found);
                    } else {
                        setFeedback(R.string.an_unkown_error_has_occurred);
                    }
                    endTask(true);
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    setFeedback(R.string.network_error);
                    endTask(false);
                }
            });

        }
    }

    public void deleteNote(String id) {
        startTask();

        mCall = getApi().noteDelete(id);
        mCall.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (!response.isSuccessful()) {
                    setFeedback(R.string.note_not_found);
                } else {
                    setFeedback(R.string.note_deleted);
                }
                endTask(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                setFeedback(R.string.network_error);
                endTask(false);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }
}
