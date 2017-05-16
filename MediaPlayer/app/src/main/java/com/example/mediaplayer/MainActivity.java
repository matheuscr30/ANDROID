package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = (ToggleButton)findViewById(R.id.tocarId);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.musica);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mediaPlayer.isPlaying()) {
                    if(!isChecked){
                        pausarMusica();
                    }
                } else{
                    if(isChecked){
                        tocarMusica();
                    }else{
                        Toast.makeText(MainActivity.this, "Ja esta pausado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void tocarMusica(){

        //Se for != null é pq foi criado
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    private void pausarMusica(){

        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
