package com.example.sujay.assignment3_notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Sujay on 2/24/2017.
 */

public class EditActivity extends AppCompatActivity {
    JsonWriter jw;
    EditText E1,E2;
    DateFormat dateFormat;
    String current_date;
    String note_n,note_t;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dateFormat = new SimpleDateFormat("EEE, d MMM yy hh:mm:ss a");
        current_date = dateFormat.format(Calendar.getInstance().getTime());

        E1=(EditText) findViewById(R.id.editText);
        E2=(EditText) findViewById(R.id.editText2);

        Intent intent = getIntent();
        note_n=intent.getStringExtra("Note_Name");
        note_t=intent.getStringExtra("Note_Text");
        E1.setText(note_n);
        E2.setText(note_t);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        if(E1.getText().toString().equals("") && E2.getText().toString().equals(""))
        {
            finish();
        }
        else if(E1.getText().toString().equals("") && !E2.getText().toString().equals(""))
        {
            Toast.makeText(this, "Un-titled activity was not saved", Toast.LENGTH_SHORT).show();
            finish();
        } else if (E1.getText().toString().equals(note_n) && E2.getText().toString().equals(note_t))
        {
            finish();
        }
        else {
            System.out.print("onBackPressed in edit called !!");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.out.print("In side OK");
                    Intent data = new Intent();
                    data.putExtra("Note_Title", E1.getText().toString());
                    dateFormat = new SimpleDateFormat("EEE, d MMM yy hh:mm:ss a");
                    current_date = dateFormat.format(Calendar.getInstance().getTime());
                    data.putExtra("Note_Date", current_date);
                    data.putExtra("Note_Text", E2.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    finish();
                }
            });
            builder.setMessage("Your note is not saved" + " Save note " + E1.getText() + " ?");
            builder.setTitle("Confirmation");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSave:
                try {
                    if(E1.getText().toString().equals(""))
                    {
                        System.out.println("HI");
                        Toast.makeText(this, "Un-titled activity was not saved", Toast.LENGTH_SHORT).show();
                    }
                    else if (E1.getText().toString().equals(note_n) && E2.getText().toString().equals(note_t))
                    {
                        finish();
                    }
                    else {
                        Intent data = new Intent();
                        data.putExtra("Note_Title", E1.getText().toString());
                        dateFormat = new SimpleDateFormat("EEE, d MMM yy hh:mm:ss a");
                        current_date = dateFormat.format(Calendar.getInstance().getTime());
                        data.putExtra("Note_Date", current_date);
                        data.putExtra("Note_Text", E2.getText().toString());
                        setResult(RESULT_OK, data);
                    }
                    finish();
                    return true;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
