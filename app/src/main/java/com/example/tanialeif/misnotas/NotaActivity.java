package com.example.tanialeif.misnotas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tanialeif.misnotas.Adapters.ListMemoAdapter;
import com.example.tanialeif.misnotas.DB.DAOMemo;
import com.example.tanialeif.misnotas.Model.Memo;

import java.util.ArrayList;

public class NotaActivity extends AppCompatActivity {

    RecyclerView list;
    ListMemoAdapter adapter;

    int REQ_NEW_NOTE = 1;
    int REQ_MOD_NOTE = 2;
    int ID_NOTA = 1;

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

        daoMemo.insert(temporalStaticListExample().get(0));
        daoMemo.insert(temporalStaticListExample().get(1));

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
}
