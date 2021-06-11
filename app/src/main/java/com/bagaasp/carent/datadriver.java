package com.bagaasp.carent;

public class datadriver {
    public String namaDriver, genderDriver, umurDriver, fotoDriver;
    public int kodeDriver;

    public datadriver(String namaDriver, String genderDriver, String umurDriver, String fotoDriver, int kodeDriver) {
        this.namaDriver = namaDriver;
        this.genderDriver = genderDriver;
        this.umurDriver = umurDriver;
        this.fotoDriver = fotoDriver;
        this.kodeDriver = kodeDriver;
    }


    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver;
    }

    public String getGenderDriver() {
        return genderDriver;
    }

    public void setGenderDriver(String genderDriver) {
        this.genderDriver = genderDriver;
    }

    public String getUmurDriver(){
        return umurDriver;
    }

    public void setUmurDriver(String umurDriver){
        this.umurDriver = umurDriver;
    }

    public String getFotoDriver() {
        return fotoDriver;
    }

    public void setFotoDriver(String fotoDriver) {
        this.fotoDriver = fotoDriver;
    }

    public int getKodeDriver(){
        return kodeDriver;
    }

    public void setKodeDriver(int kodeDriver){
        this.kodeDriver = kodeDriver;
    }

}
