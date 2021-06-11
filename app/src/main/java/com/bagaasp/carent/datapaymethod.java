package com.bagaasp.carent;

public class datapaymethod {
    String namaMethod, logoMethod;
    int kodeMethod;

    public datapaymethod(String namaMethod, String logoMethod, int kodeMethod) {
        this.namaMethod = namaMethod;
        this.logoMethod = logoMethod;
        this.kodeMethod = kodeMethod;
    }

    public String getNamaMethod() {
        return namaMethod;
    }

    public void setNamaMethod(String namaMethod) {
        this.namaMethod = namaMethod;
    }

    public String getLogoMethod() {
        return logoMethod;
    }

    public void setLogoMethod(String logoMethod) {
        this.logoMethod = logoMethod;
    }

    public int getKodeMethod(){
        return kodeMethod;
    }

    public void setKodeMethod(int kodeMethod){
        this.kodeMethod = kodeMethod;
    }
}
