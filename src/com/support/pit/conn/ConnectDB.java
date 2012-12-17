/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.conn;

import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.paym.SupportTreasuryPaymApp;
import java.io.IOException;
import com.support.pit.utility.Utility;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ConnectDB {

    /**
     * Get connection oracle database
     * @return connection to database
     * @throws SQLException 
     */
    public static Connection getConnORA() throws SQLException, IOException {

        //Load info database oracle         
        String config_ora[] = Utility.getConfigORA("configORA.ora").split(",");
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = config_ora[0];
            String username = config_ora[1];
            String password = config_ora[2];
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Driver not found."+cnfe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Thực hiện các câu lệnh sql database
     * @param sql
     * @throws SQLException 
     */
    public static ArrayList<TreasuryPayment> sqlDatabase(String sql, String comment) throws SQLException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        //Lưu thông tin từng file xml   
        String map_cqt[] = new String[2];
         ArrayList<TreasuryPayment> arr_tp = new ArrayList<TreasuryPayment>();
        try {
            conn = SupportTreasuryPaymApp.connORA;
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
           int count = 0;
            while(rset.next()){       
                count++;
                 TreasuryPayment tp = new TreasuryPayment();
                tp.setCqt(rset.getString("comp_code"));
                tp.setTran_no(rset.getString("parent_id"));
                tp.setNgay_kb(rset.getString("trea_date_no"));
                tp.setMakb(rset.getString("trea_code"));
                tp.setMa_cqthu(rset.getString("TAX_OFFICE_ID"));
                tp.setNgay_ct(rset.getString("crea_date"));
                map_cqt = Utility.getMapCQT(rset.getString("comp_code"), rset.getString("TAX_OFFICE_ID"));
                tp.setTen_cqt(map_cqt[1]);
                System.out.println(comment+count);                
               arr_tp.add(tp);
            }            
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            rset.close();
            stmt.close();
        }
        
        return arr_tp;
    }
    
        /**
     * Thực hiện các câu lệnh sql database
     * @param sql
     * @throws SQLException 
     */
    public static ArrayList<TreasuryPayment> sqlDatabase_Paym(String sql, String comment) throws SQLException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        //Lưu thông tin từng file xml   
        String map_cqt[] = new String[2];
         ArrayList<TreasuryPayment> arr_tp = new ArrayList<TreasuryPayment>();
        try {
            conn = SupportTreasuryPaymApp.connORA;
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
           int count = 0;
            while(rset.next()){       
                count++;
                 TreasuryPayment tp = new TreasuryPayment();
                //tp.setCqt(rset.getString("comp_code"));
                tp.setTran_no(rset.getString("parent_id"));
                tp.setNgay_kb(rset.getString("crea_date"));
                tp.setLcn_owner(rset.getString("lcn_owner"));
                //tp.setMa_cqthu(rset.getString("TAX_OFFICE_ID"));
                //tp.setNgay_ct(rset.getString("trea_date_no"));
                //map_cqt = Utility.getMapCQT(rset.getString("comp_code"), rset.getString("TAX_OFFICE_ID"));
                //tp.setTen_cqt(map_cqt[1]);
                System.out.println(comment+count);
               arr_tp.add(tp);
            }            
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            rset.close();
            stmt.close();
        }
        
        return arr_tp;
    }
    
}
