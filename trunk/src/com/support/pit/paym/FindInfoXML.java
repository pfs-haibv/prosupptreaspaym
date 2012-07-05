package com.support.pit.paym;

import com.support.pit.datatype.TreasuryPayment;
import com.support.pit.serializable.SerializableDemo;
import com.support.pit.system.Constants;
import com.support.pit.utility.Utility;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.util.ArrayList;

public class FindInfoXML {

    //Tổng chứng từ kho bạc
    private static int total_ct_khobac = 0;
    //Tổng chứng từ về PIT
    private static int total_ct_pit = 0;
    // Tiểu mục
    private static int tmuc = 0;
    // Mã kho bạc
    private static String ma_kbac = "";
    // Mã cơ quan thu
    private static String ma_cqthu = "";
    // String ngày kho bạc
    private static String ngay_kb = "";
    // String ngày chứng từ
    private static String ngay_ct = "";
    // Tên file
    private static String file_name = "";
    //Tran_no
    private static String trans_no = "";
    //map mã cqt
    private static String map_cqt[] = new String[2];

    public static void setFile_name(String file_name) {
        FindInfoXML.file_name = file_name;
    }

    public static String getFile_name() {
        return file_name;
    }
    //Lưu thông tin từng file xml   
    public static ArrayList<TreasuryPayment> arr_tp = new ArrayList<TreasuryPayment>();

