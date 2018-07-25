package com.example.littlekids8.wanandroid.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerData {

    /**
     * data : []
     * errorCode : 0
     * errorMsg :
     */

    private int errorCode;
    private String errorMsg;

    @SerializedName("data")
    private List<Banner> bannerList;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setData(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }
}
