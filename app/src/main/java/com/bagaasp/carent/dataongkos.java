package com.bagaasp.carent;

public class dataongkos {
    String tujuan;
    Double biayaDriver;
    int kodeOngkos;

    public dataongkos(String tujuan, Double biayaDriver, int kodeOngkos) {
        this.tujuan = tujuan;
        this.biayaDriver = biayaDriver;
        this.kodeOngkos  = kodeOngkos;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public Double getBiayaDriver() {
        return biayaDriver;
    }

    public void setBiayaDriver(Double biayaDriver) {
        this.biayaDriver = biayaDriver;
    }

    public int getKodeOngkos(){
        return kodeOngkos;
    }

    public void setKodeOngkos(int kodeOngkos){
        this.kodeOngkos = kodeOngkos;
    }
}
