package com.vanlam.everynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanlam.everynotes.R;
import com.vanlam.everynotes.entities.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{
    private List<Note> noteList;

    public NotesAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note item = noteList.get(position);
        holder.setNote(item);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle, textSubtitle, textDateTime;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textTitle = (TextView) itemView.findViewById(R.id.textTitle);
            this.textSubtitle = (TextView) itemView.findViewById(R.id.textSubtitle);
            this.textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
        }

        public void setNote(Note note) {
            textTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            }
            else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());
        }

        public TextView getTextTitle() {
            return textTitle;
        }

        public void setTextTitle(TextView textTitle) {
            this.textTitle = textTitle;
        }

        public TextView getTextSubtitle() {
            return textSubtitle;
        }

        public void setTextSubtitle(TextView textSubtitle) {
            this.textSubtitle = textSubtitle;
        }

        public TextView getTextDateTime() {
            return textDateTime;
        }

        public void setTextDateTime(TextView textDateTime) {
            this.textDateTime = textDateTime;
        }
    }
}
