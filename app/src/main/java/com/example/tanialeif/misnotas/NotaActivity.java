package com.example.tanialeif.misnotas;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tanialeif.misnotas.Adapters.ListImageAdapter;
import com.example.tanialeif.misnotas.Adapters.ListMemoAdapter;
import com.example.tanialeif.misnotas.DB.DAOMedia;
import com.example.tanialeif.misnotas.DB.DAOMemo;
import com.example.tanialeif.misnotas.DB.DAONote;
import com.example.tanialeif.misnotas.Model.Media;
import com.example.tanialeif.misnotas.Model.Memo;
import com.example.tanialeif.misnotas.Model.Note;

import java.io.Console;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static java.security.AccessController.getContext;

public class NotaActivity extends AppCompatActivity {

    RecyclerView list, listFiles;
    ListMemoAdapter adapter;
    ListImageAdapter adapterMedia;

    Button btnGuardar;
    FloatingActionButton btnAgregarMultimedia;
    EditText txtTitulo, txtDescripcion;
    TextView txtFecha, txtHora;
    Button btnFecha, btnHora;
    MediaPlayer player;

    int REQ_NEW_NOTE = 1;
    int REQ_MOD_NOTE = 2;
    int INSERT_MEMO = 3;
    int INSERT_AUDIO = 4;
    int INSERT_FOTO = 5;
    int INSERT_VIDEO = 6;
    int INSERT_MULTI = 7;
    long ID_NOTA = -1;

    boolean allowetToUseCamera = false;
    boolean allowetToUseAudio = false;

    String type_note;
    String pathPhoto;

    ArrayList<Memo> temporalMemo = new ArrayList<>();
    ArrayList<Media> temporalMedia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        final Context self = this;
        final DAOMemo daoMemo = new DAOMemo(self);

        btnFecha = (Button)findViewById(R.id.btnFecha);
        btnHora = (Button)findViewById(R.id.btnHora);

        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        txtHora = (TextView) findViewById(R.id.txtHora);

        type_note = getIntent().getStringExtra("type");

        if(type_note.equals("Note")){
            btnFecha.setVisibility(View.INVISIBLE);
            btnHora.setVisibility(View.INVISIBLE);
            txtFecha.setVisibility(View.INVISIBLE);
            txtHora.setVisibility(View.INVISIBLE);
        }

        ID_NOTA = getIntent().getLongExtra("id",-1);

