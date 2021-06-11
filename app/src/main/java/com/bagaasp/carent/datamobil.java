package com.bagaasp.carent;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;

public class datamobil {

    public String namaMobil, gambarMobil, tipeMesin, jumlahKursi;
    public double hargaSewa;
    public int kodeMobil;

    public datamobil(String namaMobil, double hargaSewa, String gambarMobil, String tipeMesin, String jumlahKursi, int kodeMobil) {
        this.namaMobil = namaMobil;
        this.hargaSewa = hargaSewa;
        this.gambarMobil = gambarMobil;
        this.tipeMesin = tipeMesin;
        this.jumlahKursi = jumlahKursi;
        this.kodeMobil = kodeMobil;
    }


    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public double getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(double hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public String getGambarMobil() {
        return gambarMobil; }

    public void setGambarMobil(String gambarMobil) { this.gambarMobil = gambarMobil ; }

    public String getTipeMesin(){
        return tipeMesin;
    }

    public void setTipeMesin(String tipeMesin){
        this.tipeMesin = tipeMesin;
    }

    public String getJumlahKursi(){
        return jumlahKursi;
    }

    public void setJumlahKursi(String jumlahKursi){
        this.jumlahKursi = jumlahKursi;
    }

    public int getKodeMobil(){
        return kodeMobil;
    }

    public void setKodeMobil(int kodeMobil){
        this.kodeMobil = kodeMobil;
    }
}
