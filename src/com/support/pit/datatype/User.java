/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.datatype;

/**
 *
 * @author Administrator
 */
public class User {

    private String account;
    private String name;
    private String email;
    private String phongban;
    private String[] role;
    private String cqt;
    private String short_name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhongban() {
        return phongban;
    }

    public void setPhongban(String phongban) {
        this.phongban = phongban;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCqt() {
        return cqt;
    }

    public void setCqt(String cqt) {
        this.cqt = cqt;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
       
    
    public User() {
    }

}
