/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.utility;

import com.support.pit.config.LoadMapDMuc;
import com.support.pit.datatype.MapCqt;
import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Constants;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
public class Utility {

    public static String array_timuc = "1049, 1013, 1012, 1011, 1008, 1007, 1006, 1005, 1004, 1003, 1001, 1014";

    /**
     * 
     * @param file_ 
     */
    public static void readTXT(String file_) {
        File file = new File(file_);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            // dis.available() returns 0 if the file does not have more lines.
            while (dis.available() != 0) {

                // this statement reads the line from the file and print it to
                // the console.
                System.out.println(dis.readLine());
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create file config oracle
     * @param file_ora
     * @return get connection
     * @throws IOException 
     */
    public static String getConfigORA(String file_ora) throws IOException {

        String getConnORA = "";
        File file = new File(file_ora);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String line = null;
        try {

            //Create file 
            if (!file.exists()) {
                file.createNewFile();
                //write use buffering
                Writer output = new BufferedWriter(new FileWriter(file));
                output.write("jdbc:oracle:thin:@10.64.85.52:1527/PE1,SAP_READ,123456");
                output.close();
            }

            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            // dis.available() returns 0 if the file does not have more lines.
            while ((line = dis.readLine()) != null) {
                getConnORA = line;
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getConnORA;
    }

    /**
     * Create data TK
     * @param source_file
     * @throws IOException 
     */
    public void createDataTK(String source_file) throws IOException {
        //StringBuffer chứa thông tin mẫu tk
        String line = null;
        StringBuffer mautk1_1 = new StringBuffer();
        StringBuffer mautk1_2 = new StringBuffer();
        StringBuffer mautk1_3 = new StringBuffer();
        //Load dữ liệu các mẫu tờ khai
        FileInputStream fis_mautk1_1 = new FileInputStream(new File("C:/Users/Administrator/Desktop/mautk/mau1/append.txt"));
        FileInputStream fis_mautk1_2 = new FileInputStream(new File("C:/Users/Administrator/Desktop/mautk/mau1/append_2.txt"));
        FileInputStream fis_mautk1_3 = new FileInputStream(new File("C:/Users/Administrator/Desktop/mautk/mau1/append_3.txt"));
        //Lấy dữ liệu các mẫu tờ khai trong file
        BufferedInputStream bis_mautk1_1 = new BufferedInputStream(fis_mautk1_1);
        DataInputStream dis_mautk1_1 = new DataInputStream(bis_mautk1_1);

        BufferedInputStream bis_mautk1_2 = new BufferedInputStream(fis_mautk1_2);
        DataInputStream dis_mautk1_2 = new DataInputStream(bis_mautk1_2);

        BufferedInputStream bis_mautk1_3 = new BufferedInputStream(fis_mautk1_3);
        DataInputStream dis_mautk1_3 = new DataInputStream(bis_mautk1_3);


        // dis.available() returns 0 if the file does not have more lines.
        // Lấy thông tin mẫu tờ khai

        while ((line = dis_mautk1_1.readLine()) != null) {

            mautk1_1.append(line.replaceAll("ï»¿", ""));
        }
        // Lấy thông tin mẫu tờ khai
        while ((line = dis_mautk1_2.readLine()) != null) {
            mautk1_2.append(line.replaceAll("ï»¿", ""));
        }
        // Lấy thông tin mẫu tờ khai
        while ((line = dis_mautk1_3.readLine()) != null) {
            mautk1_3.append(line.replaceAll("ï»¿", ""));
        }
        // dispose all the resources after using them.
        fis_mautk1_1.close();
        fis_mautk1_1.close();
        fis_mautk1_1.close();
        bis_mautk1_1.close();
        bis_mautk1_1.close();
        bis_mautk1_1.close();
        dis_mautk1_3.close();
        dis_mautk1_3.close();
        dis_mautk1_3.close();

        try {
            File file = new File(source_file);
            //Create file 
            if (!file.exists()) {
                file.createNewFile();
                //write use buffering               
                Writer output = new BufferedWriter(new FileWriter(file));
                output.flush();
                output.write(mautk1_1.toString().trim());
                //append MST
                output.write("1111111111");
                output.write(mautk1_2.toString().trim());
                //append CQT
                output.write("0101");
                output.write(mautk1_3.toString().trim());
                output.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * get info cqt từ danh mục
     * @param ma_kb
     * @param ma_cqthu
     * @param lcn_owner
     * @return 
     */
    public static MapCqt getInfoDMuc(String ma_kb, String ma_cqthu, String lcn_owner) {

        MapCqt cqt = new MapCqt();
        for (MapCqt cqt_ : LoadMapDMuc.arr_cqt) {            
            if (cqt_.getMa_kb().equals(ma_kb) || cqt_.getMa_cqthu().equals(ma_cqthu) || cqt_.getLcn_owner().equals(lcn_owner)) {
                cqt = cqt_;
                break;
            }
        }
        return cqt;        
    }

    /**
     * move file from folder to folder
     * @param source
     * @param targer 
     */
    public static void moveFiles(String source, String targer) {
        // File (or directory) to be moved
        File file = new File(source);
        // Destination directory
        File dir = new File(targer);
        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        if (!success) {
            // File was not successfully moved                
        }
    }

    /**
     * Create file excel 2007 for payment treasury
     * @param arr_tp
     * @param part_file 
     */
    public static void createExcel2007(ArrayList<TreasuryPayment> arr_tp, String part_file) {
        try {
            int rowCount = 0;
            Workbook workbook_xlsx = new XSSFWorkbook();
            Sheet sheet = workbook_xlsx.createSheet(Constants.SHEET_DATA_PAYMENT);
            Row row = sheet.createRow(rowCount++);

            CellStyle cellStyle = workbook_xlsx.createCellStyle();
            Font font = workbook_xlsx.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            //set first row
            cellStyle.setFont(font);
            for (int i = 0; i < Constants.COLUMN_DATA_PAYMENT.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(Constants.COLUMN_DATA_PAYMENT[i]);
                cell.setCellStyle(cellStyle);
            }

            //set next row
            CellStyle style = workbook_xlsx.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_tp.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                Cell cell = dataRow.createCell(0);
                //cell.setCellStyle(style);
                cell.setCellValue(arr_tp.get(i).getFilename());

                cell = dataRow.createCell(1);
                cell.setCellValue(arr_tp.get(i).getCqt());

                cell = dataRow.createCell(2);
                cell.setCellValue(arr_tp.get(i).getTen_cqt());

                cell = dataRow.createCell(3);
                cell.setCellValue(arr_tp.get(i).getMakb());

                cell = dataRow.createCell(4);
                cell.setCellValue(arr_tp.get(i).getMa_cqthu());

                cell = dataRow.createCell(5);
                cell.setCellValue(arr_tp.get(i).getTran_no());

                cell = dataRow.createCell(6);
                cell.setCellValue(arr_tp.get(i).getNgay_ct());

                cell = dataRow.createCell(7);
                cell.setCellValue(arr_tp.get(i).getNgay_kb());

                cell = dataRow.createCell(8);
                cell.setCellValue(arr_tp.get(i).getTotal_ct_khobac());

                cell = dataRow.createCell(9);
                cell.setCellValue(arr_tp.get(i).getTotal_ct_pit());
            }
            //out file
            FileOutputStream outputStream = new FileOutputStream(part_file + Constants.TYPE_EXCEL_2007);
            //write excel
            workbook_xlsx.write(outputStream);
            // workbook_xlsx.write(outputStream);
            //close
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create excel 2007 for payment onlinet (check payment treasury)
     * @param arr_tp
     * @param arr_tp_thieu_bk
     * @param arr_tp_thieu_ko_bk
     * @param part_file 
     */
    public static void createExcel2007OnlinePaym(ArrayList<TreasuryPayment> arr_tp, ArrayList<TreasuryPayment> arr_tp_thieu_bk, ArrayList<TreasuryPayment> arr_tp_thieu_ko_bk, String part_file) {
        try {
            System.out.println("size data: arr_tp_pit " + arr_tp.size() + " arr_tp_thieu_bk " + arr_tp_thieu_bk.size() + " arr_tp_thieu_ko_bk " + arr_tp_thieu_ko_bk.size());
            System.out.println("start export sheet 1.");
            int rowCount = 0;
            Workbook workbook_xlsx = new XSSFWorkbook();
            //Create sheet Ctừ về pit
            Sheet sheet = workbook_xlsx.createSheet(Constants.SHEET_CT_VE_PIT);
            Row row = sheet.createRow(rowCount++);

            CellStyle cellStyle = workbook_xlsx.createCellStyle();
            Font font = workbook_xlsx.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            //set first row
            cellStyle.setFont(font);
            for (int i = 0; i < Constants.COLUMN_DATA_PAYMENT_ONLINE_PIT.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(Constants.COLUMN_DATA_PAYMENT_ONLINE_PIT[i]);
                cell.setCellStyle(cellStyle);
            }

            //set next row
            CellStyle style = workbook_xlsx.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (TreasuryPayment tp : arr_tp) {
                Row dataRow = sheet.createRow(rowCount++);
                Cell cell = dataRow.createCell(0);
                //cell.setCellStyle(style);
//                cell.setCellValue(arr_tp.get(i).getFilename());

//                cell = dataRow.createCell(0);
                cell.setCellValue(tp.getCqt());

                cell = dataRow.createCell(1);
                cell.setCellValue(tp.getTen_cqt());

                cell = dataRow.createCell(2);
                cell.setCellValue(tp.getMakb());

                cell = dataRow.createCell(3);
                cell.setCellValue(tp.getMa_cqthu());

                cell = dataRow.createCell(4);
                cell.setCellValue(tp.getTran_no());

                cell = dataRow.createCell(5);
                cell.setCellValue(tp.getNgay_kb());

                cell = dataRow.createCell(6);
                cell.setCellValue(tp.getNgay_kb());

                cell = dataRow.createCell(7);
                cell.setCellValue(tp.getTotal_ct_pit());

                cell = dataRow.createCell(8);
                cell.setCellValue(tp.getTax_mount());

                cell = dataRow.createCell(9);
                cell.setCellValue(tp.getNgay_ct());
            }

            //Create sheet Ctừ thiếu tồn tại trong Backup
            System.out.println("start export sheet 2.");
            rowCount = 0;
            sheet = workbook_xlsx.createSheet(Constants.SHEET_CT_THIEU_CO_BK);
            row = sheet.createRow(rowCount++);
            cellStyle.setFont(font);
            for (int i = 0; i < Constants.COLUMN_DATA_PAYMENT_ONLINE.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(Constants.COLUMN_DATA_PAYMENT_ONLINE[i]);
                cell.setCellStyle(cellStyle);
            }

            //set next row
            style = workbook_xlsx.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (TreasuryPayment tp : arr_tp_thieu_bk) {

                Row dataRow = sheet.createRow(rowCount++);

                Cell cell = dataRow.createCell(0);
                //cell.setCellStyle(style);
                cell.setCellValue(tp.getFilename());

                cell = dataRow.createCell(1);
                cell.setCellValue(tp.getCqt());

                cell = dataRow.createCell(2);
                cell.setCellValue(tp.getTen_cqt());

                cell = dataRow.createCell(3);
                cell.setCellValue(tp.getMakb());

                cell = dataRow.createCell(4);
                cell.setCellValue(tp.getMa_cqthu());

                cell = dataRow.createCell(5);
                cell.setCellValue(tp.getTran_no());

                cell = dataRow.createCell(6);
                cell.setCellValue(tp.getNgay_ct());

                cell = dataRow.createCell(8);
                cell.setCellValue(tp.getNgay_kb());

            }
            System.out.println("start export sheet 3.");
            //Create sheet Ctừ thiếu không tồn tại trong Backup            
            rowCount = 0;
            sheet = workbook_xlsx.createSheet(Constants.SHEET_CT_THIEU_KO_BK);
            row = sheet.createRow(rowCount++);
            cellStyle.setFont(font);
            for (int i = 0; i < Constants.COLUMN_DATA_PAYMENT_ONLINE.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(Constants.COLUMN_DATA_PAYMENT_ONLINE[i]);
                cell.setCellStyle(cellStyle);
            }

            //set next row
            style = workbook_xlsx.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (TreasuryPayment tp : arr_tp_thieu_ko_bk) {
                Row dataRow = sheet.createRow(rowCount++);

                Cell cell = dataRow.createCell(0);
                //cell.setCellStyle(style);
                cell.setCellValue(tp.getLcn_owner());

                cell = dataRow.createCell(1);
                cell.setCellValue(tp.getCqt());

                cell = dataRow.createCell(2);
                cell.setCellValue(tp.getTen_cqt());

                cell = dataRow.createCell(3);
                cell.setCellValue(tp.getMakb());

                cell = dataRow.createCell(4);
                cell.setCellValue(tp.getMa_cqthu());

                cell = dataRow.createCell(5);
                cell.setCellValue(tp.getTran_no());

                cell = dataRow.createCell(6);
                cell.setCellValue(tp.getNgay_ct());

                cell = dataRow.createCell(8);
                cell.setCellValue(tp.getNgay_kb());
            }

            //out file
            FileOutputStream outputStream = new FileOutputStream(part_file + Constants.TYPE_EXCEL_2007);
            //write excel
            workbook_xlsx.write(outputStream);
            //close
            outputStream.close();
            System.out.println("size data: arr_tp_pit " + arr_tp.size() + " arr_tp_thieu_bk " + arr_tp_thieu_bk.size() + " arr_tp_thieu_ko_bk " + arr_tp_thieu_ko_bk.size());
        } catch (IOException e) {
            SupportTreasuryPaymView.logger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Create excel 2003 for treasury payment
     * @param arr_tp
     * @param part_file 
     */
    public static void createExcel2003(ArrayList<TreasuryPayment> arr_tp, String part_file) {
        try {
            int rowCount = 0;
            Workbook workbook_xls = new HSSFWorkbook();
            Sheet sheet = workbook_xls.createSheet(Constants.SHEET_DATA_PAYMENT);
            Row row = sheet.createRow(rowCount++);

            CellStyle cellStyle = workbook_xls.createCellStyle();
            Font font = workbook_xls.createFont();
            //set first row
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            for (int i = 0; i < Constants.COLUMN_DATA_PAYMENT.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(Constants.COLUMN_DATA_PAYMENT[i]);
                cell.setCellStyle(cellStyle);
            }
            //Set next row
            CellStyle style = workbook_xls.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_tp.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                Cell cell = dataRow.createCell(0);
                //cell.setCellStyle(style);
                cell.setCellValue(arr_tp.get(i).getFilename());

                cell = dataRow.createCell(1);
                cell.setCellValue(arr_tp.get(i).getCqt());

                cell = dataRow.createCell(2);
                cell.setCellValue(arr_tp.get(i).getTen_cqt());

                cell = dataRow.createCell(3);
                cell.setCellValue(arr_tp.get(i).getMakb());

                cell = dataRow.createCell(4);
                cell.setCellValue(arr_tp.get(i).getMa_cqthu());

                cell = dataRow.createCell(5);
                cell.setCellValue(arr_tp.get(i).getTran_no());

                cell = dataRow.createCell(6);
                cell.setCellValue(arr_tp.get(i).getNgay_ct());

                cell = dataRow.createCell(7);
                cell.setCellValue(arr_tp.get(i).getNgay_kb());

                cell = dataRow.createCell(8);
                cell.setCellValue(arr_tp.get(i).getTotal_ct_khobac());

                cell = dataRow.createCell(9);
                cell.setCellValue(arr_tp.get(i).getTotal_ct_pit());
            }
            //out file
            FileOutputStream outputStream = new FileOutputStream(part_file + Constants.TYPE_EXCEL_2003);
            //write excel file
            workbook_xls.write(outputStream);
            //close
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy file từ thư mục đến thư mục
     * @param srcPath
     * @param dstPath
     * @throws IOException 
     */
    public static void copyDirectory(File srcPath, File dstPath) throws IOException {
        // bỏ trường hợp srcPath = dstPath
        if (!srcPath.equals(dstPath)) {
            if (srcPath.isDirectory()) {
                if (!dstPath.exists()) {
                    dstPath.mkdir();
                }

                String files[] = srcPath.list();
                for (int i = 0; i < files.length; i++) {
                    copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
                }
            } else {
                if (!srcPath.exists()) {
                    System.out.println("File or directory does not exist.");
                    System.exit(0);
                } else {
                    InputStream in = new FileInputStream(srcPath);
                    OutputStream out = new FileOutputStream(dstPath);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }

    /**
     * Tìm kiếm và copy file trong thư mục
     * @param srcPath
     * @param dstPath
     * @param find_file
     * @throws IOException 
     */
    public static void copyDirectory(File srcPath, File dstPath, String find_file) throws IOException {
        String[] file_copy = find_file.split(",");
        for (int f = 0; f < file_copy.length; f++) {
            // bỏ trường hợp srcPath = dstPath
            if (!srcPath.equals(dstPath)) {
                if (srcPath.isDirectory()) {
                    if (!dstPath.exists()) {
                        dstPath.mkdir();
                    }

                    String files[] = srcPath.list();
                    for (int i = 0; i < files.length; i++) {
                        //tìm file và thực hiện copy
                        if (files[i].equals(file_copy[f].trim())) {
                            copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
                        }
                    }
                } else {
                    if (!srcPath.exists()) {
                        System.out.println("File or directory does not exist.");
                        System.exit(0);
                    } else {
                        InputStream in = new FileInputStream(srcPath);
                        OutputStream out = new FileOutputStream(dstPath);

                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    }
                }
            }
        }
    }

    /**
     * Lấy ngày cuối cùng của tháng
     * @param datetime
     * @return max_date
     */
    public static String getMaxDate(String datetime) {

        String max_date = "";

        String month_year[] = datetime.split("-");
        //Year
        int year = Integer.parseInt(month_year[1]);
        //Month (January = 0 ... )
        int month = Integer.parseInt(month_year[0]) - 1;
        //date        
        int date = 01;

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, date);

        int maxDay = calendar.getActualMaximum(calendar.DATE);

        max_date = maxDay + "-" + datetime;

        return max_date;

    }

    /**
     * Lấy ngày cuối cùng của tháng
     * @param datetime
     * @return max_date
     */
    public static String getMaxDateOnlinePaym(String datetime) {

        String max_date = "";

        String month_year[] = datetime.split("/");
        //Year
        int year = Integer.parseInt(month_year[1]);
        //Month (January = 0 ... )
        int month = Integer.parseInt(month_year[0]) - 1;
        //date        
        int date = 01;

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, date);

        int maxDay = calendar.getActualMaximum(calendar.DATE);

        max_date = maxDay + "/" + datetime;

        return max_date;
    }
}
