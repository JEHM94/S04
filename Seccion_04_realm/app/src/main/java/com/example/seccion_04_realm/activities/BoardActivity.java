package com.example.seccion_04_realm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seccion_04_realm.R;
import com.example.seccion_04_realm.adapters.BoardAdapter;
import com.example.seccion_04_realm.models.Board;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    private Realm realm;

    private FloatingActionButton fabBoard;

    private ListView listView;
    private BoardAdapter adapter;
    private RealmResults<Board> boards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //DB Realm
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);
        //Opción 2
//        boards.addChangeListener(new RealmChangeListener<RealmResults<Board>>() {
//            @Override
//            public void onChange(RealmResults<Board> boards) {
//                adapter.notifyDataSetChanged();
//            }
//        });

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
        listView = (ListView) findViewById(R.id.listViewBoard);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fabBoard = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fabBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingBoard("New Board", "Type a name for the new Board");
            }
        });
        registerForContextMenu(listView);

        //Limpiar DB
//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();
    }

    //CRUD ACTIONS
    private void createNewBoard(String boardName) {

        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        Toast.makeText(this, "Board " + boardName + " added successfully", Toast.LENGTH_LONG).show();
        //adapter.notifyDataSetChanged();
        realm.commitTransaction();
    }

    private void deleteBoard(Board board) {
        realm.beginTransaction();
        Toast.makeText(this, "Board " + board.getTittle() + " deleted successfully", Toast.LENGTH_LONG).show();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editBoard (String newName, Board board){
        realm.beginTransaction();
        board.setTittle(newName);
        realm.copyToRealmOrUpdate(board);
        Toast.makeText(this, "Board " + board.getTittle() + " edited successfully", Toast.LENGTH_LONG).show();
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
    //CRUD ACTIONS

    //Dialogs
    private void showAlertForCreatingBoard(String tittle, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (tittle != null) builder.setTitle(tittle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextDialog);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Trim para borrar los espacios al final
                String boardName = input.getText().toString().trim();
                if (boardName.length() > 0)
                    createNewBoard(boardName);
                else {
                    Toast.makeText(getApplicationContext(), "The name is required to create a new Board", Toast.LENGTH_LONG).show();
                    showAlertForCreatingBoard("New Board", "Type a name for the new Board");
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

    private void showAlertForEditingBoard(String tittle, String message, final Board board) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (tittle != null) builder.setTitle(tittle);
        if (message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextDialog);
        input.setText(board.getTittle());
        input.extendSelection(input.length());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Trim para borrar los espacios al final
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "The name is required to edit the current Board", Toast.LENGTH_LONG).show();
                    showAlertForEditingBoard("Edit Board", "Type a new name for " + board.getTittle(), board);
                } else if (boardName.equals(board.getTittle())) {
                    Toast.makeText(getApplicationContext(), "The name is the same as the current Board", Toast.LENGTH_LONG).show();
                    showAlertForEditingBoard("Edit Board", "Type a new name for " + board.getTittle(), board);
                } else {
                    editBoard(boardName, board);
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


    // Events


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTittle());
        getMenuInflater().inflate(R.menu.context_menu_board_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Edit Board", "Type a new name for " + boards.get(info.position).getTittle(), boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }
}