package com.example.tanialeif.misnotas;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class AudioRecorderActivity extends AppCompatActivity {

    Button btnRecord;
    ImageButton btnPlay;
    ImageButton btnCancel;
    ImageButton btnConfirm;

    boolean hasRecorded = false;
    boolean isRecording = false;
    boolean isPlaying = false;

    MediaRecorder recorder;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        btnRecord = findViewById(R.id.btnRecord);
        btnPlay = findViewById(R.id.btnPlay);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

    }

    public void btnRecord_onClick(View view) {
        if (isPlaying) return;

        if (isRecording) {
            btnRecord.clearAnimation();

            stopRecording();
            isRecording = false;

        } else {
            Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.audio_record_button_animation_wiggle);
            btnRecord.startAnimation(wiggle);

            startRecording();
            isRecording = true;
            hasRecorded = true;
        }
    }

    public void btnPlay_onClick(View view) {
        if (isRecording || !hasRecorded) return;

        if (isPlaying)
            pause();
        else
            play();
    }

    public void btnCancel_onClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void btnConfirm_onClick(View view) {
        if (!hasRecorded) {
            Toast.makeText(this, "Aun no has grabado nada!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void startRecording() {
        btnPlay.setClickable(false);

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
            File file = new File(path, "tmp_recording.3gp");

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(file.getAbsolutePath());

            recorder.prepare();
            recorder.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void stopRecording() {
        btnPlay.setClickable(true);

        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void play() {
        btnPlay.setImageResource(R.drawable.ic_menu_pause_32);

        isPlaying = true;

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
            File file = new File(path, "tmp_recording.3gp");

            player = new MediaPlayer();
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    pause();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pause() {
        btnPlay.setImageResource(R.drawable.ic_menu_play_32);

        player.stop();
        player.release();
        player = null;

        isPlaying = false;
    }

}
