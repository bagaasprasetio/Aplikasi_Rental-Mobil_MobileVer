package com.bagaasp.carent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class dengandriver extends AppCompatActivity {

    public ArrayList<datadriver> dataDriver;
    public ArrayList<datamobil> dataMobil;
    public ArrayList<dataongkos> dataOngkos;
    public RecyclerView recyclerViewD, recyclerViewM, recyclerViewO;
    public RecyclerView.Adapter adapterD, adapterM, adapterO;
    public RecyclerView.LayoutManager layoutManagerD, layoutManagerM, layoutManagerO;

    datadriver dtDrv;

    ShimmerFrameLayout shimmerCar, shimmerDriver, shimmerOngkos;
    SwipeRefreshLayout refreshData;
    Button btnSelanjutnya;
    ImageView emptyImage, emptyImageDriver;
    TextView emptyText, emptyTextDriver, biayaDriver;
    CalendarView calendarV, calendarTglSewa;
    Calendar kalender;
    SimpleDateFormat dateFormat, monthFormat;

    String tglPengembalian, bulan, calendarMonth, tglAmbil, bulanBooking;
    int userSelectedReturnDate, userSelectedGrabDate;

    String TAG = "Kalender";

    String namaBulan[] = {"", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dengandriver);

        recyclerViewD = findViewById(R.id.pilih_driver);
        recyclerViewD.setHasFixedSize(true);
        dataDriver = new ArrayList<>();
        layoutManagerD = new LinearLayoutManager(dengandriver.this, RecyclerView.HORIZONTAL, false);
        recyclerViewD.setLayoutManager(layoutManagerD);
        adapterD = new ListDriverAdapter(this, dataDriver);
        recyclerViewD.setAdapter(adapterD);


        recyclerViewM = findViewById(R.id.pilih_mbl);
        recyclerViewM.setHasFixedSize(true);
        dataMobil = new ArrayList<>();
        layoutManagerM = new LinearLayoutManager(dengandriver.this, RecyclerView.VERTICAL, false);
        recyclerViewM.setLayoutManager(layoutManagerM);
        adapterM = new ListMobilAdapter(dataMobil, this);
        recyclerViewM.setAdapter(adapterM);


        recyclerViewO = findViewById(R.id.recy_ongkos);
        recyclerViewO.setHasFixedSize(true);
        dataOngkos = new ArrayList<>();
        layoutManagerO = new LinearLayoutManager(dengandriver.this, RecyclerView.VERTICAL, false);
        recyclerViewO.setLayoutManager(layoutManagerO);
        adapterO = new ListOngkosAdapter(this, dataOngkos);
        recyclerViewO.setAdapter(adapterO);


        loadDriver();
        loadCar();
        loadOngkos();

        shimmerCar = findViewById(R.id.shimmer_car);
        shimmerDriver = findViewById(R.id.shimmer_driver);
        shimmerOngkos = findViewById(R.id.shimmer_ongkos);
        refreshData = findViewById(R.id.swipe_refresh);
        btnSelanjutnya = findViewById(R.id.btn_selanjutnya);
        calendarV = findViewById(R.id.calendar_driver);
        emptyImage = findViewById(R.id.empty_image);
        emptyText = findViewById(R.id.text_empty);
        emptyImageDriver = findViewById(R.id.empty_imageDriver);
        emptyTextDriver = findViewById(R.id.text_emptyDriver);
        calendarTglSewa = findViewById(R.id.calendar_driver_tglsewa);


        shimmerCar.startShimmerAnimation();
        shimmerDriver.startShimmerAnimation();
        shimmerOngkos.startShimmerAnimation();

        refreshData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerCar.setVisibility(View.VISIBLE);
                shimmerCar.startShimmerAnimation();
                dataMobil.clear();
                loadCar();

                shimmerDriver.setVisibility(View.VISIBLE);
                shimmerDriver.startShimmerAnimation();
                dataDriver.clear();
                loadDriver();

                shimmerOngkos.setVisibility(View.VISIBLE);
                shimmerOngkos.startShimmerAnimation();
                dataOngkos.clear();
                loadOngkos();

                refreshData.setRefreshing(false);
            }
        });

        calendarTglSewa.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {
                userSelectedGrabDate = date;
                month = month + 1;

                if ( month == 1 ) {
                    bulanBooking = namaBulan[1];
                } else if ( month == 2 ) {
                    bulanBooking = namaBulan[2];
                } else if ( month == 3 ) {
                    bulanBooking = namaBulan[3];
                } else if ( month == 4 ) {
                    bulanBooking = namaBulan[4];
                } else if ( month == 5 ) {
                    bulanBooking = namaBulan[5];
                } else if ( month == 6 ) {
                    bulanBooking = namaBulan[6];
                } else if ( month == 7 ) {
                    bulanBooking = namaBulan[7];
                } else if ( month == 8 ) {
                    bulanBooking = namaBulan[8];
                } else if ( month == 9 ) {
                    bulanBooking = namaBulan[9];
                } else if ( month == 10 ) {
                    bulanBooking = namaBulan[10];
                } else if ( month == 11 ) {
                    bulanBooking = namaBulan[11];
                } else if ( month == 12 ) {
                    bulanBooking= namaBulan[12];
                } else {
                    bulanBooking = "NULL";
                }

                tglAmbil = date + " " + bulanBooking + " " + year;


            }
        });


        calendarV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {
                userSelectedReturnDate = date;
                month = month + 1;
                calendarMonth = String.valueOf(month);

                if ( month == 1 ) {
                    bulan = namaBulan[1];
                } else if ( month == 2 ) {
                    bulan = namaBulan[2];
                } else if ( month == 3 ) {
                    bulan = namaBulan[3];
                } else if ( month == 4 ) {
                    bulan = namaBulan[4];
                } else if ( month == 5 ) {
                    bulan = namaBulan[5];
                } else if ( month == 6 ) {
                    bulan = namaBulan[6];
                } else if ( month == 7 ) {
                    bulan = namaBulan[7];
                } else if ( month == 8 ) {
                    bulan = namaBulan[8];
                } else if ( month == 9 ) {
                    bulan = namaBulan[9];
                } else if ( month == 10 ) {
                    bulan = namaBulan[10];
                } else if ( month == 11 ) {
                    bulan = namaBulan[11];
                } else if ( month == 12 ) {
                    bulan = namaBulan[12];
                } else {
                    bulan = "NULL";
                }

                tglPengembalian = date + " " + bulan + " " + year;


            }
        });

        btnSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), detailPenyewaanDriver.class);

                /* kalender = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("dd");
                String currentDate = dateFormat.format(kalender.getTime());

                Date bulan = new Date();
                monthFormat = new SimpleDateFormat("M");
                String currentMonth = monthFormat.format(bulan); */


                int lamaSewa = userSelectedReturnDate - userSelectedGrabDate;

                Log.d(TAG, "lama" + lamaSewa);

                if (lamaSewa <= 0){
                    Toast.makeText(getApplicationContext(), "Tanggal kurang dri 0", Toast.LENGTH_LONG).show();
                } else if (lamaSewa > 7){
                    Toast.makeText(getApplicationContext(), "Tanggal lebih dari 7 Hari", Toast.LENGTH_LONG).show();
                } else {
                    a.putExtra("lamasewa", lamaSewa + 1);
                    a.putExtra("tglbalik", tglPengembalian);
                    a.putExtra("tglAmbil", tglAmbil);

                    startActivity(a);
                }


            }
        });

    }

    public void loadDriver(){
        AndroidNetworking.post(config.IP + "driver.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")) {
                                JSONArray jsonArray = response.getJSONArray("datadriver");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    datadriver item = new datadriver(
                                            data.getString("namadriver"),
                                            data.getString("gender"),
                                            data.getString("umur"),
                                            data.getString("foto"),
                                            data.getInt("id")
                                    );
                                    dataDriver.add(item);

                                    emptyImageDriver.setVisibility(View.GONE);
                                    emptyTextDriver.setVisibility(View.GONE);
                                }
                            } else {
                                emptyImageDriver.setVisibility(View.VISIBLE);
                                emptyTextDriver.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e){
                            isServerError();
                        }

                        adapterD.notifyDataSetChanged();

                        shimmerDriver.stopShimmerAnimation();
                        shimmerDriver.setVisibility(View.GONE);

                    }
                    @Override
                    public void onError(ANError anError) {
                        isServerError();
                    }
                });
    }

    public void loadCar(){
        AndroidNetworking.post(config.IP + "mobil.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")) {
                                JSONArray jsonArray = response.getJSONArray("datamobil");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    datamobil item = new datamobil(
                                            data.getString("mobil"),
                                            data.getDouble("harga"),
                                            data.getString("gambarmobil"),
                                            data.getString("tipemesin"),
                                            data.getString("jmlkursi"),
                                            data.getInt("id")
                                    );
                                    dataMobil.add(item);

                                    emptyImage.setVisibility(View.GONE);
                                    emptyText.setVisibility(View.GONE);
                                }

                            } else {
                                emptyImage.setVisibility(View.VISIBLE);
                                emptyText.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e){
                            isServerError();
                        }

                        adapterM.notifyDataSetChanged();

                        shimmerCar.stopShimmerAnimation();
                        shimmerCar.setVisibility(View.GONE);

                    }
                    @Override
                    public void onError(ANError anError) {
                        isServerError();

                    }
                });
    }

    public void isServerError(){
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.servergalat, findViewById(R.id.servergalat_contain));
        setContentView(v);

        v.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), beranda.class);
                startActivity(a);
                finishAffinity();
            }
        });
    }

    public void loadOngkos(){
        AndroidNetworking.post(config.IP + "ongkos.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("tersedia")){
                                JSONArray jsonArray = response.getJSONArray("dataongkos");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    dataongkos ongkos = new dataongkos(
                                            jsonObject.getString("tujuan"),
                                            jsonObject.getDouble("biaya"),
                                            jsonObject.getInt("id")
                                    );
                                    dataOngkos.add(ongkos);

                                    shimmerOngkos.stopShimmerAnimation();
                                    shimmerOngkos.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Kosong", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapterO.notifyDataSetChanged();


                    }
                    @Override
                    public void onError(ANError anError) {
                        isServerError();
                    }
                });

    }

}






