package com.example.tanialeif.misnotas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tanialeif.misnotas.DB.DAOMedia;
import com.example.tanialeif.misnotas.Model.Media;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    VideoView mostrarVideo;
    TextView txtPrueba;

    Media media;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        id = getIntent().getLongExtra("id",-1);
        mostrarVideo = (VideoView) findViewById(R.id.mostrarVideo);
        txtPrueba = (TextView) findViewById(R.id.txtPruebaVid);

        DAOMedia daoMedia = new DAOMedia(this);
        media = daoMedia.get(id);

        txtPrueba.setText(media.getArchivo());

        if(id != -1) {

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File file = new File(path, media.getArchivo());
            mostrarVideo.setVideoPath(file.getPath());
            mostrarVideo.start();
        }

        //mostrarVideo.setVideoURI();
    }
}
