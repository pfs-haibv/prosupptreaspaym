package com.support.pit.datatype;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class MapCqt implements Serializable {
    private String ma_cqt;
    private String ma_cqthu;
    private String lcn_owner;
    private String ma_kb;
    private String ten_cqt;
    private String short_name;

    public String getMa_cqt() {
        return ma_cqt;
    }

    public String getLcn_owner() {
        return lcn_owner;
    }

    public void setLcn_owner(String lcn_owner) {
        this.lcn_owner = lcn_owner;
    }

    public String getMa_cqthu() {
        return ma_cqthu;
    }

    public void setMa_cqthu(String ma_cqthu) {
        this.ma_cqthu = ma_cqthu;
    }

    public void setMa_cqt(String ma_cqt) {
        this.ma_cqt = ma_cqt;
    }

    public String getMa_kb() {
        return ma_kb;
    }

    public void setMa_kb(String ma_kb) {
        this.ma_kb = ma_kb;
    }

    public String getTen_cqt() {
        return ten_cqt;
    }

    public void setTen_cqt(String ten_cqt) {
        this.ten_cqt = ten_cqt;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
 
}
