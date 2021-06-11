package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class editdatauser extends AppCompatActivity {

    EditText namaPenyewa, email, alamat, noTelp;
    Button editData;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdatauser);

        namaPenyewa = findViewById(R.id.value_username);
        email = findViewById(R.id.value_email);
        alamat = findViewById(R.id.value_address);
        noTelp = findViewById(R.id.value_phonenumber);
        editData = findViewById(R.id.ubah_data);

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();

            }
        });

        getUserData();
    }

    private void updateData(){
        sp = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        int get = sp.getInt("idUser", 0);

        AndroidNetworking.post(config.IP + "editdatauser.php")
                .addBodyParameter("id", String.valueOf(get))
                .addBodyParameter("namapenyewa", namaPenyewa.getText().toString())
                .addBodyParameter("email", email.getText().toString())
                .addBodyParameter("notelp", noTelp.getText().toString())
                .addBodyParameter("alamat", alamat.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("berhasil")){
                                Toast.makeText(getApplicationContext(), "Berhasil ubah data", Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ubah data belum berhasil nih, coba lagi ya!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal Terhubung dengan server, coba lagi"+anError.getErrorBody(), Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void getUserData(){
        sp = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        int get = sp.getInt("idUser", 0);

        AndroidNetworking.post(config.IP + "fetchUserData.php")
                .addBodyParameter("id", String.valueOf(get))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("ada")){
                                namaPenyewa.setText(response.getString("nama_penyewa"));
                                email.setText(response.getString("email"));
                                alamat.setText(response.getString("alamat"));
                                noTelp.setText(response.getString("no_telp"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung dengan database", Toast.LENGTH_LONG).show();

                    }
                });
    }
}
