package com.example.tanialeif.misnotas;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tanialeif.misnotas.Adapters.ListGalleryAdapter;
import com.example.tanialeif.misnotas.Fragments.DetalleArchivo;

public class PruebasGaleria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_galeria);

        Button boton = (Button) findViewById(R.id.button);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                DetalleArchivo galeria = new DetalleArchivo();
                galeria.setStyle(DetalleArchivo.STYLE_NO_FRAME, R.style.transparent);
                galeria.show(manager,"");
            }
        });
    }
}
