package com.vanlam.everynotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.vanlam.everynotes.R;
import com.vanlam.everynotes.adapters.NotesAdapter;
import com.vanlam.everynotes.dao.NoteDao;
import com.vanlam.everynotes.database.NotesDatabase;
import com.vanlam.everynotes.entities.Note;
import com.vanlam.everynotes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements NotesListener {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;    // Request code is used display all notes
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> noteList;
    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = (ImageView) findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        notesRecyclerView = (RecyclerView) findViewById(R.id.notesRecyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        /*
            The method is called by onCreate(). Meaning the application has just been started, thus displaying all notes from the database
            (so pass the request code as REQUEST_CODE_SHOW_NOTES)
         */
        getNotes(REQUEST_CODE_SHOW_NOTES);

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    // Async task to save a note
    public void getNotes(final int requestCode) {
        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                NoteDao noteDao = NotesDatabase.getDatabase(getApplicationContext()).noteDao();
                List<Note> listNotes = noteDao.getAllNotes();
                return listNotes;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {   // Request code is show notes so adding all notes from database to noteList and notify adapter about the new data set
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }
                else if (requestCode == REQUEST_CODE_ADD_NOTE) {    // Request code is add note, so adding only first note (newly added note) from the database to noteList and notify adapter for the newly inserted item
                    noteList.add(notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.scrollToPosition(0);
                }
                else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {     // Request code is update note, so remove note from the clicked position and adding the lasted update note from same position from the database
                    noteList.remove(noteClickedPosition);
                    noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }
            }
        }

        new GetNoteTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE);
        }
        else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }
    }
}