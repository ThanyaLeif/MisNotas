package com.example.tanialeif.misnotas.Model;

import com.example.tanialeif.misnotas.R;

import java.util.ArrayList;

public class Galeria {

    private int idImane;
    private String title;

    public Galeria(){}

    public Galeria(int idImane, String title) {
        this.idImane = idImane;
        this.title = title;
    }

    public int getIdImane() {
        return idImane;
    }

    public void setIdImane(int idImane) {
        this.idImane = idImane;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Galeria> listaPrueba(){
        Galeria galeria;
        ArrayList<Galeria> lista = new ArrayList<Galeria>();

        Integer[] idImagenes = new Integer[]{R.drawable.fondo1, R.drawable.fondo2,
                R.drawable.fondo3, R.drawable.fondo4, R.drawable.fondo5};
        String[] titulos = new String[] {"Fondo1","Fondo2","Fondo3","Fondo4","Fondo5"};

        for(int i=0; i<idImagenes.length; i++){
            galeria = new Galeria(idImagenes[i],titulos[i]);

            lista.add(galeria);
        }

        return lista;
    }
}
