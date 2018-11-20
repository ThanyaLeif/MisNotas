package com.example.tanialeif.misnotas.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tanialeif.misnotas.R;

import java.util.Calendar;

public class DetalleNota extends Fragment implements View.OnClickListener {

    TextView txtFecha, txtHora;
    Button btnFecha, btnHora;

    private int dia, mes, anio, hora, minutos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_nota, container, false);

        btnFecha = (Button)view.findViewById(R.id.btnFecha);
        btnHora = (Button)view.findViewById(R.id.btnHora);
        txtFecha = (TextView) view.findViewById(R.id.txtFecha);
        txtHora = (TextView) view.findViewById(R.id.txtHora);

        btnFecha.setOnClickListener(this);
        btnHora.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==btnFecha){
            final Calendar calendar =Calendar.getInstance();
            dia = calendar.get(Calendar.DAY_OF_MONTH);
            mes = calendar.get(Calendar.MONTH);
            anio = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            },anio, mes, dia
            );
            datePickerDialog.show();
        }
        if(v==btnHora){
            final Calendar calendar = Calendar.getInstance();

            hora = calendar.get(Calendar.HOUR_OF_DAY);
            minutos = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtHora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,true);
            timePickerDialog.show();
        }
    }
}
