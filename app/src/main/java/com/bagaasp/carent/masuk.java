package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class masuk extends AppCompatActivity {
    EditText Email, Pass;
    Button login;
    int id_user;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        Email = findViewById(R.id.emailuser);
        Pass = findViewById(R.id.pass);
        login = findViewById(R.id.btn_masuk);

        sp = getSharedPreferences("dataUser", Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = Email.getText().toString();
                String password = Pass.getText().toString();
                if (nik.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "Masukkan data dengan benar", Toast.LENGTH_LONG).show();
                } else {
                    masuk();
                }
            }
        });
    }

    public void masuk() {
        AndroidNetworking.post(config.IP + "penyewa_login.php")
                .addBodyParameter("email", Email.getText().toString())
                .addBodyParameter("password", Pass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if(response.getString("hasil").equals("ada")){
                                id_user = response.getInt("id");

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("idUser", id_user);
                                editor.putBoolean("login", true);
                                editor.commit();

                                Intent beranda = new Intent(getApplicationContext(), beranda.class);
                                startActivity(beranda);
                                finishAffinity();
                            } else {
                                showBottomSheet();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), "Gagal Terhubung dengan server, coba lagi"+error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showBottomSheet(){
        BottomSheetDialog btDialog = new BottomSheetDialog(masuk.this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wrongpassword, (LinearLayout) findViewById(R.id.wrongPassContainer));
        btDialog.setContentView(v);
        btDialog.show();

        v.findViewById(R.id.button_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btDialog.dismiss();
            }
        });

    }
}
