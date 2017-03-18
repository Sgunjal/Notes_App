package com.example.sujay.assignment3_notes;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

class MyAsyncTask extends AsyncTask<Context, Integer, String> { //  <Parameter, Progress, Result>
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Context... contexts) {
        JsonReader jr;
        JsonWriter jw;
        String notes_name=null,notes_date=null;
        String notes_text = null;
        ArrayList<Note> json1=new ArrayList<Note>();
        int Edit_post;
        String filePath = contexts[0].getFilesDir().getPath().toString() + "/notes.json";
        File f =new File(filePath);

        json1.clear();
        int object_counter=-1;
        try {
            jr = new JsonReader(new InputStreamReader(contexts[0].openFileInput("notes.json"),"UTF-8"));
            jr.beginObject();
            int temp=0;
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals("Notes_Name")) {
                    notes_name=jr.nextString();
                    Log.i("notes_name:- ",notes_name);
                    temp++;
                }else if (name.equals("Notes_Date") ) {
                    notes_date=jr.nextString();
                    Log.i("notes_date:- ",notes_date);
                    temp++;
                }
                else if (name.equals("Notes_Text") ) {
                    notes_text=jr.nextString();
                    Log.i("notes_Text:- ",notes_text);
                    temp++;
                }

                else{
                    jr.skipValue();
                }
                if(temp==3) {
                    Note note=new Note(notes_name,notes_date,notes_text);
                    json1.add(note);
                    if (notes_text.length()>80)
                        notes_text=notes_text.substring(0,79)+"...";
                    temp=0;
                }
            }
            return json1.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return json1.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static List<Note> noteList = new ArrayList<>();
    private static final int B_REQ = 1;
    private static final int  Edit_REQ= 2;
    private RecyclerView recyclerView;
    private NoteAdapter nAdapter;
    JsonReader jr;
    JsonWriter jw;
    String notes_name,notes_text,notes_date;
    ArrayList<Note> json1=new ArrayList<Note>();
    int Edit_post;
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Edit_post=pos;
        Note n = noteList.get(pos);
        Intent intent3=new Intent(this,EditActivity.class);
        intent3.putExtra("Note_Name",json1.get(pos).getNote_name().toString());
        intent3.putExtra("Note_Date",json1.get(pos).getNote_date().toString());
        intent3.putExtra("Note_Text",json1.get(pos).getNote_text().toString());

        startActivityForResult(intent3, Edit_REQ);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //doAsync();
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            nAdapter = new NoteAdapter(noteList, this);
            recyclerView.setAdapter(nAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            executer();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        menu.getItem(0).setIcon(R.drawable.info_note);
        menu.getItem(1).setIcon(R.drawable.create_note);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuA:
                Intent intent1=new Intent(this,EditActivity.class);
                startActivityForResult(intent1, B_REQ);
                return true;
            case R.id.menuB:
                Intent intent2=new Intent(this,AboutActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            Note temp;
            System.out.println("On Pause called");
            File f =new File("notes.json");
            f.delete();
            jw = new JsonWriter(new OutputStreamWriter(openFileOutput("notes.json", Context.MODE_PRIVATE), "UTF-8"));
            jw.beginObject();
            System.out.println("Array List size is :- "+json1.size());
            Iterator<Note> iterator = json1.iterator();
            while(iterator.hasNext())
            {
                temp=iterator.next();
                jw.name("Notes_Name").value(temp.getNote_name().toString());
                jw.name("Notes_Date").value(temp.getNote_date().toString());
                jw.name("Notes_Text").value(temp.getNote_text().toString());
            }
            jw.endObject();
            jw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doAsync() {
        new MyAsyncTask().execute();
    }
    @Override
    public boolean onLongClick(View v) {
        final int  pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                json1.remove(pos);
                if (!noteList.isEmpty()) {
                    noteList.remove(pos);
                    nAdapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Do you want to delete this Note?");
        builder.setTitle("Confirmation");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            System.out.println("onActivityResult Called !!");
            Note temp;
            System.out.println("Edit_REQ"+Edit_REQ+"B_REQ"+B_REQ+" RESULT_OK "+RESULT_OK);
            if (requestCode == B_REQ) {
                if (resultCode == RESULT_OK) {
                    System.out.println("On onActivityResult inside call ed");
                    String note_name = data.getStringExtra("Note_Title");
                    String note_date = data.getStringExtra("Note_Date");
                    String note_text = data.getStringExtra("Note_Text");

                    Note note = new Note(note_name,note_date,note_text);
                    System.out.println("Array List size is :- "+json1.size());
                    json1.add(0,note);
                    /*File f =new File("notes.json");
                    f.delete();
                    jw = new JsonWriter(new OutputStreamWriter(openFileOutput("notes.json", Context.MODE_PRIVATE), "UTF-8"));
                    jw.beginObject();
                    Iterator<Note> iterator = json1.iterator();
                    while(iterator.hasNext())
                    {
                        temp=iterator.next();
                        jw.name("Notes_Name").value(temp.getNote_name().toString());
                        jw.name("Notes_Date").value(temp.getNote_date().toString());
                        jw.name("Notes_Text").value(temp.getNote_text().toString());
                    }
                    jw.endObject();
                    jw.close();*/
                    if (note_text.length()>80)
                        note_text=note_text.substring(0,79)+"...";
                    noteList.add(0, new Note(note_name,note_date, note_text));
                    nAdapter.notifyDataSetChanged();

                    Log.i("Note_Title: ", note_name);
                } else {
                    Log.i("result Code: ", String.valueOf(resultCode));
                }
            }
            else if(requestCode == Edit_REQ) {
                if (resultCode == RESULT_OK) {
                    System.out.println("inside else");
                    Note j1;
                    String note_name = data.getStringExtra("Note_Title");
                    String note_date = data.getStringExtra("Note_Date");
                    String note_text = data.getStringExtra("Note_Text");

                    j1 = json1.get(Edit_post);
                    j1=new Note(note_name,note_date,note_text);
                    json1.remove(Edit_post);
                    noteList.remove(Edit_post);
                    nAdapter.notifyDataSetChanged();
                    json1.add(0,j1);
                    if (note_text.length()>80)
                        note_text=note_text.substring(0,79)+"...";
                    noteList.add(0, new Note(note_name,note_date, note_text));
                    nAdapter.notifyDataSetChanged();
                }
            }
            else {
                Log.d("Request Code ", String.valueOf(requestCode));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public  void executer()
    {
        noteList.clear();
        json1.clear();
        int object_counter=-1;
        try {
            jr = new JsonReader(new InputStreamReader(openFileInput("notes.json"),"UTF-8"));
            jr.beginObject();
            int temp=0;
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals("Notes_Name")) {
                    notes_name=jr.nextString();
                    temp++;
                }else if (name.equals("Notes_Date") ) {
                    notes_date=jr.nextString();
                    temp++;
                }
                else if (name.equals("Notes_Text") ) {
                    notes_text=jr.nextString();
                    temp++;
                }

                else{
                    jr.skipValue();
                }
                if(temp==3) {
                    object_counter++;
                    System.out.println(notes_name+" "+notes_text);
                    Note note=new Note(notes_name,notes_date,notes_text);
                    json1.add(note);
                    if (notes_text.length()>80)
                        notes_text=notes_text.substring(0,79)+"...";
                    noteList.add(object_counter, new Note(notes_name, notes_date,notes_text));
                    nAdapter.notifyDataSetChanged();
                    temp=0;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
