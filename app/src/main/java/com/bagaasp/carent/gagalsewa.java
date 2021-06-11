package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.nio.BufferUnderflowException;

public class gagalsewa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gagalsewa);

        Button gagal = findViewById(R.id.btn_cblg);
        gagal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cobalagi = new Intent(getApplicationContext(), beranda.class);
                startActivity(cobalagi);
                finish();
            }
        });
    }
}
