/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.conn;

import com.support.pit.datatype.MapCqt;
import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.paym.SupportTreasuryPaymApp;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Message;
import java.io.IOException;
import com.support.pit.utility.Utility;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

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
    public static Connection getConnORA() {
            
        Connection conn = null;
        try {

            Class.forName(SupportTreasuryPaymApp.prop.getProperty("db.class"));
            conn = DriverManager.getConnection(SupportTreasuryPaymApp.prop.getProperty("db.url"), SupportTreasuryPaymApp.prop.getProperty("db.user"),
                    SupportTreasuryPaymApp.prop.getProperty("db.password"));
        } catch (SQLException | ClassNotFoundException e) {

            SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_CONNECTION_DATABASE, e.getMessage());

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
                MapCqt cqt = Utility.getInfoDMuc(rset.getString("trea_code"), rset.getString("TAX_OFFICE_ID"), rset.getString("lcn_owner"));
                tp.setTen_cqt(cqt.getTen_cqt());
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
                MapCqt map_cqt = Utility.getInfoDMuc(rset.getString("trea_code"), rset.getString("TAX_OFFICE_ID"), rset.getString("lcn_owner"));
                tp.setCqt(map_cqt.getMa_cqt());
                tp.setMakb(map_cqt.getMa_kb());
                tp.setTen_cqt(map_cqt.getTen_cqt());
                tp.setMa_cqthu(map_cqt.getMa_cqthu());
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
