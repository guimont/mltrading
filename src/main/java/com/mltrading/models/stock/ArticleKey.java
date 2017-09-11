package com.mltrading.models.stock;

import java.io.Serializable;

/**
 * Created by gmo on 13/04/2016.
 */
public class ArticleKey implements Serializable {
    String code;
    String date;

    public ArticleKey(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
