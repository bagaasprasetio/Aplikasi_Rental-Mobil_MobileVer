package com.bagaasp.carent;

public class datariwayat {

    public String tanggalSewa, waktuPenyewan, namaMobil, namaDriver, lamaSewa, kodePenyewaan, statusPenyewaan, metodeBayar, gambarMobil, tglAmbil, tglBalik;
    public double totalPembayaran, biayaMobil, biayaDriver, biayaKerusakan;

    public datariwayat(String tanggalSewa, String waktuPenyewan, String namaMobil, String namaDriver, double biayaMobil, String lamaSewa, double biayaDriver,  double totalPembayaran, String kodePenyewaan, String statusPenyewaan, double biayaKerusakan, String metodeBayar, String gambarMobil, String tglAmbil, String tglBalik) {
        this.tanggalSewa = tanggalSewa;
        this.waktuPenyewan = waktuPenyewan;
        this.namaMobil = namaMobil;
        this.namaDriver = namaDriver;
        this.biayaMobil = biayaMobil;
        this.lamaSewa = lamaSewa;
        this.biayaDriver = biayaDriver;
        this.totalPembayaran = totalPembayaran;
        this.kodePenyewaan = kodePenyewaan;
        this.statusPenyewaan = statusPenyewaan;
        this.biayaKerusakan = biayaKerusakan;
        this.metodeBayar = metodeBayar;
        this.gambarMobil = gambarMobil;
        this.tglAmbil = tglAmbil;
        this.tglBalik = tglBalik;
    }

    public String getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(String tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }

    public String getWaktuPenyewan() {
        return waktuPenyewan;
    }

    public void setWaktuPenyewan(String waktuPenyewan) {
        this.waktuPenyewan = waktuPenyewan;
    }

    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver;
    }


    public double getTotalPembayaran() {
        return totalPembayaran;
    }

    public void setTotalPembayaran(double totalPembayaran) {
        this.totalPembayaran = totalPembayaran;
    }

    public double getBiayaMobil() {
        return biayaMobil;
    }

    public void setBiayaMobil(double biayaMobil) {
        this.biayaMobil = biayaMobil;
    }

    public double getBiayaDriver() {
        return biayaDriver;
    }

    public void setBiayaDriver(double biayaDriver) {
        this.biayaDriver = biayaDriver;
    }

    public String getLamaSewa() {
        return lamaSewa;
    }

    public void setLamaSewa(String lamaSewa) {
        this.lamaSewa = lamaSewa;
    }

    public String getKodePenyewaan(){
        return kodePenyewaan;
    }

    public void setKodePenyewaan(String kodePenyewaan){
        this.kodePenyewaan = kodePenyewaan;
    }

    public String getStatusPenyewaan(){
        return statusPenyewaan;
    }

    public void setStatusPenyewaan(String statusPenyewaan){
        this.statusPenyewaan = statusPenyewaan;
    }

    public double getBiayaKerusakan(){
        return biayaKerusakan;
    }

    public void setBiayaKerusakan(double biayaKerusakan){
        this.biayaKerusakan = biayaKerusakan;
    }

    public String getMetodeBayar(){
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar){
        this.metodeBayar = metodeBayar;
    }

    public String getGambarMobil() {
        return gambarMobil;
    }

    public void setGambarMobil(String gambarMobil){
        this.gambarMobil = gambarMobil;
    }

    public String getTglAmbil(){
        return tglAmbil;
    }

    public void setTglAmbil(String tglAmbil){
        this.tglAmbil = tglAmbil;
    }

    public String getTglBalik(){
        return tglBalik;
    }

    public void setTglBalik(String tglBalik){
        this.tglBalik = tglBalik;
    }
}
