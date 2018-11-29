package com.example.tanialeif.misnotas.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanialeif.misnotas.DB.DAOMedia;
import com.example.tanialeif.misnotas.Model.Media;
import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;

public class DetalleGaleria extends Fragment {

    private ArrayList<String> images;
    private ArrayList<Media> media;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_galeria, container, false);

        DAOMedia daoMedia = new DAOMedia(view.getContext());
        media = daoMedia.getAll(1);


        return  view;
    }

}
