package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class MainActivity extends AppCompatActivity {

    Button btn_daftar, btn_masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_daftar = findViewById(R.id.btn_daftar);
        btn_masuk = findViewById(R.id.btn_masuk);


        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if ( internetCheck() == true){
                        Intent mulai = new Intent(getApplicationContext(), daftar.class);
                        startActivity(mulai);
                    } else {
                        showBottomSheet();
                    }
                }
        });

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( internetCheck() == true ){
                    Intent pindah = new Intent(getApplicationContext(), masuk.class);
                    startActivity(pindah);
                } else {
                    showBottomSheet();
                }
            }
        });
    }

    private void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet, (LinearLayout) findViewById(R.id.bottomsheet_container));
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();
    }


    public boolean internetCheck(){
        boolean statusInternet = true;

        ConnectivityManager connectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if ( networkInfo != null && networkInfo.isConnected()==true){
            statusInternet = true;
        } else {
            statusInternet = false;
        }
        return statusInternet;
    }



}
