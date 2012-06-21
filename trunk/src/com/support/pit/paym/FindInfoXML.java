package com.support.pit.paym;

import com.support.pit.utility.Utility;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class FindInfoXML {

    // Tổng số bản ghi trong file
    private static int record = 0;
    // Tiểu mục
    private static int forTMuc_ = 0;
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

    public static String getFile_name() {
        return file_name;
    }
    //Tran_no
    private static String trans_no = "";

    public static void setFile_name(String file_name) {
        FindInfoXML.file_name = file_name;
    }
    // Tiểu mục nhận về PIT
    private static int[] array_timuc = {1049,1013,1012,1011,1008,1007,1006,1005,1004,1003,1001,1014};
    /**
     * @date: 13.03.2011
     * @desc: read info treasury payment in file XML
     */
    public static void readXML(String source_file, String log_file) throws ParserConfigurationException, SAXException, IOException {

        // Write file text
        FileWriter fstream = new FileWriter(log_file);
        BufferedWriter out = new BufferedWriter(fstream);

        try {

            // Forder file scand
            File actual = new File(source_file);

            for (File f : actual.listFiles()) {
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
                    record = 0;
                    for (int s = 0; s < listOfPersons.getLength(); s++) {

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
                                forTMuc_ = Integer.parseInt(((Node) textAgeList.item(0)).getNodeValue());
                            }

                            // Load TMuc và lấy tiểu mục theo yêu cầu
                            for (int i = 0; i < array_timuc.length; i++) {
                                if (forTMuc_ == array_timuc[i]) {
//                            Số bản ghi theo tiểu mục
                                    record++;
                                }

                            } // End load TMuc

                        } // End check element node                    

                    } // End Transaction_No
                    // Write text file
                    if (record != 0) {
                        out.newLine();
                        out.write("File name: " + file_name
                                + "\tMã cqt: " + 
                                Utility.getMapCQT(ma_kbac)
                                + "\tNgay kho bac: " + ngay_kb
                                + "\tTotal records: " + record);

                    }
                }  // End scan file
            }
            out.close();
            fstream.close();
        } catch (SAXParseException err) {
            String desc = "Lối file: " + getFile_name() + "\n Mô tả: " + err.getMessage();
            System.out.println("---" + desc);
            throw new RuntimeException(desc, null);
        }
    }
}