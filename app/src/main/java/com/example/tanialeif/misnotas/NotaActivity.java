package com.example.tanialeif.misnotas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tanialeif.misnotas.Adapters.ListMemoAdapter;
import com.example.tanialeif.misnotas.Model.Memo;

import java.util.ArrayList;

public class NotaActivity extends AppCompatActivity {

    RecyclerView list;
    ListMemoAdapter adapter;

    private ArrayList<Memo> temporalStaticListExample() {
        ArrayList<Memo> listMemo = new ArrayList<>();

        listMemo.add(new Memo(
                45,
                "29/11/2018",
                "21:00"
        ));

        listMemo.add(new Memo(
                5,
                "8/11/2018",
                "23:09"
        ));

        listMemo.add(new Memo(
                198,
                "09/12/1998",
                "3:12"
        ));

        return listMemo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        list = findViewById(R.id.listMemo);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListMemoAdapter(this, temporalStaticListExample());
        list.setAdapter(adapter);

        //TODO: Implementar el onLongClick para las opciones

    }
}
