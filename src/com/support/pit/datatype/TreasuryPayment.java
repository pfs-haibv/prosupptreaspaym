/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.datatype;

/**
 *
 * @author Administrator
 */
public class TreasuryPayment {
    
    private String filename;
    private String cqt;
    private String ten_cqt;
    private String makb;
    private String ma_cqthu;
    private String tran_no;    
    private String ngay_ct;
    private String ngay_kb;
    private int total_ct_khobac;
    private int total_ct_pit;
    private String lcn_owner;
    private String tax_mount;

    public String getCqt() {
        return cqt;
    }

    public void setCqt(String cqt) {
        this.cqt = cqt;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMa_cqthu() {
        return ma_cqthu;
    }

    public void setMa_cqthu(String ma_cqthu) {
        this.ma_cqthu = ma_cqthu;
    }

    public String getMakb() {
        return makb;
    }

    public void setMakb(String makb) {
        this.makb = makb;
    }

    public String getNgay_ct() {
        return ngay_ct;
    }

    public void setNgay_ct(String ngay_ct) {
        this.ngay_ct = ngay_ct;
    }

    public String getNgay_kb() {
        return ngay_kb;
    }

    public void setNgay_kb(String ngay_kb) {
        this.ngay_kb = ngay_kb;
    }

    public int getTotal_ct_khobac() {
        return total_ct_khobac;
    }

    public void setTotal_ct_khobac(int total_ct_khobac) {
        this.total_ct_khobac = total_ct_khobac;
    }

    public int getTotal_ct_pit() {
        return total_ct_pit;
    }

    public void setTotal_ct_pit(int total_ct_pit) {
        this.total_ct_pit = total_ct_pit;
    }

    public String getTran_no() {
        return tran_no;
    }

    public void setTran_no(String tran_no) {
        this.tran_no = tran_no;
    }

    public TreasuryPayment() {
    }

    public String getTen_cqt() {
        return ten_cqt;
    }

    public void setTen_cqt(String ten_cqt) {
        this.ten_cqt = ten_cqt;
    }

    public String getLcn_owner() {
        return lcn_owner;
    }

    public void setLcn_owner(String lcn_owner) {
        this.lcn_owner = lcn_owner;
    }

    public String getTax_mount() {
        return tax_mount;
    }

    public void setTax_mount(String tax_mount) {
        this.tax_mount = tax_mount;
    }   
    
    public TreasuryPayment(String filename, String cqt, String ten_cqt, String makb, String ma_cqthu, String tran_no, String ngay_ct, String ngay_kb, int total_ct_khobac, int total_ct_pit) {
        this.filename = filename;
        this.cqt = cqt;
        this.ten_cqt = ten_cqt;
        this.makb = makb;
        this.ma_cqthu = ma_cqthu;
        this.tran_no = tran_no;
        this.ngay_ct = ngay_ct;
        this.ngay_kb = ngay_kb;
        this.total_ct_khobac = total_ct_khobac;
        this.total_ct_pit = total_ct_pit;
    }   
    
}
