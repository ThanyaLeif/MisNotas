package com.example.tanialeif.misnotas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tanialeif.misnotas.Model.Memo;

import java.io.Serializable;
import java.util.Calendar;

public class MemoActivity extends AppCompatActivity {

    TextView txtFechaMemo, txtHoraMemo;
    Button btnFechaMemo, btnHoraMemo, btnGuardarMemo;

    private int dia, mes, anio, hora, minutos;

    int INSERT_MEMO = 3;

    final Context self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        txtFechaMemo = (TextView) findViewById(R.id.txtFechaMemo);
        txtHoraMemo = (TextView) findViewById(R.id.txtHoraMemo);
        btnFechaMemo = (Button) findViewById(R.id.btnFechaMemo);
        btnHoraMemo = (Button) findViewById(R.id.btnHoraMemo);
        btnGuardarMemo = (Button) findViewById(R.id.btnGuardarMemo);

        btnFechaMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar =Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                anio = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(self, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFechaMemo.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },anio, mes, dia
                );
                datePickerDialog.show();

                //startAlarm(calendar);
            }
        });
        btnHoraMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                hora = calendar.get(Calendar.HOUR_OF_DAY);
                minutos = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(self, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtHoraMemo.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,true);
                timePickerDialog.show();
            }
        });

        btnGuardarMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memo memo = new Memo(
                        1,
                        txtFechaMemo.getText().toString(),
                        txtHoraMemo.getText().toString(),
                        1
                );

                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("memo", memo);
                resultIntent.putExtras(bundle);
                prueba();
                setResult(INSERT_MEMO,resultIntent);
                finish();

            }
        });

    }

    private void prueba(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(anio,mes,dia,hora,minutos);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this,0,intent,0);

        alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

}
