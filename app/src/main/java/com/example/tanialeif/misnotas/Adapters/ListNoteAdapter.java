package com.example.tanialeif.misnotas.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanialeif.misnotas.Model.Note;
import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> {

    public enum TypeFilter {
        All, Notes, Tasks
    }

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Note> dataset;
    private ArrayList<Integer> filteredIndexes;
    private ArrayList<Note> filteredDataset;

    private TypeFilter filter = TypeFilter.All;
    private String filterText = "";

    private View.OnLongClickListener onLongClickListener;

    public ListNoteAdapter(Context context, ArrayList<Note> data) {
        this.context = context;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.dataset = data;
        this.setFilter(TypeFilter.All);
        this.setFilterText("");
    }

    public void setFilter(TypeFilter filter) {
        this.filter = filter;
        recalcFilter();
    }
    public void setFilterText(String s) {
        this.filterText = s.toLowerCase();
        recalcFilter();
    }

    public void recalcFilter() {
        filteredDataset = new ArrayList<>();
        filteredIndexes = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            Note note = dataset.get(i);
            Note.TypeNote type = dataset.get(i).getType();

            if (!note.getTitle().toLowerCase().contains(filterText) &&
                    !note.getText().toLowerCase().contains(filterText))
                continue;

            if (filter == TypeFilter.All) {
                filteredDataset.add(note);
                filteredIndexes.add(i);
            }
            else if (filter == TypeFilter.Notes && type == Note.TypeNote.Note) {
                filteredDataset.add(note);
                filteredIndexes.add(i);
            } else if (filter == TypeFilter.Tasks && type == Note.TypeNote.Task) {
                filteredDataset.add(note);
                filteredIndexes.add(i);
            }
        }

        notifyDataSetChanged();
    }


    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_note_view, viewGroup, false);

        view.setOnLongClickListener(onLongClickListener);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note note = filteredDataset.get(i);

        viewHolder.title.setText(note.getTitle());
        viewHolder.text.setText(note.getText());

        int icon = note.getType() == Note.TypeNote.Note ?
                R.drawable.ic_menu_note :
                R.drawable.ic_menu_task;

        viewHolder.icon.setImageResource(icon);
    }

    @Override
    public int getItemCount() {
        return filteredDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView text;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.noteTitle);
            text = itemView.findViewById(R.id.noteText);
            icon = itemView.findViewById(R.id.noteIcon);

        }
    }
}
