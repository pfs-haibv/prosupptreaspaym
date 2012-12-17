/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.support.pit.system;

/**
 *
 * @author NTS
 */
public class Constants {

    //Scand file xml
    public static final String SRC_FILE_XML                                     = "D:\\Projects\\TreasuryPaym\\Data\\";
    //Result files text after check treasury payment
    public static final String RESULT_CHK_FILE                                  = "D:\\Projects\\TreasuryPaym\\Data\\XML_MATINH_05_2011.TXT";
    //Move file by file names
    public static final String MOVE_FILES_NAME                                  = "D:\\Projects\\chungtu\\XML_HAN_032011\\";
    public static final String TARGET_FILES_NAME                                = "D:\\Projects\\chungtu\\XML_HAN_032011\\FILES_LOAI\\";    
    
    //Part tab INFO_PAYMENT
    public static final String PART_INFO_PAYM_SRC_FORDER                       = "PART_INFO_PAYM_SRC_FORDER";
    public static final String PART_INFO_PAYM_EXP_FILE                         = "PART_INFO_PAYM_EXP_FILE";
    public static final String PART_INFO_PAYM_ERR_FORDER                       = "PART_INFO_PAYM_ERR_FORDER";
    //Part tab MODIFY_PAYMENT
    public static final String PART_MODIFY_FILES                               = "PART_MODIFY_FILES";
    public static final String PART_MODIFY_SRC_FOLDER                          = "PART_MODIFY_SRC_FOLDER";
    public static final String PART_MODIFY_INFO_NNT                            = "PART_MODIFY_INFO_NNT";
    //Part tab CREATE_FORM
    public static final String PART_CREATE_FORM_CQT                            = "PART_CREATE_FORM_CQT";
    public static final String PART_CREATE_FORM_MST                            = "PART_CREATE_FORM_MST";
    //Part tab Tiện ích
    public static final String PART_TIEN_ICH_SRC_FLD                           = "PART_TIEN_ICH_SRC_FLD";
    public static final String PART_TIEN_ICH_COPY_TO_FLD                       = "PART_TIEN_ICH_COPY_TO_FLD";
    //Part tab Create List User
    public static final String PART_CREATE_LIST_USER_SCAND_FLD                 = "PART_CREATE_LIST_USER_SCAND_FLD";
    public static final String PART_CREATE_LIST_USER_EXP_FILE                  = "PART_CREATE_LIST_USER_EXP_FILE";
    //Part tab Create List User
    public static final String PART_ONLINE_PAYMENT_GET_FPT                     = "PART_ONLINE_PAYMENT_GET_FPT";
    public static final String PART_ONLINE_PAYMENT_EXPORT_FILE                 = "PART_ONLINE_PAYMENT_EXPORT_FILE";
    //SHEET Data Payment
    public static final String SHEET_DATA_PAYMENT                              = "Payment";
    public static final String SHEET_USER_INFO                                 = "USER_INFO";
    public static final String SHEET_USER_ROLE                                 = "USER_ROLE";
    //SHEET Online Payment
    public static final String SHEET_CT_VE_PIT                                 = "Ctừ về PIT";
    public static final String SHEET_CT_THIEU_CO_BK                            = "CTừ thiếu có trong BK";
    public static final String SHEET_CT_THIEU_KO_BK                            = "CTừ thiếu ko có trong BK";
    //Column Treasury Payment
    public static final String COLUMN_DATA_PAYMENT[] = {"FILE NAME", "Mã CQT", "Tên CQT","Mã KB", "Mã CQThu", "Mã gói", 
                                                        "Ngày CT", "Ngày KB", "Tổng CT KB", "Tổng CT về PIT" };  
    //Column Treasury Payment Online
    public static final String COLUMN_DATA_PAYMENT_ONLINE[] = {"FILE NAME", "Mã CQT", "Tên CQT","Mã KB", "Mã CQThu", "Mã gói", 
                                                        "Ngày CT", "Ngày KB", "Create Date"}; 
    //Tiêu mục nhận CT về PIT
    public static int[] arr_tmuc = {1049,1012,1008,1007,1006,1005,1004,1003,1001,1014,4268};
    //Type excel
    public static final String TYPE_EXCEL_2003 = ".XLS";
    public static final String TYPE_EXCEL_2007 = ".XLSX";
    //Multi excel
    public static final String TYPE_MULTI_EXCEL = "EXCELS";
    public static final String TYPE_MULTI_SHEET = "EXCEL";
    
    
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
}
