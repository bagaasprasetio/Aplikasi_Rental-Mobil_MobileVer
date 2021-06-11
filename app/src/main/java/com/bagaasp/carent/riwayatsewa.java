package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class riwayatsewa extends AppCompatActivity {

    ShimmerFrameLayout shimmerEffect;
    SwipeRefreshLayout refresh;
    SharedPreferences sp;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager lm;
    public RecyclerView.Adapter adapter;
    public ArrayList<datariwayat> dataRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayatsewa);

        shimmerEffect = findViewById(R.id.shimmer_riwayat);
        refresh = findViewById(R.id.refresh_data);
        recyclerView = findViewById(R.id.recy_riwayat);

        recyclerView.setHasFixedSize(true);
        dataRiwayat = new ArrayList<>();
        lm = new LinearLayoutManager(riwayatsewa.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        adapter = new ListRiwayatAdapter(dataRiwayat, this);
        recyclerView.setAdapter(adapter);

        shimmerEffect.startShimmerAnimation();
        loadRiwayatSewa();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerEffect.setVisibility(View.VISIBLE);
                shimmerEffect.startShimmerAnimation();
                dataRiwayat.clear();
                loadRiwayatSewa();

                refresh.setRefreshing(false);
            }
        });

    }

    public void loadRiwayatSewa(){
        sp = getApplicationContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE);

        AndroidNetworking.post(config.IP + "riwayat.php")
                .addBodyParameter("id_user", String.valueOf(sp.getInt("idUser", 0)))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")) {
                                JSONArray jsonArray = response.getJSONArray("riwayatsewa");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    datariwayat item = new datariwayat(
                                            jsonObject.getString("tanggal_penyewaan"),
                                            jsonObject.getString("waktu_penyewaan"),
                                            jsonObject.getString("mobil_yang_disewa"),
                                            jsonObject.getString("driver"),
                                            jsonObject.getDouble("harga_sewa_mobil"),
                                            jsonObject.getString("lama_sewa"),
                                            jsonObject.getDouble("biaya_driver"),
                                            jsonObject.getDouble("total_pembayaran"),
                                            jsonObject.getString("kode_penyewaan"),
                                            jsonObject.getString("status_sewa"),
                                            jsonObject.getDouble("b_rusak"),
                                            jsonObject.getString("method"),
                                            jsonObject.getString("gambar"),
                                            jsonObject.getString("tgl_pengambilan"),
                                            jsonObject.getString("tgl_pengembalian")
                                    );
                                    dataRiwayat.add(item);

                                    adapter.notifyDataSetChanged();

                                    shimmerEffect.stopShimmerAnimation();
                                    shimmerEffect.setVisibility(View.GONE);
                                }
                            } else {
                                isDataEmpty();
                            }
                        } catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onError(ANError anError) {
                        isServerError();
                    }
                });
    }

    public void isDataEmpty(){
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.empty, (LinearLayout) findViewById(R.id.empty_contain));
        setContentView(v);

        v.findViewById(R.id.try_rent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), beranda.class);
                startActivity(a);
                finishAffinity();
            }
        });


    }

    public void isServerError(){
        View a = LayoutInflater.from(getApplicationContext()).inflate(R.layout.servergalat, findViewById(R.id.servergalat_contain));
        setContentView(a);

        a.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(getApplicationContext(), beranda.class);
                startActivity(b);
                finishAffinity();
            }
        });

    }
}
