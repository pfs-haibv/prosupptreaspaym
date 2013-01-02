/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.conn;

import com.support.pit.config.ConfigDatabase;
import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.paym.SupportTreasuryPaymApp;
import java.io.IOException;
import com.support.pit.utility.Utility;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class ConnectDB {
    
    /**
     * Thực hiện connect database oracle
     * @return Connection
     * @throws SQLException
     * @throws IOException 
     */
    public static Connection getConnORA() throws SQLException, IOException {
        
        //Load info database oracle         
        ConfigDatabase conf = new ConfigDatabase();
        Properties prop = new Properties();             
        InputStream is = new FileInputStream("CONFIG_DATABASE_ORACLE.CONF");        
        prop.loadFromXML(is);
        Connection conn = null;
        
        try {
            Class.forName(prop.getProperty("db.class"));
            conn = DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.user"), 
                                               prop.getProperty("db.password"));
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Driver not found." + cnfe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Thực hiện các câu lệnh sql database 
     * </p>truy xuất các chứng từ đầy đủ trên pit
     * @param sql
     * @param comment
     * @return arraylist payment on pit
     * @throws SQLException
     * @throws IOException 
     */
    public static ArrayList<TreasuryPayment> sqlDatabase(String sql, String comment) throws SQLException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;        
        String map_cqt[] = new String[3];
        ArrayList<TreasuryPayment> arr_tp = new ArrayList<TreasuryPayment>();
        try {
            conn = SupportTreasuryPaymApp.connORA;
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            int count = 0;
            while (rset.next()) {
                count++;
                TreasuryPayment tp = new TreasuryPayment();
                tp.setCqt(rset.getString("comp_code"));
                tp.setTotal_ct_pit(Integer.parseInt(rset.getString("total_ct_pit")));
                tp.setTran_no(rset.getString("parent_id"));
                tp.setNgay_kb(rset.getString("trea_date_no"));
                tp.setMakb(rset.getString("trea_code"));
                tp.setMa_cqthu(rset.getString("TAX_OFFICE_ID"));
                tp.setNgay_ct(rset.getString("crea_date"));
                tp.setTax_mount(rset.getString("tax_amount"));
                tp.setLcn_owner(rset.getString("lcn_owner"));
                map_cqt = Utility.getMapCQT(rset.getString("trea_code"), rset.getString("TAX_OFFICE_ID"), rset.getString("lcn_owner"));
                tp.setTen_cqt(map_cqt[1]);
                System.out.println(comment + count);
                arr_tp.add(tp);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();;
        } finally {
            rset.close();
            stmt.close();
        }
        return arr_tp;
    }

    /**
     * Thực hiện các câu lệnh sql database truy xuất dữ liệu tdtttct
     * @param sql
     * @param comment
     * @return arraylist payment on tdtttct
     * @throws SQLException
     * @throws IOException 
     */
    public static ArrayList<TreasuryPayment> sqlDatabase_Paym(String sql, String comment) throws SQLException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String map_cqt[] = new String[3];
        ArrayList<TreasuryPayment> arr_tp = new ArrayList<TreasuryPayment>();
        try {
            conn = SupportTreasuryPaymApp.connORA;
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            int count = 0;
            while (rset.next()) {
                count++;
                TreasuryPayment tp = new TreasuryPayment();
                tp.setTran_no(rset.getString("parent_id"));                
                tp.setNgay_kb(rset.getString("crea_date"));
                tp.setLcn_owner(rset.getString("lcn_owner"));
                map_cqt = Utility.getMapCQT(rset.getString("trea_code"), rset.getString("TAX_OFFICE_ID"), rset.getString("lcn_owner"));
                tp.setCqt(map_cqt[0]);
                tp.setMakb(map_cqt[2]);
                tp.setTen_cqt(map_cqt[1]);
                tp.setMa_cqthu(map_cqt[3]);
                System.out.println(comment + count);
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
