package com.portsip.sipsample.modification.src;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;


    public void click() {

    }

    private void pla1y(int file) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, file);
        mediaPlayer.start();
    }


    public void showToast(String str) {
        runOnUiThread(() -> {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        });
    }


    public void redirect(Class c) {
        this.startActivity(new Intent(this, c));
    }


    public void log(String tag, String msg) {
        Log.i(tag, tag + ": " + msg);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mediaPlayer = new MediaPlayer();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
