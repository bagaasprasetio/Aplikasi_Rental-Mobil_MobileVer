package com.bagaasp.carent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

public class lepaskunci extends AppCompatActivity {

    public ArrayList<datamobil> dataMobil;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter recyAdapter;
    public RecyclerView.LayoutManager recyLayoutM;

    ShimmerFrameLayout shimmerCar;
    SwipeRefreshLayout swipeRefresh;

    Button btn_selanjutnya;
    ImageView emptyImage;
    TextView emptyText, txtHint;
    CalendarView calendarTo, calendarFrom;
    Calendar kalender;
    SimpleDateFormat dateFormat, monthFormat;

    int tanggalTo, tglFrom;
    String TAG = "Calendar";
    String showTglPengembalian, bulanPengembalian, bulanKalender, tglBalik, tglAmbil, bulanPengambilan;

    String namaBulan[] = {"", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lepaskunci);

        recyclerView = findViewById(R.id.pilihMbl);
        shimmerCar = findViewById(R.id.shimmer_car);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        btn_selanjutnya = findViewById(R.id.btnNext);
        emptyImage = findViewById(R.id.empty_image);
        emptyText = findViewById(R.id.text_empty);
        calendarTo = findViewById(R.id.calendar_to);
        calendarFrom = findViewById(R.id.calendar_tglPengambilan);

        recyclerView.setHasFixedSize(true);
        dataMobil = new ArrayList<>();
        recyLayoutM = new LinearLayoutManager(lepaskunci.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(recyLayoutM);
        recyAdapter = new ListMobilAdapter(dataMobil, this);
        recyclerView.setAdapter(recyAdapter);

        loadCar();

        shimmerCar.startShimmerAnimation();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerCar.setVisibility(View.VISIBLE);
                shimmerCar.startShimmerAnimation();
                dataMobil.clear();
                loadCar();
                swipeRefresh.setRefreshing(false);
            }
        });

        calendarFrom.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {
                tglFrom = date;
                month = month + 1;

                if ( month == 1 ) {
                    bulanPengambilan = namaBulan[1];
                } else if ( month == 2 ) {
                    bulanPengambilan = namaBulan[2];
                } else if ( month == 3 ) {
                    bulanPengambilan = namaBulan[3];
                } else if ( month == 4 ) {
                    bulanPengambilan = namaBulan[4];
                } else if ( month == 5 ) {
                    bulanPengambilan = namaBulan[5];
                } else if ( month == 6 ) {
                    bulanPengambilan = namaBulan[6];
                } else if ( month == 7 ) {
                    bulanPengambilan = namaBulan[7];
                } else if ( month == 8 ) {
                    bulanPengambilan = namaBulan[8];
                } else if ( month == 9 ) {
                    bulanPengambilan = namaBulan[9];
                } else if ( month == 10 ) {
                    bulanPengambilan = namaBulan[10];
                } else if ( month == 11 ) {
                    bulanPengambilan = namaBulan[11];
                } else if ( month == 12 ) {
                    bulanPengambilan = namaBulan[12];
                } else {
                    bulanPengambilan = "NULL";
                }

                tglAmbil = date + " " + bulanPengambilan + " " + year;
            }
        });

        calendarTo.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {
                tanggalTo = date;
                month = month + 1;
                bulanKalender = String.valueOf(month);

                if ( month == 1 ) {
                    bulanPengembalian = namaBulan[1];
                } else if ( month == 2 ) {
                    bulanPengembalian = namaBulan[2];
                } else if ( month == 3 ) {
                    bulanPengembalian = namaBulan[3];
                } else if ( month == 4 ) {
                    bulanPengembalian = namaBulan[4];
                } else if ( month == 5 ) {
                    bulanPengembalian = namaBulan[5];
                } else if ( month == 6 ) {
                    bulanPengembalian = namaBulan[6];
                } else if ( month == 7 ) {
                    bulanPengembalian = namaBulan[7];
                } else if ( month == 8 ) {
                    bulanPengembalian = namaBulan[8];
                } else if ( month == 9 ) {
                    bulanPengembalian = namaBulan[9];
                } else if ( month == 10 ) {
                    bulanPengembalian = namaBulan[10];
                } else if ( month == 11 ) {
                    bulanPengembalian = namaBulan[11];
                } else if ( month == 12 ) {
                    bulanPengembalian = namaBulan[12];
                } else {
                    bulanPengembalian = "NULL";
                }

                tglBalik = date + " " + bulanPengembalian + " " + year;

            }
        });

        btn_selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent a = new Intent(getApplicationContext(), detailPenyewaan.class);

                    /* kalender = Calendar.getInstance();
                    dateFormat = new SimpleDateFormat("dd");
                    String currentDate = dateFormat.format(kalender.getTime()); */

                    Date bulan = new Date();
                    monthFormat = new SimpleDateFormat("M");
                    String currentMonth = monthFormat.format(bulan);

                    int lamaSewa = tanggalTo - tglFrom;


                    if ( lamaSewa <= 0){
                        Toast.makeText(getApplicationContext(), "Pilih tanggal yang bener!", Toast.LENGTH_LONG).show();
                    } else if (lamaSewa > 7){
                        Toast.makeText(getApplicationContext(), "Maksimal cuma 7 hari ya!" + lamaSewa, Toast.LENGTH_LONG).show();
                    } else if ( currentMonth.equals(bulanKalender)){
                        a.putExtra("lamasewa", lamaSewa + 1);
                        a.putExtra("tglBalik", tglBalik);
                        a.putExtra("tglAmbil", tglAmbil);
                        startActivity(a);
                    } else {
                        Toast.makeText(getApplicationContext(), "Bulan ngaco beb", Toast.LENGTH_LONG).show();
                    }

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

                        recyAdapter.notifyDataSetChanged();

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
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.servergalat, (LinearLayout) findViewById(R.id.servergalat_contain));
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
}
