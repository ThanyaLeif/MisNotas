package com.example.tanialeif.misnotas.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tanialeif.misnotas.Model.Galeria;
import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;

public class ListGalleryAdapter extends RecyclerView.Adapter<ListGalleryAdapter.MyViewHolder>{

    private ArrayList<Galeria> listaGaleria;
    private OnclickRecycler listener;

    @NonNull
    @Override
    public ListGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adaptador,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Galeria galeria = listaGaleria.get(position);
        holder.bind(galeria,listener);
    }

    @Override
    public int getItemCount() {
        return listaGaleria.size();
    }

    public interface OnclickRecycler{
        void OnclickItemRecycler(Galeria galeria);
    }

    public ListGalleryAdapter(ArrayList<Galeria> listaGaleria, OnclickRecycler listener){
        this.listaGaleria = listaGaleria;
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public MyViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageGallery);
        }

        public void bind(final Galeria galeria, final OnclickRecycler listener){
            Glide.with(imageView.getContext()).load(galeria.getIdImane()).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnclickItemRecycler(galeria);
                }
            });
        }

    }

}
