package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splashscreen extends AppCompatActivity {
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window a = getWindow();
        a.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_splashscreen);

        sp = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        Boolean loginManager = sp.getBoolean("login", false);



        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    if ( loginManager == true ) {
                        startActivity(new Intent(Splashscreen.this, beranda.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splashscreen.this, MainActivity.class));
                        finish();
                    }
                }
            }
        };
        thread.start();
    }

}


