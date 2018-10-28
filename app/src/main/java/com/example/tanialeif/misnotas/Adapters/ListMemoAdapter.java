package com.example.tanialeif.misnotas.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tanialeif.misnotas.Model.Memo;
import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;
import java.util.MissingResourceException;


public class ListMemoAdapter extends RecyclerView.Adapter<ListMemoAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Memo> dataset;

    private View.OnLongClickListener onLongClickListener;

    public ListMemoAdapter(Context context, ArrayList<Memo> data){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.dataset = data;
    }

    public void updateData(ArrayList<Memo> data) {
        this.dataset = data;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_memo_view, viewGroup, false);

        view.setOnLongClickListener(onLongClickListener);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Memo memo = dataset.get(i);
        viewHolder.date.setText(memo.getDate());
        viewHolder.time.setText(memo.getTime());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.memoDate);
            time = itemView.findViewById(R.id.memoTime);

        }
    }
}
