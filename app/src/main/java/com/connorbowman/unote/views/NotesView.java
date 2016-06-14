package com.connorbowman.unote.views;

import com.connorbowman.unote.fragments.callbacks.SecureCallback;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.NotesPresenter;
import com.connorbowman.unote.views.common.TaskView;

import java.util.List;

public interface NotesView extends TaskView<SecureCallback, NotesPresenter> {

    enum SortBy {
        TITLE,
        DATE_CREATED,
        DATE_MODIFIED
    }

    void onFinished(List<Note> notes);
}
