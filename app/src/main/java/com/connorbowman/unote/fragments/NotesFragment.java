package com.connorbowman.unote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.connorbowman.unote.R;
import com.connorbowman.unote.adapters.NotesAdapter;
import com.connorbowman.unote.fragments.callbacks.SecureCallback;
import com.connorbowman.unote.fragments.common.BaseFragment;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.presenters.NotesPresenter;
import com.connorbowman.unote.presenters.PresenterManager;
import com.connorbowman.unote.utils.LogUtil;
import com.connorbowman.unote.utils.SwipeRefreshUtil;
import com.connorbowman.unote.views.NotesView;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends BaseFragment<SecureCallback, NotesView, NotesPresenter>
        implements NotesView {

    public static final String TAG = LogUtil.makeTag(NotesFragment.class);

    private NotesAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mViewNoNotes;
    private View mViewNotes;
    private ProgressBar mProgressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListAdapter = new NotesAdapter(new ArrayList<Note>(0), mItemListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        setCallback((SecureCallback) getContext());
        setView(this);
        setPresenter((NotesPresenter) PresenterManager.getInstance()
                .get(NotesPresenter.TAG, new NotesPresenter()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Notes adapter
        ListView listView = (ListView) view.findViewById(R.id.notes_list);
        listView.setAdapter(mListAdapter);

        mViewNotes = view.findViewById(R.id.view_notes);
        mViewNoNotes = view.findViewById(R.id.view_no_notes);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNotes(true);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showSortMenu();
                break;
            case R.id.menu_refresh:
                mPresenter.loadNotes(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notes, menu);
    }

    /**
     * TODO Sorting
     */
    public void showSortMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.menu_sort_notes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_date_created:
                        mPresenter.setSortBy(SortBy.DATE_CREATED);
                        break;
                    case R.id.sort_date_modified:
                        mPresenter.setSortBy(SortBy.DATE_MODIFIED);
                        break;
                    default:
                        mPresenter.setSortBy(SortBy.TITLE);
                        break;
                }
                mPresenter.loadNotes(false);
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void showLoading(boolean loading) {
        mSwipeRefreshLayout.setRefreshing(loading);
        // FIXME Temporary fix
        SwipeRefreshUtil.setRefreshing(mSwipeRefreshLayout, loading);
        if (loading) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFinished() {
        //onFinished(null);
    }

    @Override
    public void onFinished(List<Note> notes) {
        if (notes == null) {
            mViewNotes.setVisibility(View.GONE);
            mViewNoNotes.setVisibility(View.VISIBLE);
        } else {
            mViewNotes.setVisibility(View.VISIBLE);
            mViewNoNotes.setVisibility(View.GONE);
            mListAdapter.replaceData(notes);
        }
    }

    NotesAdapter.NoteItemListener mItemListener = new NotesAdapter.NoteItemListener() {
        @Override
        public void onNoteClicked(Note note) {
            mCallback.navigateToNote(note.getId());
        }
    };
}
