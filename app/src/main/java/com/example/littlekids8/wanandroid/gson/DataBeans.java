package com.example.littlekids8.wanandroid.gson;

import com.google.gson.annotations.SerializedName;

public class DataBeans {

    /**
     * articleListData : {}
     * errorCode : 0
     * errorMsg :
     */

    @SerializedName("data")
    private ArticleListData articleListData;
    private int errorCode;
    private String errorMsg;

    public ArticleListData getArticleListData() {
        return articleListData;
    }

    public void setData(ArticleListData articleListData) {
        this.articleListData = articleListData;
    }

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

    public static class DataBean {
    }
}
