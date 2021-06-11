package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Locale;

public class detailPenyewaanDriver extends AppCompatActivity {
    Button btnSewa;
    TextView prevNamaDrv, prevBiayaDrv, prevBiayaSewaMbl, prevNamaMbl, prevLamaSewa, txtEmpty, prevTotalBayar, prevTglBalik, prevTglAmbil, prevTujuan;
    ImageView imgEmpty, prevGambarMobil, prevFotoDriver;
    ShimmerFrameLayout shimmerPay, shimmerMobil, shimmerDriver;
    SwipeRefreshLayout refresh;
    BottomSheetDialog btDialog;
    SharedPreferences spD, spM, spU, spP, spO;
    Intent getDataBefore, rentSuccess, rentFail;
    int lamaSewa;
    String tglBalik, namaMobil, namaDriver, ongkos, biayaSewa, tglAmbil, gambarMobil, fotoDriver, tujuan;

    ArrayList<datapaymethod> dataPay;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyLM;
    RecyclerView.Adapter recyAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penyewaan_driver);

        prevNamaDrv = findViewById(R.id.preview_namadriver);
        prevBiayaDrv = findViewById(R.id.preview_biayadriver);
        prevBiayaSewaMbl = findViewById(R.id.preview_hargasewa);
        prevNamaMbl = findViewById(R.id.preview_mobildisewa);
        prevLamaSewa = findViewById(R.id.preview_lamasewa);
        prevTglBalik = findViewById(R.id.preview_tglpengembalian);
        prevTotalBayar = findViewById(R.id.total_bayar);
        prevTglAmbil = findViewById(R.id.preview_tglpengambilan);
        prevTujuan = findViewById(R.id.prev_detailtujuandriver);

        txtEmpty = findViewById(R.id.text_empty);
        imgEmpty = findViewById(R.id.empty_image);
        btnSewa = findViewById(R.id.btn_prosesSewa);
        refresh = findViewById(R.id.refresh_detail);
        shimmerPay = findViewById(R.id.shimmer_paymethod);
        recyclerView = findViewById(R.id.recy_paymethod);

        recyclerView.setHasFixedSize(true);
        dataPay = new ArrayList<>();
        recyLM = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(recyLM);
        recyAdapter = new ListPayMethodAdapter(dataPay, this);
        recyclerView.setAdapter(recyAdapter);

        shimmerPay.startShimmerAnimation();

        loadPayMethod();


        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);
        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        spP = getApplicationContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
        spD = getApplicationContext().getSharedPreferences("fetchDriver", Context.MODE_PRIVATE);
        spO = getApplicationContext().getSharedPreferences("fetchOngkos", Context.MODE_PRIVATE);

        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(localeID);

        getDataBefore = getIntent();
        lamaSewa = getDataBefore.getIntExtra("lamasewa", 0);
        tglBalik = getDataBefore.getStringExtra("tglbalik");
        tglAmbil = getDataBefore.getStringExtra("tglAmbil");

        prevTglAmbil.setText(tglAmbil);
        prevTglBalik.setText(tglBalik);
        prevLamaSewa.setText(lamaSewa + " Hari");


        Double biayaSewa = Double.valueOf(spM.getString("hargaSewa", ""));
        Double biayaDriver = Double.valueOf(spO.getString("ongkosDrv", ""));

        prevNamaMbl.setText(spM.getString("namaMobil", ""));
        prevNamaDrv.setText(spD.getString("namaDriver", ""));

        prevBiayaSewaMbl.setText(rupiah.format(biayaSewa) + " x " + lamaSewa + " Hari");
        prevBiayaDrv.setText(rupiah.format(biayaDriver) + " x " + lamaSewa + " Hari");
        prevTujuan.setText(spO.getString("tujuan", ""));



        Double totalBayar = biayaSewa + biayaDriver;
        Double grandPrice = totalBayar * Double.valueOf(lamaSewa);

        prevTotalBayar.setText(rupiah.format(grandPrice));



        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btDialog = new BottomSheetDialog(detailPenyewaanDriver.this, R.style.BottomSheetDialogTheme);
                View b = LayoutInflater.from(getApplicationContext()).inflate(R.layout.instructions, findViewById(R.id.instruc));
                btDialog.setContentView(b);
                btDialog.show();

                b.findViewById(R.id.button_confirmsewa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prosesSewa();
                    }
                });
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                shimmerPay.setVisibility(View.VISIBLE);
                shimmerPay.startShimmerAnimation();
                dataPay.clear();
                loadPayMethod();

                refresh.setRefreshing(false);
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

                                    imgEmpty.setVisibility(View.GONE);
                                    txtEmpty.setVisibility(View.GONE);
                                }
                            } else {
                                imgEmpty.setVisibility(View.VISIBLE);
                                txtEmpty.setVisibility(View.VISIBLE);
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

    public void isServerError(){
        View c = LayoutInflater.from(getApplicationContext()).inflate(R.layout.servergalat, findViewById(R.id.servergalat_contain));
        setContentView(c);

        c.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(getApplicationContext(), beranda.class);
                startActivity(b);
                finishAffinity();
            }
        });
    }

    private void prosesSewa(){
        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);
        spU = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        spP = getApplicationContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
        spD = getApplicationContext().getSharedPreferences("fetchDriver", Context.MODE_PRIVATE);
        spO = getApplicationContext().getSharedPreferences("fetchOngkos", Context.MODE_PRIVATE);

        getDataBefore = getIntent();
        lamaSewa = getDataBefore.getIntExtra("lamasewa", 0);
        tglBalik = getDataBefore.getStringExtra("tglbalik");
        tglAmbil = getDataBefore.getStringExtra("tglAmbil");

        Double biayaSewa = Double.valueOf(spM.getString("hargaSewa", ""));
        Double biayaDriver = Double.valueOf(spO.getString("ongkosDrv", ""));

        Double totalBayar = biayaSewa + biayaDriver;
        Double grandPrice = totalBayar * Double.valueOf(lamaSewa);

        AndroidNetworking.post(config.IP + "proses_sewa.php")
                .addBodyParameter("id_user", String.valueOf(spU.getInt("idUser", 0)))
                .addBodyParameter("status", "Menunggu")
                .addBodyParameter("tglsewa", tglAmbil)
                .addBodyParameter("tglkembali", tglBalik)
                .addBodyParameter("lama", String.valueOf(lamaSewa))
                .addBodyParameter("id_mobil", String.valueOf(spM.getInt("idMobil", 0)))
                .addBodyParameter("id_driver", String.valueOf(spD.getInt("idDriver", 0)))
                .addBodyParameter("biayarusak", "0")
                .addBodyParameter("id_paymet", String.valueOf(spP.getInt("idMethod", 0)))
                .addBodyParameter("id_ongkos", String.valueOf(spO.getInt("kdOngkos", 0)))
                .addBodyParameter("total_bayar", String.valueOf(grandPrice))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("berhasil")){
                                rentSuccess = new Intent(getApplicationContext(), berhasilsewa.class);
                                startActivity(rentSuccess);

                                SharedPreferences.Editor editM = spM.edit();
                                editM.clear().commit();

                                SharedPreferences.Editor editD = spD.edit();
                                editD.clear().commit();

                                SharedPreferences.Editor editP = spP.edit();
                                editP.clear().commit();

                                finish();
                            } else {
                                rentFail = new Intent(getApplicationContext(), gagalsewa.class);
                                startActivity(rentFail);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal Terhubung Dengan Server \n" + anError.getErrorBody(), Toast.LENGTH_LONG).show();

                    }
                });
    }

}
