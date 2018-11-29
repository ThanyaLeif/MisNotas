package com.example.tanialeif.misnotas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanialeif.misnotas.DB.DAOMedia;
import com.example.tanialeif.misnotas.Model.Media;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtPrueba;

    long id;
    Media media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        id = getIntent().getLongExtra("id",-1);
        imageView = (ImageView) findViewById(R.id.mostrarImg);
        txtPrueba = (TextView) findViewById(R.id.txtPrueba);

        DAOMedia daoMedia = new DAOMedia(this);
        media = daoMedia.get(id);

        if(id != -1) {


            if (media.getIdImage().startsWith("content")) {
                imageView.setImageURI(Uri.parse(media.getIdImage()));
            } else {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path, media.getIdImage());
                Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
