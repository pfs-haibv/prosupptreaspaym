/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.utility;

import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.paym.SupportTreasuryPaymApp;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Message;
import com.support.pit.ziper.FolderZiper;
import java.io.FileNotFoundException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.ArrayUtils;

public class GetFileFTP {

    public static void getFTP(ArrayList<TreasuryPayment> arr_tp_pit, ArrayList<TreasuryPayment> arr_tp_thieu, String fold_ftp, String excel_fld) {
        System.out.println("Loading files on ftp server ...please wait");
        FileOutputStream fos = null;
        FTPClient client = new FTPClient();
        ArrayList<TreasuryPayment> arr_tp_thieu_ko_bk = new ArrayList<TreasuryPayment>();
        ArrayList<TreasuryPayment> arr_tp_thieu_co_bk = new ArrayList<TreasuryPayment>();
        try {

            client.connect("10.64.85.28");
            client.login("px1adm", "Gdt$2012");
            client.changeWorkingDirectory("/DataPaym/Archive");

            String[] names = client.listNames();
            String[] tranNoToFind = new String[names.length];
            int r = 0;
            String[] getToTranNo = new String[2];
            //Load and get Tran_No
            for (String name : names) {
                getToTranNo = name.split("_");
                if (getToTranNo.length >= 2) {
                    tranNoToFind[r] = getToTranNo[1];
                }
                r++;
            }

            String file_name = "";
            int indexof = 0;
            int total = arr_tp_thieu.size();
            for (int i = 0; i < total; i++) {

                indexof = ArrayUtils.indexOf(tranNoToFind, arr_tp_thieu.get(i).getTran_no());
                if (indexof != -1) {
                    file_name = names[indexof];
                    TreasuryPayment tp1 = new TreasuryPayment();
                    tp1.setLcn_owner(arr_tp_thieu.get(i).getLcn_owner());
                    tp1.setNgay_kb(arr_tp_thieu.get(i).getNgay_kb());
                    tp1.setTran_no(arr_tp_thieu.get(i).getTran_no());
                    tp1.setCqt(arr_tp_thieu.get(i).getCqt());
                    tp1.setMa_cqthu(arr_tp_thieu.get(i).getMa_cqthu());
                    tp1.setTen_cqt(arr_tp_thieu.get(i).getTen_cqt());
                    tp1.setMakb(arr_tp_thieu.get(i).getMakb());
                    tp1.setFilename(file_name);
                    arr_tp_thieu_co_bk.add(tp1);
                } //add chứng từ ko tồn tại trong backup
                else {
                    TreasuryPayment tp = new TreasuryPayment();
                    tp.setLcn_owner(arr_tp_thieu.get(i).getLcn_owner());
                    tp.setNgay_kb(arr_tp_thieu.get(i).getNgay_kb());
                    tp.setTran_no(arr_tp_thieu.get(i).getTran_no());
                    tp.setCqt(arr_tp_thieu.get(i).getCqt());
                    tp.setMa_cqthu(arr_tp_thieu.get(i).getMa_cqthu());
                    tp.setTen_cqt(arr_tp_thieu.get(i).getTen_cqt());
                    tp.setMakb(arr_tp_thieu.get(i).getMakb());
                    arr_tp_thieu_ko_bk.add(tp);
                    //System.out.println("no file ftp on backup, tran_no --->>> " + arr_tp_thieu.get(i).getTran_no());
                }
                System.out.println("check data on ftp to files " + i + "/" + total);
            }
            System.out.println("get files from ftp success and start eport excel file");




        } catch (IOException e) {
            SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_GET_FTP_FILE, e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_GET_FTP_FILE, e.getMessage());
            }
        }

        //get excel
        Utility.createExcel2007OnlinePaym(arr_tp_pit, arr_tp_thieu_co_bk, arr_tp_thieu_ko_bk, excel_fld);

    }

    public static void findAndGetFTP(String file_copy, String fold_ftp) throws SocketException, IOException {
        System.out.println("Loading files ftp server ... please wait");
        FTPClient client = ftpConnection();
        String[] names = client.listNames();
        String[] tranNoToFind = new String[names.length];
        int r = 0;
        String[] getToTranNo = new String[2];
        //Load and get Tran_No
        for (String name : names) {
            getToTranNo = name.split("_");
            if (getToTranNo.length >= 2) {
                tranNoToFind[r] = getToTranNo[1];
            }
            r++;
        }
        String file_name = "";
        FileOutputStream fos = null;

        int indexof = 0;
        int total = file_copy.split(",").length;
        int i = 0;
        for (String curr_file : file_copy.split(",")) {


            indexof = ArrayUtils.indexOf(tranNoToFind, curr_file);
            if (indexof != -1) {
                file_name = names[indexof];
                fos = new FileOutputStream(fold_ftp + "/" + file_name);
                // Download file from FTP server
                client.retrieveFile(file_name, fos);

            }
            i++;
            System.out.println("check data on ftp to files " + i + "/" + total);
        }
    }

    /**
     * Thực hiện kết nối ftp
     * @return FTPClient
     */
    public static FTPClient ftpConnection() {
        FTPClient client = new FTPClient();
        try {
            client.connect(SupportTreasuryPaymApp.prop.getProperty("ftp.url"));
            client.login(SupportTreasuryPaymApp.prop.getProperty("ftp.user"), SupportTreasuryPaymApp.prop.getProperty("ftp.password"));
            client.changeWorkingDirectory(SupportTreasuryPaymApp.prop.getProperty("ftp.WorkingDirectory"));

        } catch (IOException e) {
            SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_CONNECTION_FTP, e.getMessage());
        }

        return client;

    }
}