    /**
     * Thông tin chứng từ
     * @param source_file
     * @param log_file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public static void readXML(String source_file, String log_file, String type_excel) throws ParserConfigurationException, SAXException, IOException {

        //Clear array
        arr_tp.clear();
        try {

            // Forder file scand
            File actual = new File(source_file);

            for (File f : actual.listFiles()) {
                //clear total ct
                total_ct_khobac = 0;
                total_ct_pit = 0;
                // Lay file XML
                int endIndex = f.getName().length();
                int beginIndex = endIndex - 3;
                // Scan file có định dạng .XML
                if (f.getName().substring(beginIndex, endIndex).equals("xml")) {

                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    // normalize text representation
                    // File name
                    file_name = f.getName();
                    setFile_name(file_name);
                    Document doc = docBuilder.parse(new File(source_file + "\\" + f.getName()));
                    doc.getDocumentElement().normalize();

                    // Header payment
                    NodeList headerPaym = doc.getElementsByTagName("Header");
                    for (int s = 0; s < headerPaym.getLength(); s++) {
                        Node HeaderNode = headerPaym.item(0);

                        if (HeaderNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element firstCQTElement = (Element) HeaderNode;
                            // Tran_no - Gói chứng từ
                            NodeList tranList = firstCQTElement.getElementsByTagName("Msg_RefID");
                            Element tranElement = (Element) tranList.item(0);
                            NodeList textTranList = tranElement.getChildNodes();
                            trans_no = ((Node) textTranList.item(0)).getNodeValue();
                        }

                    }
                    // Detail payment
                    NodeList listOfPersons = doc.getElementsByTagName("Transaction");

                    for (int s = 0; s < listOfPersons.getLength(); s++) {                        
                        total_ct_khobac++;                        
                        Node chungTuNode = listOfPersons.item(s);
                        //Check ELEMENT NODE
                        if (chungTuNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element firstCQTElement = (Element) chungTuNode;
                            // Kho bạc
                            NodeList cqThueList = firstCQTElement.getElementsByTagName("ma_kbac");
                            Element cqThueElement = (Element) cqThueList.item(0);
                            NodeList textCQThueList = cqThueElement.getChildNodes();
                            // ma_cqthu
                            NodeList cqthuList = firstCQTElement.getElementsByTagName("ma_cqthu");
                            Element cqthuElement = (Element) cqthuList.item(0);
                            NodeList textCQThuList = cqthuElement.getChildNodes();
                            // Ngày kho bạc 
                            NodeList ngay_kb_List = firstCQTElement.getElementsByTagName("ngay_kb");
                            Element ngay_kb_Element = (Element) ngay_kb_List.item(0);
                            NodeList textngay_kb_List = ngay_kb_Element.getChildNodes();
                            // Ngày chứng từ
                            NodeList ngay_kbac_List = firstCQTElement.getElementsByTagName("ngay_kbac");
                            Element ngay_kbac_Element = (Element) ngay_kbac_List.item(0);
                            NodeList textngay_kbac_List = ngay_kbac_Element.getChildNodes();
                            ma_cqthu = "";
                            if (textCQThuList.getLength() != 0) {
                                ma_cqthu = ((Node) textCQThuList.item(0)).getNodeValue();
                            }
                            // Mã kho bạc
                            ma_kbac = ((Node) textCQThueList.item(0)).getNodeValue();
                            // Ngày kho bạc
                            String year = ((Node) textngay_kb_List.item(0)).getNodeValue().substring(0, 4);
                            String month = ((Node) textngay_kb_List.item(0)).getNodeValue().substring(4, 6);
                            String date = ((Node) textngay_kb_List.item(0)).getNodeValue().substring(6, 8);
                            ngay_kb = date + "/" + month + "/" + year;
                            // Ngày chứng từ
                            ngay_ct = ((Node) textngay_kbac_List.item(0)).getNodeValue();
                            // Tiểu mục
                            NodeList chungTuList = firstCQTElement.getElementsByTagName("ma_tmuc");
                            Element chungTuElement = (Element) chungTuList.item(0);
                            NodeList textAgeList = chungTuElement.getChildNodes();

                            // Loại bỏ tiểu mục values null
                            if (textAgeList.getLength() != 0) {
                                tmuc = Integer.parseInt(((Node) textAgeList.item(0)).getNodeValue());
                            }
                            // Load TMuc và lấy tiểu mục theo yêu cầu
                            for (int i = 0; i < Constants.arr_tmuc.length; i++) {         
                                if (tmuc == Constants.arr_tmuc[i]) {
                                    total_ct_pit++;
                                }

                            } // End load TMuc

                        } // End check element node                    

                    } // End Transaction_No

                    //Set thông tin từng file dữ liệu và lưu vào mảng
                    TreasuryPayment tp = new TreasuryPayment();
                    //set cqt
                    //2 cách lấy tên đều ko lỗi font
                    map_cqt = Utility.getMapCQT(ma_kbac, ma_cqthu);
                    //map_cqt = SerializableDemo.getDataMapCQT(ma_kbac);
                    tp.setCqt(map_cqt[0]);
                    //set tên cqt
                    tp.setTen_cqt(map_cqt[1]);
                    //file name
                    tp.setFilename(file_name);
                    //mã cqthu
                    tp.setMa_cqthu(ma_cqthu);
                    //mã kho bạc
                    tp.setMakb(ma_kbac);
                    //ngày chứng từ
                    tp.setNgay_ct(ngay_ct);
                    //ngày kho bạc
                    tp.setNgay_kb(ngay_kb);
                    //mã gói tin
                    tp.setTran_no(trans_no);
                    //tổng số chứng từ kho bạc
                    tp.setTotal_ct_khobac(total_ct_khobac);
                    //tổng số chứng từ về pit
                    tp.setTotal_ct_pit(total_ct_pit);

                    //add to array
                    arr_tp.add(tp);
                }  // End scan file
            }
            //Write to excel
            switch (type_excel) {
                case Constants.TYPE_EXCEL_2007:
                    Utility.createExcel2007(arr_tp, log_file);
                    break;

                case Constants.TYPE_EXCEL_2003:
                    Utility.createExcel2003(arr_tp, log_file);
                    break;

                default:
                    break;
            }
        } catch (SAXParseException err) {
            String desc = "Lối file: " + getFile_name() + "\n Mô tả: " + err.getMessage();
            throw new RuntimeException(desc, null);
        }
    }
}
