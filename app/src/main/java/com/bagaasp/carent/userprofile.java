package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class userprofile extends AppCompatActivity {

    Button btn_logout, btn_sk, btn_cs, btn_editIdentity;
    TextView tvNIK, tvEmail, tvNoTelp, tvAlamat, tvUsername;
    String userName, email, noTelp, alamat, NIK;
    SharedPreferences spU, spM, spD, spP, spO;
    SwipeRefreshLayout refreshUser;
    CardView userIdentity;

    ShimmerFrameLayout shimmerUsername, shimmerData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        btn_logout = findViewById(R.id.logoutBtn);
        btn_sk = findViewById(R.id.syaratBtn);
        btn_cs = findViewById(R.id.pengaduanBtn);
        btn_editIdentity = findViewById(R.id.ubah_identity);
        refreshUser = findViewById(R.id.refresh_user);
        tvUsername = findViewById(R.id.tvUsername);
        tvNIK = findViewById(R.id.tvNIK);
        tvEmail = findViewById(R.id.tvEmail);
        tvNoTelp = findViewById(R.id.tvNoTelp);
        tvAlamat = findViewById(R.id.tvAlamat);
        shimmerUsername = findViewById(R.id.shimmer_username);
        shimmerData = findViewById(R.id.shimmer_data);
        userIdentity = findViewById(R.id.user_identity);

        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);
        spD = getApplicationContext().getSharedPreferences("fetchDriver", Context.MODE_PRIVATE);
        spP = getApplicationContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
        spO = getApplicationContext().getSharedPreferences("fetchOngkos", Context.MODE_PRIVATE);



        getUserData();
        shimmerUsername.startShimmerAnimation();
        shimmerData.startShimmerAnimation();


        refreshUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerUsername.setVisibility(View.VISIBLE);
                shimmerUsername.startShimmerAnimation();

                shimmerData.setVisibility(View.VISIBLE);
                shimmerData.startShimmerAnimation();

                getUserData();

                refreshUser.setRefreshing(false);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(userprofile.this, R.style.BottomSheetDialogTheme);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_logout, (LinearLayout) findViewById(R.id.user_container));
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();

                v.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(a);

                        SharedPreferences.Editor editor = spU.edit();
                        //editor.putBoolean("login", false);
                        editor.clear().commit();

                        SharedPreferences.Editor editM = spM.edit();
                        editM.clear().commit();

                        SharedPreferences.Editor editD = spD.edit();
                        editD.clear().commit();

                        SharedPreferences.Editor editP = spP.edit();
                        editP.clear().commit();

                        SharedPreferences.Editor editO = spO.edit();
                        editO.clear().commit();

                        finishAffinity();
                    }
                });
            }
        });

        btn_sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialogSK = new BottomSheetDialog(userprofile.this, R.style.BottomSheetDialogTheme);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsyarat, (LinearLayout) findViewById(R.id.bottomsk));
                dialogSK.setContentView(v);
                dialogSK.show();

                v.findViewById(R.id.okengerti).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSK.dismiss();
                    }
                });
            }
        });

        btn_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pengaduan();
            }
        });

        btn_editIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), editdatauser.class);
                startActivity(a);
            }
        });
    }

    private void pengaduan() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/plain");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cscarent@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Pengaduan");
        email.putExtra(Intent.EXTRA_TEXT, "Hai, ini " + spU.getString("username", "") + ". Saya ingin memberikan masukan untuk pelayanan carent! Mohon untuk segera dibalas ya! \n \n Ketik Pesan disini.");
        startActivity(Intent.createChooser(email, "Pilih Platform Email yang Ingin Digunakan"));

    }

    private void getUserData() {
        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        int get = spU.getInt("idUser", 0);

        AndroidNetworking.post(config.IP + "fetchUserData.php")
                .addBodyParameter("id", String.valueOf(get))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("ada")) {
                                userName = response.getString("nama_penyewa");
                                NIK = response.getString("NIK");
                                noTelp = response.getString("no_telp");
                                email = response.getString("email");
                                alamat = response.getString("alamat");
                            }

                            shimmerData.stopShimmerAnimation();
                            shimmerUsername.stopShimmerAnimation();
                            shimmerUsername.setVisibility(View.GONE);
                            shimmerData.setVisibility(View.GONE);


                            tvUsername.setVisibility(View.VISIBLE);
                            tvNIK.setVisibility(View.VISIBLE);
                            tvEmail.setVisibility(View.VISIBLE);
                            tvAlamat.setVisibility(View.VISIBLE);
                            tvNoTelp.setVisibility(View.VISIBLE);

                            tvUsername.setText(userName);
                            tvNIK.setText(NIK);
                            tvNoTelp.setText(noTelp);
                            tvEmail.setText(email);
                            tvAlamat.setText(alamat);

                            userIdentity.setVisibility(View.VISIBLE);


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