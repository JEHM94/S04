package com.example.seccion_04_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seccion_04_realm.R;
import com.example.seccion_04_realm.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> list;
    private int layout;


    public BoardAdapter(Context context, List<Board> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tittle = (TextView) convertView.findViewById(R.id.textViewBoardTittle);
            viewHolder.notes = (TextView) convertView.findViewById(R.id.textViewBoardNotes);
            viewHolder.date = (TextView) convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Board board = list.get(position);
        viewHolder.tittle.setText(board.getTittle());

        int numNote = board.getNotes().size();
        String textForNotes = (numNote == 1) ? numNote + " Note" : numNote + " Notes: ";
        viewHolder.notes.setText(textForNotes);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(board.getCreatedAt());
        viewHolder.date.setText(createdAt);


        return convertView;
    }

    public class ViewHolder {
        TextView tittle;
        TextView notes;
        TextView date;
    }
}
