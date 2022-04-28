package com.example.seccion_04_realm.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seccion_04_realm.R;
import com.example.seccion_04_realm.adapters.NoteAdapter;
import com.example.seccion_04_realm.models.Board;
import com.example.seccion_04_realm.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private ListView listView;
    private FloatingActionButton fab;

    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;

    private int boardID;
    private Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null) {
            boardID = getIntent().getExtras().getInt("id");
        }
        board = realm.where(Board.class).equalTo("id", boardID).findFirst();
        board.addChangeListener(this);
        notes = board.getNotes();

        this.setTitle(board.getTittle());

        fab = (FloatingActionButton) findViewById(R.id.fabAddNote);
        listView = (ListView) findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this, notes, R.layout.list_view_note_item);

        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingNote("Add New Note", "Type a note for " + board.getTittle() + ".");
            }
        });
    }

    //Dialogs
    private void showAlertForCreatingNote(String tittle, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (tittle != null) builder.setTitle(tittle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewNote);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Trim para borrar los espacios al final
                String note = input.getText().toString().trim();
                if (note.length() > 0)
                    createNewNote(note);
                else {
                    Toast.makeText(getApplicationContext(), "The note cannot be empty", Toast.LENGTH_LONG).show();
                    showAlertForCreatingNote("Add New Note", "Type a note for " + board.getTittle() + ".");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //User canceled the action
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createNewNote(String note) {
        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note);
        realm.commitTransaction();
    }

    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }
}