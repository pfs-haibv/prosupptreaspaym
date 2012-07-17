/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.excel;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.support.pit.datatype.MapCqt;
import com.support.pit.datatype.MapRole;
import com.support.pit.datatype.User;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Constants;
import com.support.pit.utility.EmailValidator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Administrator
 */
public class ListUserToExcel {

    public static ArrayList<MapRole> arr_role = new ArrayList<>();
    public static ArrayList<MapCqt> arrcqt = new ArrayList<>();
    public static ArrayList<User> arr_user = new ArrayList<>();

    /**
     * lấy thông tin cqt từ file excel
     * @return arrcqt
     */
    public static ArrayList<MapCqt> getMapCQT() {
        HSSFRow row = null;//Row   
        HSSFSheet sheet_name = null;//sheet_name

        try {
            FileInputStream fileInputStream = new FileInputStream("table/WD_AU_CS_Map_v003.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            /**-----------------------------------------------------------------*
             *                      SHEET MAP_CQT_ROLE                          *
             **-----------------------------------------------------------------*/
            sheet_name = workbook.getSheet("MAP_CQT_ROLE");
            int t_rows = sheet_name.getLastRowNum();

            for (int i = 1; i <= t_rows; i++) {
                row = sheet_name.getRow(i);
                //new cqt
                MapCqt cqt = new MapCqt();
                cqt.setMa_cqt(row.getCell(0).toString());
                cqt.setShort_name(row.getCell(2).toString());
                //add to array cqt
                arrcqt.add(cqt);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return arrcqt;
    }

    /**
     * Lấy thông tin role
     * @return arr_role
     */
    public static ArrayList<MapRole> getMapRole() {
        HSSFRow row = null;//Row   
        HSSFSheet sheet_name = null;//sheet_name

        try {
            FileInputStream fileInputStream = new FileInputStream("table/WD_AU_CS_Map_v003.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            /**-----------------------------------------------------------------*
             *                      SHEET MAP_CQT_ROLE                          *
             **-----------------------------------------------------------------*/
            sheet_name = workbook.getSheet("MAP_ROLE");
            int t_rows = sheet_name.getLastRowNum();

            for (int i = 1; i <= t_rows; i++) {
                row = sheet_name.getRow(i);
                //new role
                MapRole role = new MapRole();
                role.setName(row.getCell(0).toString());
                role.setRole(row.getCell(1).toString());
                //add to array cqt
                arr_role.add(role);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return arr_role;
    }

    /**
     * Lấy thông tin user
     * @param file
     * @return arr_user
     */
    public static ArrayList<User> getInfoUser(String dir_file, int max_role) {
        //Clear
        arr_user.clear();
        File file = new File(dir_file);

        HSSFRow row = null;//Row   
        HSSFSheet sheet_name = null;//sheet_name
        String temp = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            /**-----------------------------------------------------------------*
             *                      SHEET PhongBan-Vaitro-ChucNang              *
             **-----------------------------------------------------------------*/
            sheet_name = workbook.getSheet("DanhSachCanBo-ChucNangNghiepVu");
            int t_rows = sheet_name.getLastRowNum();
            for (int i = 7; i <= t_rows; i++) {
                String[] role = new String[max_role];

                int cell_role = 5;

                row = sheet_name.getRow(i);

                //thoát nếu ko có tên user
                if (row.getCell(1).toString().trim().isEmpty()) {
                    break;
                }
                //max row current
                int max_row_current = row.getLastCellNum() - cell_role;

                //new user
                User user = new User();
                //Check email
                boolean valid = EmailValidator.validate(row.getCell(2).toString());
                if (!valid) {
                    SupportTreasuryPaymView.writeLog("file: " + dir_file + ", lỗi email: " + row.getCell(2).toString());
                    break;
                }
                //set account
                user.setAccount(row.getCell(2).toString().substring(0, row.getCell(2).toString().indexOf("@")));
                //set name
                user.setName(row.getCell(1).toString());
                //set email
                user.setEmail(row.getCell(2).toString());
                //set phongban
                user.setPhongban(row.getCell(3).toString());
                //short_name
                user.setShort_name(file.getName().substring(0, file.getName().indexOf(".")));
                //mã cqt
                user.setCqt(findCQT(user.getShort_name()));
                //List role
                for (int r = 0; r < max_row_current; r++) {
                    temp = "";
                    temp = row.getCell(cell_role).toString();
                    if (temp != null) {
                        //tìm kiếm role theo tên
                        role[r] = findRole(user.getShort_name(), temp);
                    }
                    cell_role++;
                }
                user.setRole(role);
                //add to array cqt               
                arr_user.add(user);
                //clear row
                max_row_current = 0;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("file error:" + dir_file + ", message desc " + ex.getMessage());
        }

        return arr_user;
    }

    /**
     * Tìm kiếm role
     * @param name_role
     * @return role
     */
    public static String findRole(String short_name, String name_role) {
        String temp = "Z" + short_name + "_";
        String role = "";
        for (int i = 0; i < arr_role.size(); i++) {
            if (arr_role.get(i).getName().equals(name_role)) {
                role = temp + arr_role.get(i).getRole();
                break;
            }
        }

        return role;
    }

    /**
     * Lấy thông tin ma_cqt theo short_name
     * @param short_name
     * @return ma_cqt
     */
    public static String findCQT(String short_name) {
        String ma_cqt = "";
        for (int i = 0; i < arrcqt.size(); i++) {
            if (arrcqt.get(i).getShort_name().equals(short_name)) {
                ma_cqt = arrcqt.get(i).getMa_cqt();
                break;
            }
        }
        return ma_cqt;
    }

    /**
     * create excel 2007
     */
    public static void createExcel2007(ArrayList<User> arr_user, String part_file) {
        try {
            int rowCount = 0;
            Workbook workbook_xlsx = new XSSFWorkbook();
            /**
             * Create sheet User role
             */
            Sheet sheet = workbook_xlsx.createSheet(Constants.SHEET_USER_INFO);

            CellStyle cellStyle = workbook_xlsx.createCellStyle();
            //set next row
            CellStyle style = workbook_xlsx.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_user.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                //length account
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(arr_user.get(i).getAccount().length());
                //account
                cell = dataRow.createCell(1);
                cell.setCellValue(arr_user.get(i).getAccount());
                //name
                cell = dataRow.createCell(2);
                cell.setCellValue(arr_user.get(i).getName());
                //phongban
                cell = dataRow.createCell(3);
                cell.setCellValue(arr_user.get(i).getPhongban());
                //EN
                cell = dataRow.createCell(4);
                cell.setCellValue("EN");
                //Email
                cell = dataRow.createCell(5);
                cell.setCellValue(arr_user.get(i).getEmail());
                //RML
                cell = dataRow.createCell(6);
                cell.setCellValue("RML");
                //A
                cell = dataRow.createCell(7);
                cell.setCellValue("A");
                //123123
                cell = dataRow.createCell(8);
                cell.setCellValue("123123");
                //123123
                cell = dataRow.createCell(9);
                cell.setCellValue("123123");
                //mã cqt
                cell = dataRow.createCell(10);
                cell.setCellValue(arr_user.get(i).getCqt());
                //VI
                cell = dataRow.createCell(11);
                cell.setCellValue("VI");
                //1	
                cell = dataRow.createCell(12);
                cell.setCellValue("1");
                //0	
                cell = dataRow.createCell(13);
                cell.setCellValue("0");
                //805TC	
                cell = dataRow.createCell(14);
                cell.setCellValue("805TC");
                //F4METHOD	
                cell = dataRow.createCell(15);
                cell.setCellValue("F4METHOD");
                //ZPRINTPREVIEW	
                cell = dataRow.createCell(16);
                cell.setCellValue("ZPRINTPREVIEW");
                //FWS	
                cell = dataRow.createCell(17);
                cell.setCellValue("FWS");
                //PIT	
                cell = dataRow.createCell(18);
                cell.setCellValue("PIT");
                //NoActiveX	
                cell = dataRow.createCell(19);
                cell.setCellValue("NoActiveX");
                //X	
                cell = dataRow.createCell(20);
                cell.setCellValue("X");
                //VND
                cell = dataRow.createCell(21);
                cell.setCellValue("VND");
                //mã cqt
                cell = dataRow.createCell(22);
                cell.setCellValue(arr_user.get(i).getCqt());
            }
            /**
             * Create sheet User role
             */
            rowCount = 0;
            sheet = workbook_xlsx.createSheet(Constants.SHEET_USER_ROLE);

            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_user.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                //account
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(arr_user.get(i).getAccount());
                int num_role = 1;
                for (int r = 0; r < arr_user.get(i).getRole().length; r++) {
                    //role user
                    cell = dataRow.createCell(num_role);
                    cell.setCellValue(arr_user.get(i).getRole()[r]);
                    num_role++;
                }

            }
            //out file
            FileOutputStream outputStream = new FileOutputStream(part_file + Constants.TYPE_EXCEL_2007);
            //write excel
            workbook_xlsx.write(outputStream);
            //close
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create excel 2003
     */
    public static void createExcel2003(ArrayList<User> arr_user, String part_file) {
        try {
            int rowCount = 0;
            Workbook workbook_xls = new HSSFWorkbook();
            Sheet sheet = workbook_xls.createSheet(Constants.SHEET_USER_INFO);

            CellStyle cellStyle = workbook_xls.createCellStyle();
            //set next row
            CellStyle style = workbook_xls.createCellStyle();
            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_user.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                //length account
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(arr_user.get(i).getAccount().length());
                //account
                cell = dataRow.createCell(1);
                cell.setCellValue(arr_user.get(i).getAccount());
                //name
                cell = dataRow.createCell(2);
                cell.setCellValue(arr_user.get(i).getName());
                //phongban
                cell = dataRow.createCell(3);
                cell.setCellValue(arr_user.get(i).getPhongban());
                //EN
                cell = dataRow.createCell(4);
                cell.setCellValue("EN");
                //Email
                cell = dataRow.createCell(5);
                cell.setCellValue(arr_user.get(i).getEmail());
                //RML
                cell = dataRow.createCell(6);
                cell.setCellValue("RML");
                //A
                cell = dataRow.createCell(7);
                cell.setCellValue("A");
                //123123
                cell = dataRow.createCell(8);
                cell.setCellValue("123123");
                //123123
                cell = dataRow.createCell(9);
                cell.setCellValue("123123");
                //mã cqt
                cell = dataRow.createCell(10);
                cell.setCellValue(arr_user.get(i).getCqt());
                //VI
                cell = dataRow.createCell(11);
                cell.setCellValue("VI");
                //1	
                cell = dataRow.createCell(12);
                cell.setCellValue("1");
                //0	
                cell = dataRow.createCell(13);
                cell.setCellValue("0");
                //805TC	
                cell = dataRow.createCell(14);
                cell.setCellValue("805TC");
                //F4METHOD	
                cell = dataRow.createCell(15);
                cell.setCellValue("F4METHOD");
                //ZPRINTPREVIEW	
                cell = dataRow.createCell(16);
                cell.setCellValue("ZPRINTPREVIEW");
                //FWS	
                cell = dataRow.createCell(17);
                cell.setCellValue("FWS");
                //PIT	
                cell = dataRow.createCell(18);
                cell.setCellValue("PIT");
                //NoActiveX	
                cell = dataRow.createCell(19);
                cell.setCellValue("NoActiveX");
                //X	
                cell = dataRow.createCell(20);
                cell.setCellValue("X");
                //VND
                cell = dataRow.createCell(21);
                cell.setCellValue("VND");
                //mã cqt
                cell = dataRow.createCell(22);
                cell.setCellValue(arr_user.get(i).getCqt());
            }
            /**
             * Create sheet User role
             */
            rowCount = 0;
            sheet = workbook_xls.createSheet(Constants.SHEET_USER_ROLE);

            style.cloneStyleFrom(cellStyle);
            for (int i = 0; i < arr_user.size(); i++) {
                Row dataRow = sheet.createRow(rowCount++);

                //account
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(arr_user.get(i).getAccount());
                int num_role = 1;
                for (int r = 0; r < arr_user.get(i).getRole().length; r++) {
                    //role user
                    cell = dataRow.createCell(num_role);
                    cell.setCellValue(arr_user.get(i).getRole()[r]);
                    num_role++;
                }

            }
            //out file
            FileOutputStream outputStream = new FileOutputStream(part_file + Constants.TYPE_EXCEL_2003);
            //write excel file
            workbook_xls.write(outputStream);
            //close
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scand folder lấy file excel cqt gửi lên
     * đọc dữ liệu trong file lấy thông tin: name, email, phongban, role
     * export sheet INFO_USER, USER_ROLE
     * @param type_excel
     * @param max_role
     * @param dirScandFold
     * @param dirExpFile 
     */
    public static void getInfoRole(String type_excel, int max_role, String dirScandFold, String dirExpFile) {
        String file_active = "";
        try {

            //get map role
            arr_role = getMapRole();
            //get map cqt
            arrcqt = getMapCQT();
            //Scand list folder
            File dirForder = new File(dirScandFold);
            File dirFile, export;
            for (File f : dirForder.listFiles()) {
                //Scand file on folder
                dirFile = new File(f.getPath());
                for (File l : dirFile.listFiles()) {
                    //Đọc file có type .xls               
                    if (l.getName().substring(l.getName().indexOf("."), l.getName().length()).equals(".xls")) {
                        //get info user
                        file_active = dirFile + "/" + l.getName();

                        getInfoUser(file_active, max_role);

                        switch (type_excel) {

                            case Constants.TYPE_EXCEL_2007:
                                //Create folder
                                export = new File(dirExpFile + "/" + f.getName());
                                if (!export.exists()) {
                                    export.mkdir();
                                }
                                createExcel2007(arr_user, export + "/" + l.getName().substring(0, l.getName().indexOf(".")));
                                break;

                            case Constants.TYPE_EXCEL_2003:
                                //Create folder
                                export = new File(dirExpFile + "/" + f.getName());
                                if (!export.exists()) {
                                    export.mkdir();
                                }
                                createExcel2003(arr_user, export + "/" + l.getName().substring(0, l.getName().indexOf(".")));
                                break;

                            default:
                                break;
                        }
                    }
                }
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
