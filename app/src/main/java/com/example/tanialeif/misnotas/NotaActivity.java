package com.example.tanialeif.misnotas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class NotaActivity extends AppCompatActivity {

    RecyclerView list;
    ListMemoAdapter adapter;

    Button btnGuardar;
    FloatingActionButton btnAgregarMultimedia;
    EditText txtTitulo, txtDescripcion;
    TextView txtFecha, txtHora;
    Button btnFecha, btnHora;

    int REQ_NEW_NOTE = 1;
    int REQ_MOD_NOTE = 2;
    int INSERT_MEMO = 3;
    int INSERT_AUDIO = 4;
    long ID_NOTA = -1;

    boolean allowetToUseCamera = false;
    boolean allowetToUseAudio = false;

    String type_note;

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
                                validarCamara();;
                                if(allowetToUseCamera) {
                                    Intent detail = new Intent(self, PhotoActivity.class);
                                    startActivity(detail);
                                }
                                break;
                            }

                            case 2: {
                                Intent detail = new Intent(self, MemoActivity.class);
                                startActivityForResult(detail,INSERT_MEMO);
                            }
                        }
                    }
                });

                menu.show();
            }

        });

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
                                //TODO: Hacer que se recargue la información después de borrar
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

    public void insertMedia(long id){
        DAOMedia daoMedia = new DAOMedia(this);

        for (int i=0; i<temporalMedia.size(); i++){

            Media actual = temporalMedia.get(i);

            if(actual.getType().toString().equals("Audio")){
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
                File file = new File(path, "tmp"+i+".3gp");

                txtTitulo.setText("Si");

                actual.setIdNote(id);

                long idMedia = daoMedia.insert(actual);

                actual.setArchivo(Environment.DIRECTORY_PODCASTS+"audio"+idMedia+".3gp");
                actual.setId(idMedia);

                daoMedia.update(actual);

                File newPath = new File(path, "audio"+idMedia+".3gp");
                file.renameTo(newPath);
            }
        }

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
                        1
                );

                temporalMedia.add(media);
            }

        }

    }
}
