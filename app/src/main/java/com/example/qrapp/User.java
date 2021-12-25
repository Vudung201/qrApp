package com.example.qrapp;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private  String masv;
    private String tenDN;

    public User(String name, String masv, String tenDN)  {
        this.name = name;
        this.masv = masv;
        this.tenDN = tenDN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getTenDN() {
        return tenDN;
    }

    public void setTenDN(String tenDN) {
        this.tenDN = tenDN;
    }
}