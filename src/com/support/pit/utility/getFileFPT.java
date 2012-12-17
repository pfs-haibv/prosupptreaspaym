/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.utility;

import com.support.pit.datatype.TreasuryPayment;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.ArrayUtils;

public class getFileFPT {

    public static void getFTP(ArrayList<TreasuryPayment> arr_tp_pit, ArrayList<TreasuryPayment> arr_tp_thieu, String fold_ftp, String excel_fld) {
        System.out.println("Loading files ftp server ...please wait");
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
            for(String name: names){
                getToTranNo = name.split("_");
                if(getToTranNo.length >= 2){
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
                    tp1.setFilename(file_name);
                    arr_tp_thieu_co_bk.add(tp1);

                } 
                //add chứng từ ko tồn tại trong backup
                else {
                    TreasuryPayment tp = new TreasuryPayment();
                    tp.setLcn_owner(arr_tp_thieu.get(i).getLcn_owner());
                    tp.setNgay_kb(arr_tp_thieu.get(i).getNgay_kb());
                    tp.setTran_no(arr_tp_thieu.get(i).getTran_no());
                    arr_tp_thieu_ko_bk.add(tp);
                    //System.out.println("no file ftp on backup, tran_no --->>> " + arr_tp_thieu.get(i).getTran_no());
                }
                System.out.println("check data on ftp to files "+i+"/"+total);
            }
            System.out.println("get files from ftp success and start eport excel file");




        } catch (IOException e_) {
            e_.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get excel
        Utility.createExcel2007OnlinePaym(arr_tp_pit, arr_tp_thieu_co_bk, arr_tp_thieu_ko_bk, excel_fld);

    }

    public static void findAndGetFTP(String file_copy, String fold_ftp) throws SocketException, IOException {
        System.out.println("Loading files ftp server ...please wait");
        FTPClient client = new FTPClient();

        client.connect("10.64.85.28");
        client.login("px1adm", "Gdt$2012");
        client.changeWorkingDirectory("/DataPaym/Archive");
        String[] names = client.listNames();
        String[] tranNoToFind = new String[names.length];
            int r = 0;
            String[] getToTranNo = new String[2];
            //Load and get Tran_No
            for(String name: names){
                getToTranNo = name.split("_");
                if(getToTranNo.length >= 2){
                    tranNoToFind[r] = getToTranNo[1];
                }                
                r++;                
            }
        String file_name = "";
        FileOutputStream fos = null;

        int indexof = 0;
        int total = file_copy.split(",").length;
        int i =0;
        for (String curr_file : file_copy.split(",")) {
            
           
            indexof = ArrayUtils.indexOf(tranNoToFind, curr_file);
            if (indexof != -1) {
                file_name = names[indexof];
                fos = new FileOutputStream(fold_ftp + "/" + file_name);
                // Download file from FTP server
                client.retrieveFile(file_name, fos);

            }
            i++;
            System.out.println("check data on ftp to files "+i+"/"+total);
        }
    }

    public static void main(String[] args) throws SocketException, IOException {

        DateFormat df = new SimpleDateFormat("MMM/yyyy");
        Date date = new Date();
        System.out.println("01/" + df.format(date));
       // txtCTDenNgay.setText(Utility.getMaxDateOnlinePaym(df.format(date)));

    }
}