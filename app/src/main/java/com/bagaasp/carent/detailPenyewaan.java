package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class detailPenyewaan extends AppCompatActivity {
    Button btnSewa;
    BottomSheetDialog btDialog;
    SharedPreferences spM, spU, spP;
    ShimmerFrameLayout shimmerPay;
    SwipeRefreshLayout refreshDetail;
    SimpleDateFormat formatTgl, parseTgl;

    ArrayList<datapaymethod> dataPay;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyAdapter;
    RecyclerView.LayoutManager recyLM;

    TextView prevNamaMobil, prevHargaSewa, prevLamaSewa, prevTotalBayar, textEmpty, prevTanggalBalik, prevTanggalAmbil;
    ImageView imageEmpty, carImage;
    Intent getDataBefore, rentSuccess, rentFailed;
    int lamaSewa;
    String tglBalik, TAG, showTglBalik, showTglAmbil, tglAmbil;
    double totalBayar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penyewaan);

        recyclerView = findViewById(R.id.recy_paymethod);
        recyclerView.setHasFixedSize(true);
        dataPay = new ArrayList<>();
        recyLM = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(recyLM);
        recyAdapter = new ListPayMethodAdapter(dataPay, this);
        recyclerView.setAdapter(recyAdapter);

        btnSewa = findViewById(R.id.btn_prosesSewa);
        prevNamaMobil = findViewById(R.id.preview_mobildisewa);
        prevHargaSewa = findViewById(R.id.preview_hargasewa);
        prevLamaSewa = findViewById(R.id.preview_lamasewa);
        prevTotalBayar = findViewById(R.id.total_bayar);
        shimmerPay = findViewById(R.id.shimmer_paymethod);
        refreshDetail = findViewById(R.id.refresh_detail);
        imageEmpty = findViewById(R.id.empty_image);
        textEmpty = findViewById(R.id.text_empty);
        prevTanggalBalik = findViewById(R.id.preview_tglbalik);
        prevTanggalAmbil = findViewById(R.id.preview_tglambil);

        shimmerPay.startShimmerAnimation();

        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(localeID);

        loadPayMethod();

        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        spP = getApplicationContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);

        getDataBefore = getIntent();
        lamaSewa = getDataBefore.getIntExtra("lamasewa", 0);
        showTglBalik = getDataBefore.getStringExtra("tglBalik");
        showTglAmbil = getDataBefore.getStringExtra("tglAmbil");


        Double biayaSewa = Double.valueOf(spM.getString("hargaSewa", ""));

        prevNamaMobil.setText(spM.getString("namaMobil", ""));
        prevHargaSewa.setText(rupiah.format(biayaSewa) + " x " + lamaSewa + " Hari");

        prevLamaSewa.setText(lamaSewa + " Hari");
        prevTanggalAmbil.setText(showTglAmbil);
        prevTanggalBalik.setText(showTglBalik);

        Double totalHarga = Double.valueOf(biayaSewa) * Double.valueOf(lamaSewa);

        prevTotalBayar.setText(String.valueOf(rupiah.format(totalHarga)));

        totalBayar = totalHarga;

        refreshDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerPay.setVisibility(View.VISIBLE);
                shimmerPay.startShimmerAnimation();
                dataPay.clear();
                loadPayMethod();
                refreshDetail.setRefreshing(false);

            }
        });


        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btDialog = new BottomSheetDialog(detailPenyewaan.this, R.style.BottomSheetDialogTheme);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.instructions, (LinearLayout) findViewById(R.id.instruc));
                btDialog.setContentView(v);
                btDialog.show();

                v.findViewById(R.id.button_confirmsewa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prosesSewa();
                    }
                });
            }
        });
    }

    public void loadPayMethod() {
        AndroidNetworking.post(config.IP + "paymethod.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")) {
                                JSONArray jsonArray = response.getJSONArray("metodebayar");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject paymethod = jsonArray.getJSONObject(i);
                                    datapaymethod payData = new datapaymethod(
                                            paymethod.getString("nmmethod"),
                                            paymethod.getString("logo"),
                                            paymethod.getInt("id")
                                    );
                                    dataPay.add(payData);

                                    shimmerPay.stopShimmerAnimation();
                                    shimmerPay.setVisibility(View.GONE);

                                    imageEmpty.setVisibility(View.GONE);
                                    textEmpty.setVisibility(View.GONE);
                                }
                            } else {
                                imageEmpty.setVisibility(View.VISIBLE);
                                textEmpty.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error "+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        isServerError();
                    }
                });
    }

    public void prosesSewa() {
        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        spP = getApplicationContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);

        getDataBefore = getIntent();
        lamaSewa = getDataBefore.getIntExtra("lamasewa", 0);
        tglBalik = getDataBefore.getStringExtra("tglBalik");
        tglAmbil = getDataBefore.getStringExtra("tglAmbil");

        Double biayaSewa = Double.valueOf(spM.getString("hargaSewa", ""));
        Double totalHarga = Double.valueOf(biayaSewa) * Double.valueOf(lamaSewa);

        AndroidNetworking.post(config.IP + "proses_sewa.php")
                .addBodyParameter("id_user", String.valueOf(spU.getInt("idUser", 0)))
                .addBodyParameter("status", "Menunggu")
                .addBodyParameter("tglsewa", tglAmbil)
                .addBodyParameter("tglkembali", tglBalik)
                .addBodyParameter("lama", String.valueOf(lamaSewa))
                .addBodyParameter("biayarusak", "0")
                .addBodyParameter("id_mobil", String.valueOf(spM.getInt("idMobil", 0)))
                .addBodyParameter("id_paymet", String.valueOf(spP.getInt("idMethod", 0)))
                .addBodyParameter("total_bayar", String.valueOf(totalHarga))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("berhasil")){
                                rentSuccess = new Intent(getApplicationContext(), berhasilsewa.class);
                                startActivity(rentSuccess);

                                SharedPreferences.Editor editorM = spM.edit();
                                editorM.clear().commit();

                                SharedPreferences.Editor editorP = spP.edit();
                                editorP.clear().commit();
                            } else {
                                rentFailed = new Intent(getApplicationContext(), gagalsewa.class);
                                startActivity(rentFailed);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal Terhubung Dengan Server \n" + anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void isServerError(){
        View c = LayoutInflater.from(getApplicationContext()).inflate(R.layout.servergalat, findViewById(R.id.servergalat_contain));
        setContentView(c);

        c.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), beranda.class);
                startActivity(a);
                finishAffinity();
            }
        });
    }

}
