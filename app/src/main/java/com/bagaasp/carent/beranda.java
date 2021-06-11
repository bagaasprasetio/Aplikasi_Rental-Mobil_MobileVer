package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class beranda extends AppCompatActivity {

    TextView hiUsername;
    SharedPreferences spU, spM, spD, spO;
    SwipeRefreshLayout refreshBeranda;
    Button lepas, driver, riwayat, btnAkun;
    ShimmerFrameLayout shimmerBanner, shimmerGreeting;
    ImageView homeImg;
    String username;
    LinearLayout bannerEmpty;

    RecyclerView recyBanner;
    RecyclerView.Adapter recyAdapter;
    RecyclerView.LayoutManager recyLm;
    ArrayList<databanner> dataBanner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        hiUsername = findViewById(R.id.txtusername);
        refreshBeranda = findViewById(R.id.refresh_beranda);
        lepas = findViewById(R.id.btn_lepaskunci);
        driver = findViewById(R.id.btn_driver);
        riwayat = findViewById(R.id.btn_riwayat);
        btnAkun = findViewById(R.id.btn_akun);
        shimmerBanner = findViewById(R.id.shimmer_banner);
        recyBanner = findViewById(R.id.recy_banner);
        homeImg = findViewById(R.id.home_image);
        shimmerGreeting = findViewById(R.id.shimmer_greeting);
        bannerEmpty = findViewById(R.id.banner_empty);


        recyBanner.setHasFixedSize(true);
        dataBanner = new ArrayList<>();
        recyLm = new LinearLayoutManager(beranda.this, RecyclerView.VERTICAL, false);
        recyBanner.setLayoutManager(recyLm);
        recyAdapter = new ListBannerAdapter(beranda.this, dataBanner);
        recyBanner.setAdapter(recyAdapter);


        shimmerBanner.startShimmerAnimation();
        shimmerGreeting.startShimmerAnimation();
        loadBanner();
        getUsername();
        loadImage();


        spM = getApplicationContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);
        spD = getApplicationContext().getSharedPreferences("fetchDriver", Context.MODE_PRIVATE);
        spO = getApplicationContext().getSharedPreferences("fetchOngkos", Context.MODE_PRIVATE);

        hiUsername.setVisibility(View.GONE);


        if (internetCheck() != true ){
            showBottomSheet();
        }

        refreshBeranda.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerBanner.setVisibility(View.VISIBLE);
                shimmerBanner.startShimmerAnimation();
                dataBanner.clear();
                loadBanner();

                shimmerGreeting.setVisibility(View.VISIBLE);
                shimmerGreeting.startShimmerAnimation();
                getUsername();

                loadImage();

                refreshBeranda.setRefreshing(false);

            }
        });

        lepas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editM = spM.edit();
                editM.clear().commit();

                SharedPreferences.Editor editD = spD.edit();
                editD.clear().commit();

                SharedPreferences.Editor editO = spO.edit();
                editO.clear().commit();

                Intent lepasbtn = new Intent(getApplicationContext(), lepaskunci.class);
                startActivity(lepasbtn);

            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editM = spM.edit();
                editM.clear();
                editM.commit();

                SharedPreferences.Editor editD = spD.edit();
                editD.clear();
                editD.commit();

                SharedPreferences.Editor editO = spO.edit();
                editO.clear().commit();

                Intent driverbtn = new Intent(getApplicationContext(), dengandriver.class);
                startActivity(driverbtn);
            }
        });

        riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent riwayatbtn = new Intent(getApplicationContext(), riwayatsewa.class);
                startActivity(riwayatbtn);
            }
        });

        btnAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent akunbtn = new Intent(beranda.this, userprofile.class);
                startActivity(akunbtn);
            }
        });
    }


    private void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(beranda.this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet, (LinearLayout) findViewById(R.id.bottomsheet_container));
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();

        v.findViewById(R.id.try_internet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();

                if (internetCheck() == true){
                    bottomSheetDialog.dismiss();
                } else {
                    showBottomSheet();
                }

            }
        });
    }

    public boolean internetCheck(){
        boolean statusInternet = true;

        ConnectivityManager connectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if ( networkInfo != null && networkInfo.isConnected() == true){
            statusInternet = true;
        } else {
            statusInternet = false;
        }
        return statusInternet;
    }

    public void loadBanner(){
        AndroidNetworking.post(config.IP + "banner.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")){
                                JSONArray jsonArray = response.getJSONArray("data_banner");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    databanner banner = new databanner(
                                            data.getString("image"),
                                            data.getString("name"),
                                            data.getString("desc")
                                    );
                                    dataBanner.add(banner);

                                    shimmerBanner.stopShimmerAnimation();
                                    shimmerBanner.setVisibility(View.GONE);

                                }
                            } else {
                                shimmerBanner.stopShimmerAnimation();
                                shimmerBanner.setVisibility(View.GONE);

                                bannerEmpty.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyAdapter.notifyDataSetChanged();


                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung dengan database", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void getUsername(){
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
                            if (response.getString("response").equals("ada")){
                                username = response.getString("nama_penyewa");
                            }
                            shimmerGreeting.stopShimmerAnimation();
                            shimmerGreeting.setVisibility(View.GONE);

                            hiUsername.setVisibility(View.VISIBLE);
                            hiUsername.setText("Halo, " + username +"!");

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

    private void loadImage(){
        AndroidNetworking.post(config.IP + "home_img.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("berhasil")){
                                String namaGambar = response.getString("image");

                                Glide.with(getApplicationContext())
                                        .load(config.IPHomeimg + namaGambar)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(homeImg);

                                homeImg.setVisibility(View.VISIBLE);
                                homeImg.getAdjustViewBounds();


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
