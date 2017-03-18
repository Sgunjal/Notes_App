package com.example.sujay.assignment3_notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sujay on 2/24/2017.
 */

public class Note {
    private String note_name;
    private String note_date;
    private String note_text;

    private static int ctr = 1;

    public Note(String note_name,String note_date,String note_text) {
        this.note_name = note_name;
        this.note_date = note_date;
        this.note_text = note_text;
        ctr++;
    }

    public String getNote_name() {
        return note_name;
    }

    public String getNote_date() {
        return note_date;
    }

    public String getNote_text() {
        return note_text;
    }

    @Override
    public String toString() {
        return note_name + " (" + note_date+ "), " + note_text;
    }
}
