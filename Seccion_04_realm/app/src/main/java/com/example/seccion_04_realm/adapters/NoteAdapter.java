package com.example.seccion_04_realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seccion_04_realm.R;
import com.example.seccion_04_realm.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> list;
    private int layout;

    public NoteAdapter(Context context, List<Note> note, int layout) {
        this.context = context;
        this.list = note;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
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
            viewHolder.description = (TextView) convertView.findViewById(R.id.textViewNoteDescription);
            viewHolder.createdAt = (TextView) convertView.findViewById(R.id.textViewNoteDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = list.get(position);
        viewHolder.description.setText(note.getDescription());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(note.getCreatedAt());
        viewHolder.createdAt.setText(createdAt);

        return convertView;
    }

    public class ViewHolder {
        TextView description;
        TextView createdAt;
    }
}
