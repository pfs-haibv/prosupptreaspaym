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
    //SHEET Data Payment
    public static final String SHEET_DATA_PAYMENT                              = "Payment";
    //Column Treasury Payment
    public static final String COLUMN_DATA_PAYMENT[] = {"FILE NAME", "Mã CQT", "Tên CQT","Mã KB", "Mã CQThu", "Mã gói", 
                                                        "Ngày CT", "Ngày KB", "Tổng CT KB", "Tổng CT về PIT" };  
    //Tiêu mục nhận CT về PIT
    public static int[] arr_tmuc = {1049,1012,1008,1007,1006,1005,1004,1003,1001,1014,4268};
    //Type excel
    public static final String TYPE_EXCEL_2003 = ".XLS";
    public static final String TYPE_EXCEL_2007 = ".XLSX";
    
}
