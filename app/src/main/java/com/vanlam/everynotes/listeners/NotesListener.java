package com.vanlam.everynotes.listeners;

import com.vanlam.everynotes.entities.Note;

public interface NotesListener {
    public void onNoteClicked(Note note, int position);
}
