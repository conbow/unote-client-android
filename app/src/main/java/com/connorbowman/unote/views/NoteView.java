package com.connorbowman.unote.views;

import com.connorbowman.unote.fragments.callbacks.BaseCallback;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.NotePresenter;
import com.connorbowman.unote.views.common.TaskView;

public interface NoteView extends TaskView<BaseCallback, NotePresenter> {

    void onFinished(Note note);
    void clearErrors();
    void setTitleError(int feedback);
    void setBodyError(int feedback);
}
