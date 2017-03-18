package com.example.sujay.assignment3_notes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sujay on 2/24/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView note_name;
    public TextView note_date;
    public TextView note_text;

    public MyViewHolder(View view) {
        super(view);
        note_name = (TextView) view.findViewById(R.id.note_name);
        note_date = (TextView) view.findViewById(R.id.note_date);
        note_text = (TextView) view.findViewById(R.id.note_text);
    }
}
