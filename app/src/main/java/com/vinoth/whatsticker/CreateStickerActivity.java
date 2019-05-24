package com.vinoth.whatsticker;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.samplestickerapp.R;

import  android.os.Environment;

public class CreateStickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sticker);
       /* Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.png");
        CutOut.activity()
                .src(uri)
                .bordered()
                .noCrop()
                .intro()
                .start(this);*/
       findViewById(R.id.btn).setOnClickListener((v)->{

       });



    }
}
