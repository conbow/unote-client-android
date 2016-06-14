package com.connorbowman.unote.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connorbowman.unote.R;
import com.connorbowman.unote.models.Note;

import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private List<Note> mNotes;
    private NoteItemListener mItemListener;

    public NotesAdapter(List<Note> notes, NoteItemListener itemListener) {
        setList(notes);
        mItemListener = itemListener;
    }

    public void replaceData(List<Note> notes) {
        setList(notes);
        notifyDataSetChanged();
    }

    private void setList(List<Note> notes) {
        mNotes = notes;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Note getItem(int i) {
        return mNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.item_note, viewGroup, false);
        }

        final Note note = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.title);
        titleTV.setText(note.getTitle());

        TextView body = (TextView) rowView.findViewById(R.id.body);
        body.setText(note.getBody());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onNoteClicked(note);
            }
        });

        return rowView;
    }

    public interface NoteItemListener {

        void onNoteClicked(Note note);
    }
}
