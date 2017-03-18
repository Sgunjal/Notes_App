package com.example.sujay.assignment3_notes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sujay on 2/24/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<Note> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> empList, MainActivity ma) {
        this.noteList = empList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.note_name.setText(note.getNote_name());
        holder.note_date.setText(note.getNote_date());
        holder.note_text.setText(note.getNote_text());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
