package com.example.tanialeif.misnotas.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tanialeif.misnotas.Adapters.ListGalleryAdapter;
import com.example.tanialeif.misnotas.Model.Galeria;
import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;


public class DetalleArchivo extends DialogFragment {

    TextView txtTitulo;
    RecyclerView recyclerViewGaleria;
    ImageView imgGaleria;
    ArrayList<Galeria> listGallery;
    ListGalleryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_archivo, container, false);

        txtTitulo = (TextView) view.findViewById(R.id.txtTituloImagen);
        imgGaleria = (ImageView) view.findViewById(R.id.imgGaleria);
        recyclerViewGaleria = (RecyclerView) view.findViewById(R.id.galeria);

        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        recyclerViewGaleria.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));

        listGallery = new Galeria().listaPrueba();

        adapter = new ListGalleryAdapter(listGallery, new ListGalleryAdapter.OnclickRecycler() {
            @Override
            public void OnclickItemRecycler(Galeria galeria) {
                Glide.with(getContext()).load(galeria.getIdImane()).into(imgGaleria);
                txtTitulo.setText(galeria.getTitle());
            }
        });

        recyclerViewGaleria.setAdapter(adapter);

        return view;
    }
}
