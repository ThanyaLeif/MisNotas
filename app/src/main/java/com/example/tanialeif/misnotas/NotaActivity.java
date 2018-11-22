package com.example.tanialeif.misnotas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.example.tanialeif.misnotas.DB.DAOMemo;
import com.example.tanialeif.misnotas.DB.DAONote;
import com.example.tanialeif.misnotas.Model.Memo;
import com.example.tanialeif.misnotas.Model.Note;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class NotaActivity extends AppCompatActivity {

    RecyclerView list;
    ListMemoAdapter adapter;

    Button btnGuardar, btnAgregarMultimedia;
    EditText txtTitulo, txtDescripcion;
    TextView txtFecha, txtHora;
    Button btnFecha, btnHora;

    int REQ_NEW_NOTE = 1;
    int REQ_MOD_NOTE = 2;
    int ID_NOTA = 1;

    boolean allowetToUseCamera = false;
    boolean allowetToUseAudio = false;

    String type_note;

    private ArrayList<Memo> temporalStaticListExample() {
        ArrayList<Memo> listMemo = new ArrayList<>();

        listMemo.add(new Memo(
                45,
                "29/11/2018",
                "21:00",
                1
        ));

        listMemo.add(new Memo(
                5,
                "8/11/2018",
                "23:09",
                1
        ));

        listMemo.add(new Memo(
                198,
                "09/12/1998",
                "3:12",
                2
        ));

        return listMemo;
    }

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

        txtTitulo.setText(type_note);

        if(type_note.equals("Note")){
            btnFecha.setVisibility(View.INVISIBLE);
            btnHora.setVisibility(View.INVISIBLE);
            txtFecha.setVisibility(View.INVISIBLE);
            txtHora.setVisibility(View.INVISIBLE);
        }


        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    insertNote();
                }
            }
        );

        btnAgregarMultimedia = (Button) findViewById(R.id.btnAgregarMultimedia);

        btnAgregarMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] options = new CharSequence[]  {
                        "Audio",
                        "Camara"
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
                                    startActivity(detail);
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
                        }
                    }
                });

                menu.show();
            }

        });

        //daoMemo.insert(temporalStaticListExample().get(0));
        //daoMemo.insert(temporalStaticListExample().get(1));

        list = findViewById(R.id.listMemo);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListMemoAdapter(this, daoMemo.getAll(1));
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

    public long insertNote(){
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");

        DAONote daoNote = new DAONote(this);
        Note note = new Note(
                1,
                txtTitulo.getText().toString(),
                txtDescripcion.getText().toString(),
                type_note == "Note" ? Note.TypeNote.Note : Note.TypeNote.Task,
                txtFecha.getText().toString(),
                txtHora.getText().toString(),
                false,
                simpleDate.format(Calendar.getInstance().getTime())
        );

        return daoNote.insert(note);

    }
}
