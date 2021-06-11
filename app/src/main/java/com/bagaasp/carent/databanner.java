package com.bagaasp.carent;

public class databanner {
    String bannerImg, bannerName, bannerDesc;

    public databanner(String bannerImg, String bannerName, String bannerDesc) {
        this.bannerImg = bannerImg;
        this.bannerName = bannerName;
        this.bannerDesc = bannerDesc;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerDesc() {
        return bannerDesc;
    }

    public void setBannerDesc(String bannerDesc) {
        this.bannerDesc = bannerDesc;
    }
}
