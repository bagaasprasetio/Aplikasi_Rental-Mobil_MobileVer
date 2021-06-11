package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class daftar extends AppCompatActivity {
    EditText NIK, namap, email, pw, telp, alamat;
    Button btn_daftar;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        NIK = findViewById(R.id.NIK);
        namap = findViewById(R.id.namap);
        email = findViewById(R.id.email);
        pw = findViewById(R.id.pw);
        telp = findViewById(R.id.telp);
        alamat = findViewById(R.id.alamat);
        btn_daftar = findViewById(R.id.btn_daftar);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NIK.getText().toString().equals("") || namap.getText().toString().equals("") || email.getText().toString().equals("") || pw.getText().toString().equals("") || telp.getText().toString().equals("") || alamat.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Masukkan Data dengan Benar", Toast.LENGTH_LONG).show();
                } else if (NIK.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "NIK Wajib Diisi", Toast.LENGTH_LONG).show();
               } else if (namap.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Nama Penyewa Wajib Diisi", Toast.LENGTH_LONG).show();
               } else if (email.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Email Wajib Diisi", Toast.LENGTH_LONG).show();
               } else if (pw.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Password Wajib Diisi", Toast.LENGTH_LONG).show();
               } else if (telp.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Nomor Telepon Wajib Diisi", Toast.LENGTH_LONG).show();
               } else if (alamat.getText().toString().equals("")){
                   Toast.makeText(getApplicationContext(), "Alamat Wajib Diiisi", Toast.LENGTH_LONG).show();
               } else {
                    simpanData();
               }
            }
        });
        }

        private void simpanData() {
            AndroidNetworking.post(config.IP + "penyewa_daftar.php")
                    .addBodyParameter("NIK", NIK.getText().toString())
                    .addBodyParameter("namap", namap.getText().toString())
                    .addBodyParameter("email", email.getText().toString())
                    .addBodyParameter("pw", pw.getText().toString())
                    .addBodyParameter("telp", telp.getText().toString())
                    .addBodyParameter("alamat", alamat.getText().toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").equals("data belum ada")) {
                                    Toast.makeText(getApplicationContext(), "Data Sudah Disimpan", Toast.LENGTH_LONG).show();
                                    pindah_login();
                                    finish();
                                } else {
                                    showBottomSheet();
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(getApplicationContext(), "Gagal Terhubung dengan server, coba lagi"+anError.getErrorBody(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "errorCode : "+anError.getErrorBody());
                        }
                    });


        }

    private void pindah_login() {
        Intent login = new Intent(getApplicationContext(), masuk.class);
        startActivity(login);
    }

    private void showBottomSheet(){
        BottomSheetDialog btDialog = new BottomSheetDialog(daftar.this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dataavaibility, (LinearLayout) findViewById(R.id.bottomcontain));
        btDialog.setContentView(v);
        btDialog.show();

        v.findViewById(R.id.buttonlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pindah_login();
                finish();
            }
        });
    }

    private void showBottomSheetNoEnter(){
        BottomSheetDialog btDialog = new BottomSheetDialog(daftar.this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomnoenter, (LinearLayout) findViewById(R.id.noEnterContain));
        btDialog.setContentView(v);
        btDialog.show();

        v.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), masuk.class);
                startActivity(a);
                finish();
            }
        });
    }

}

