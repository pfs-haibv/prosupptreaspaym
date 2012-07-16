/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.datatype;

/**
 *
 * @author Administrator
 */
public class MapRole {
    
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MapRole(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public MapRole() {
    }
       
    
}