        if(ID_NOTA != -1){
            isModification(ID_NOTA);
        }

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    if(ID_NOTA==-1) {
                        long id = insertNote();
                        insertMemo(id);
                        insertMedia(id);
                    }
                    else{
                        editNote(ID_NOTA);
                        insertMemo(ID_NOTA);
                        insertMedia(ID_NOTA);
                    }
                    finish();
                }
            }
        );

        btnAgregarMultimedia = (FloatingActionButton) findViewById(R.id.btnAgregarMultimedia);

        btnAgregarMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] options = new CharSequence[]  {
                        "Audio",
                        "Camara",
                        "Video",
                        "Desde galeria",
                        "Recordatorio"
                };
                AlertDialog.Builder menu = new AlertDialog.Builder(self);

                menu.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                validarAudio();
                                if(allowetToUseAudio) {
                                    Intent detail = new Intent(self, AudioRecorderActivity.class);
                                    startActivityForResult(detail,INSERT_AUDIO);
                                }
                                else{
                                    Toast.makeText(self,"No hay permisos suficientes",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case 1: {
                                validarCamara();
                                if(allowetToUseCamera) {
                                    tomarFoto();
                                }
                                break;
                            }

                            case 2:{
                                validarCamara();
                                if (allowetToUseCamera){
                                    tomarVideo();
                                }
                                break;
                            }

                            case 3:{
                                tomarMultimedia();
                                break;
                            }

                            case 4: {
                                Intent detail = new Intent(self, MemoActivity.class);
                                startActivityForResult(detail,INSERT_MEMO);
                                break;
                            }
                        }
                    }
                });

                menu.show();
            }

        });

        if(ID_NOTA!=-1) {
            final DAOMedia daoMedia = new DAOMedia(this);
            listFiles = findViewById(R.id.listMedia);
            listFiles.setLayoutManager(new LinearLayoutManager(this));
            adapterMedia = new ListImageAdapter(this, daoMedia.getAll(ID_NOTA));
            listFiles.setAdapter(adapterMedia);

            adapterMedia.setOnItemLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int vid = list.getChildAdapterPosition(v);
                    final Media media = adapterMedia.getItem(vid);


                    CharSequence[] options = new CharSequence[]{
                            "Eliminar"
                    };

                    AlertDialog.Builder menu = new AlertDialog.Builder(self);

                    menu.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {

                                case 0: {
                                    daoMedia.delete(media.getId());
                                    adapterMedia.updateData(daoMedia.getAll(ID_NOTA));
                                    break;
                                }
                            }
                        }
                    });

                    menu.show();
                    return true;
                }

            });

            adapterMedia.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vid = list.getChildAdapterPosition(v);
                    final Media media = adapterMedia.getItem(vid);

                    if (media.getType().toString().equals("Photo") || media.getType().toString().equals("Multi")) {
                        Intent detail = new Intent(self, PhotoActivity.class);
                        detail.putExtra("id", media.getId());
                        startActivity(detail);
                    } else if (media.getType().toString().equals("Audio")) {
                        try {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
                            File file = new File(path, media.getArchivo());

                            player = new MediaPlayer();
                            player.setDataSource(file.getAbsolutePath());
                            player.prepare();
                            player.start();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Intent detail = new Intent(self, VideoActivity.class);
                        detail.putExtra("id", media.getId());
                        startActivity(detail);
                    }
                }
            });
        }

        list = findViewById(R.id.listMemo);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListMemoAdapter(this, daoMemo.getAll(ID_NOTA));
        list.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int vid = list.getChildAdapterPosition(v);
                final Memo memo = adapter.getItem(vid);


                CharSequence[] options = new CharSequence[]  {
                        "Editar",
                        "Eliminar"
                };

                AlertDialog.Builder menu = new AlertDialog.Builder(self);

                menu.setItems(options,  new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0: {
                                Intent detail = new Intent(self, MemoActivity.class);
                                detail.putExtra("id", memo.getId());
                                startActivityForResult(detail, REQ_MOD_NOTE);
                                break;
                            }

                            case 1:{
                                daoMemo.delete(memo.getId());
                                adapter.updateData(daoMemo.getAll(ID_NOTA));
                                break;
                            }
                        }
                    }
                });

                menu.show();
                return true;
            }

        });


    }

    private void isModification(long id){
        DAONote daoNote = new DAONote(this);
        Note note = daoNote.get(id);
        txtTitulo.setText(note.getTitle());
        txtDescripcion.setText(note.getText());
        txtHora.setText(note.getTime());
        txtFecha.setText(note.getDate());
    }

    private void validarCamara(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            allowetToUseCamera = true;
        }

        else if(ContextCompat.checkSelfPermission(NotaActivity.this,
                Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            allowetToUseCamera = true;
        }

        else if((ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA))){
            new AlertDialog.Builder(this)
                    .setTitle("Se necesitan permisos")
                    .setMessage("Habilite los permisos para el correcto funcionamiento de la app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NotaActivity.this, new String[]{Manifest.permission.CAMERA},100);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
            .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},100);
        }
    }

    private void validarAudio(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            allowetToUseAudio = true;
        }

        else if(ContextCompat.checkSelfPermission(NotaActivity.this,
                Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
            allowetToUseAudio = true;
        }

        else if((ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO))){
            new AlertDialog.Builder(this)
                    .setTitle("Se necesitan permisos")
                    .setMessage("Habilite los permisos para el correcto funcionamiento de la app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NotaActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},101);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},101);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                allowetToUseCamera = true;
            }
            else{
                allowetToUseCamera=false;
            }
        }
        if(requestCode==101){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                allowetToUseAudio = true;
            }
            else{
                allowetToUseAudio=false;
            }
        }
    }

    public void insertMemo(long id){
        DAOMemo daoMemo = new DAOMemo(this);
        for(int i=0; i<temporalMemo.size(); i++){
            temporalMemo.get(i).setIdNote(id);
            daoMemo.insert(temporalMemo.get(i));
        }
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    public void insertMedia(long id){
        DAOMedia daoMedia = new DAOMedia(this);

        for (int i=0; i<temporalMedia.size(); i++){

            Media actual = temporalMedia.get(i);

            if(actual.getType().toString().equals("Audio")){
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
                File file = new File(path, "tmp"+i+".3gp");

                actual.setIdNote(id);

                long idMedia = daoMedia.insert(actual);

                actual.setArchivo("audio"+idMedia+".3gp");
                actual.setIdNote(ID_NOTA);
                actual.setId(idMedia);

                daoMedia.update(actual);

                File newPath = new File(path, "audio"+idMedia+".3gp");
                file.renameTo(newPath);
            }

            if(actual.getType().toString().equals("Photo")){
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path, "foto"+i+".jpg");

                actual.setIdNote(id);

                long idMedia = daoMedia.insert(actual);

                actual.setArchivo(Environment.DIRECTORY_PICTURES+"photo"+idMedia+".jpg");
                actual.setIdNote(ID_NOTA);
                actual.setId(idMedia);
                actual.setIdImage("photo"+idMedia+".jpg");

                daoMedia.update(actual);
                txtTitulo.setText(daoMedia.get(idMedia).getIdImage()+"");

                File newPath = new File(path, "photo"+idMedia+".jpg");
                file.renameTo(newPath);
            }

            if(actual.getType().toString().equals("Video")){
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                File file = new File(path, "video"+i+".3gp");

                actual.setIdNote(id);

                long idMedia = daoMedia.insert(actual);

                actual.setArchivo("video"+idMedia+".3gp");
                actual.setIdNote(ID_NOTA);
                actual.setId(idMedia);

                daoMedia.update(actual);

                File newPath = new File(path, "video"+idMedia+".3gp");
                file.renameTo(newPath);
            }

            if(actual.getType().toString().equals("Multi")){
                actual.setIdNote(ID_NOTA);
                long idMulti = daoMedia.insert(actual);
                txtTitulo.setText(daoMedia.get(idMulti).getIdImage());
            }

        }

    }

    public ArrayList<String> getImages(){
        DAOMedia daoMedia = new DAOMedia(this);
        ArrayList<Media> media = daoMedia.getAll(ID_NOTA);
        ArrayList<String> result = new ArrayList<>();

        for(int i=0; i<media.size(); i++){
            result.add(media.get(i).getIdImage());
        }

        return result;
    }

    public long insertNote(){
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");

        DAONote daoNote = new DAONote(this);
        Note note = new Note(
                1,
                txtTitulo.getText().toString(),
                txtDescripcion.getText().toString(),
                type_note.equals("Note") ? Note.TypeNote.Note : Note.TypeNote.Task,
                txtFecha.getText().toString(),
                txtHora.getText().toString(),
                false,
                simpleDate.format(Calendar.getInstance().getTime())
        );

        return daoNote.insert(note);

    }

    private void tomarFoto(){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "tmp_photo.jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent,INSERT_FOTO);
    }

    private void tomarVideo(){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        File file = new File(path, "tmp_video.3gp");

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent,INSERT_VIDEO);
    }

    public void tomarMultimedia(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");

        startActivityForResult(intent.createChooser(intent,"Seleccione"),INSERT_MULTI);
    }

    public void editNote(long id){
        DAONote daoNote = new DAONote(this);
        Note note = daoNote.get(id);
        Note updatedNote = new Note(
                note.getId(),
                txtTitulo.getText().toString(),
                txtDescripcion.getText().toString(),
                note.getType(),
                txtFecha.getText().toString(),
                txtHora.getText().toString(),
                false,
                note.getActualDate()
        );
        daoNote.update(updatedNote);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INSERT_MEMO){

            Memo memo = (Memo)data.getExtras().getSerializable("memo");
            temporalMemo.add(memo);
        }

        if(requestCode == INSERT_AUDIO){

            if(resultCode == RESULT_OK) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
                File file = new File(path, "tmp_recording.3gp");

                File newPath = new File(path, "tmp" + temporalMedia.size() + ".3gp");
                file.renameTo(newPath);

                Media media = new Media(
                        1,
                        "audio" + temporalMedia.size() + ".3gp",
                        "R.drawable.fondo1.png",
                        Media.TypeMedia.Audio,
                        ID_NOTA
                );

                temporalMedia.add(media);
            }

        }

        if(requestCode == INSERT_FOTO){
            MediaScannerConnection.scanFile(this, new String[]{}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Path", ""+path);
                        }
                    });

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "tmp_photo.jpg");

            File newPath = new File(path, "foto" + temporalMedia.size() + ".jpg");
            file.renameTo(newPath);

                Media media = new Media(
                        1,
                        "foto"+temporalMedia.size()+".jpg",
                        "foto"+temporalMedia.size()+".jpg",
                        Media.TypeMedia.Photo,
                        ID_NOTA
                );

            temporalMedia.add(media);
        }

        if(requestCode == INSERT_VIDEO){
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File file = new File(path, "tmp_video.3gp");

            File newPath = new File(path, "video" + temporalMedia.size() + ".3gp");
            file.renameTo(newPath);

            Media media = new Media(
                    1,
                    "video"+temporalMedia.size()+".3gp",
                    "R.drawable.fondo1.png",
                    Media.TypeMedia.Video,
                    ID_NOTA
            );

            temporalMedia.add(media);
        }

        if(requestCode== INSERT_MULTI){
            Uri miPath = data.getData();
            //File file = new File(miPath.getPath());

            Media media = new Media(
                    1,
                    miPath.toString(),
                    miPath.toString(),
                    Media.TypeMedia.Multi,
                    ID_NOTA
            );

            temporalMedia.add(media);

        }

    }
}
