package com.support.pit.paym;

import java.io.File;
import java.io.IOException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.support.pit.system.Constants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CheckMdyXML {

    // Tổng số bản ghi trong file
    private int record = 0;
    // Tiểu mục
    private int forTMuc_ = 0;
    // Mã kho bạc
    private String ma_kbac = "";
    // String ngày kho bạc
    private String ngay_kb = "";
    // String ngày chứng từ
    private String ngay_ct = "";
    // Tên file
    private String file_name = "";
    // Số chứng từ
    private String trans_no = "";
    // Tiểu mục nhận về PIT
    private int[] array_timuc = {1049, 1013, 1012, 1011, 1008, 1007,
        1006, 1005, 1004, 1003, 1001, 1014};
    private String array_tmuc = "1049, 1013, 1012, 1011, 1008, 1007, 1006, 1005, 1004, 1003, 1001, 1014";

    /**
     * read info treasury payment in file XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public void readXML() throws ParserConfigurationException, SAXException, IOException {

        // Write file text
        FileWriter fstream = new FileWriter(Constants.RESULT_CHK_FILE);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write("\tInformation on XML files");
        out.newLine();
        out.write("\t*******************");

        try {

            // Forder file scand
            File actual = new File(Constants.SRC_FILE_XML);

            for (File f : actual.listFiles()) {
                // Lay file XML
                int endIndex = f.getName().length();
                int beginIndex = endIndex - 3;
                // Scan file có định dạng .XML
                if (f.getName().substring(beginIndex, endIndex).equals("xml")) {

                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    // normalize text representation
                    Document doc = docBuilder.parse(new File(Constants.SRC_FILE_XML + f.getName()));
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
                            // Ngày kho bạc 
                            NodeList ngay_kb_List = firstCQTElement.getElementsByTagName("ngay_kb");
                            Element ngay_kb_Element = (Element) ngay_kb_List.item(0);
                            NodeList textngay_kb_List = ngay_kb_Element.getChildNodes();
                            // Ngày chứng từ
                            NodeList ngay_kbac_List = firstCQTElement.getElementsByTagName("ngay_kbac");
                            Element ngay_kbac_Element = (Element) ngay_kbac_List.item(0);
                            NodeList textngay_kbac_List = ngay_kbac_Element.getChildNodes();
                            // File name
                            file_name = f.getName();
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
//                             Move file
//                             String source = Constants.SRC_FILE_XML+f.getName();
//                             String targer = Constants.TARGET_;
//                             moveDir.moveFiles(source, targer);
                                }

                            } // End load TMuc

                        } // End check element node


                    } // End Transaction_No
                    // Write text file
                    if (record != 0) {
                        out.newLine();
                        out.write("File name: " + file_name + "\tMã kho bac: " + ma_kbac
                                + "\tNgay kho bac: " + ngay_kb + "\tNgay chung tu: " + ngay_ct
                                + "\tTrans_no: " + trans_no + "\tTotal records: " + record);
                    }
                }  // End scan file
            }
            out.close();
            fstream.close();
        } catch (SAXParseException err) {
            err.printStackTrace();
        }
    }

    /**
     * read info treasury payment in file XML
     * @param file
     * @param field_hdr
     * @param val_hdr
     * @param f_nnt
     * @param tran_no
     * @param ma_cqthu
     * @param so_ctu
     * @param ngay_kbac
     * @param so_tkno
     * @param ma_chuong
     * @param ma_tmuc
     * @param so_tien
     * @param ngay_kb
     * @param ma_kbac
     * @param khct
     * @param so_tkco
     * @param ma_khoan
     * @param ky_thue
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public void writeXML(String file, String field_hdr, String val_hdr, String f_nnt, String tran_no,
            String ma_cqthu, String so_ctu, String ngay_kbac, String so_tkno, String ma_chuong,
            String ma_tmuc, String so_tien, String ngay_kb, String ma_kbac, String khct,
            String so_tkco, String ma_khoan, String ky_thue)
            throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // normalize text representation
            Document doc = docBuilder.parse(new File(file));
            doc.getDocumentElement().normalize();
            /**************************************************************
             * Header Treasury payment                                    *      
             **************************************************************/
            // Header payment
            NodeList headerPaym = doc.getElementsByTagName("Header");
            for (int s = 0; s < headerPaym.getLength(); s++) {
                Node HeaderNode = headerPaym.item(0);

                // loop the child node
                NodeList list = HeaderNode.getChildNodes();

                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    //Change node
                    if (field_hdr.equals(node.getNodeName())) {
                        node.setTextContent(val_hdr);
                    }

                } // End check element node
            }
            /**************************************************************
             * Detail Treasury payment                                    *      
             **************************************************************/
            NodeList listOfPersons = doc.getElementsByTagName("Transaction");
            /* info NNT*/
            // Read file
            ArrayList tin = new ArrayList();
//             ArrayList name = new ArrayList();
//             ArrayList address = new ArrayList();
            FileReader fr = null;
            // Read to buffer
            BufferedReader br = null;
            String lines = "";
            try {
                fr = new FileReader(f_nnt);
                // Load data to buffer
                br = new BufferedReader(fr);
                while ((lines = br.readLine()) != null) {
                    //String rows[] = lines.split(",");
                    // add info NNT
                    tin.add(lines);
//                    System.out.println("lines: "+lines);
//                    name.add(rows[1]);
//                    address.add(rows[2]);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //index rows
            int idx = 0;
            int idx_tran_no = 50000;
            String defaul_soct = "";
            int so_ct = Integer.parseInt(so_ctu);
            // loop transaction
            for (int s = 0; s < listOfPersons.getLength(); s++) {
                //change info on file NNT
                if (s < tin.size()) {
                    idx++;
                    so_ct++;
                    idx_tran_no++;

                    Node chungTuNode = listOfPersons.item(s);

                    // loop the child node
                    NodeList list = chungTuNode.getChildNodes();



                    for (int i = 0; i < list.getLength(); i++) {
                        Node node = list.item(i);

                        if (!"#text".equals(node.getNodeName())) {
                            //change tran_no
                            if (!tran_no.isEmpty() && "tran_no".equals(node.getNodeName())) {
                                node.setTextContent(tran_no + idx_tran_no);
                            }
                            //change lcn_owner                    
                            if (!tran_no.isEmpty() && "lcn_owner".equals(node.getNodeName())) {
                                node.setTextContent(tran_no);
                            }
                            //change ma_cqthu                    
                            if (!ma_cqthu.isEmpty() && "ma_cqthu".equals(node.getNodeName())) {
                                node.setTextContent(ma_cqthu);
                            }
                            //change ngay_kbac                    
                            if (!ngay_kbac.isEmpty() && "ngay_kbac".equals(node.getNodeName())) {
                                node.setTextContent(ngay_kbac);
                            }
                            //change ma_chuong                    
                            if (!ma_chuong.isEmpty() && "ma_chuong".equals(node.getNodeName())) {
                                node.setTextContent(ma_chuong);
                            }
                            //change so_tkno                    
                            if (!so_tkno.isEmpty() && "so_tkno".equals(node.getNodeName())) {
                                node.setTextContent(so_tkno);
                            }
                            //change ma_tmuc                    
                            if ("ma_tmuc".equals(node.getNodeName())) {
                                node.setTextContent(ma_tmuc);
                            }
                            //change so_tien                    
                            if (!so_tien.isEmpty() && "so_tien".equals(node.getNodeName())) {
                                node.setTextContent(so_tien);
                            }
                            //change ngay_kb                    
                            if (!ngay_kb.isEmpty() && "ngay_kb".equals(node.getNodeName())) {
                                node.setTextContent(ngay_kb);
                            }
                            //change ma_kbac                    
                            if (!ma_kbac.isEmpty() && "ma_kbac".equals(node.getNodeName())) {
                                node.setTextContent(ma_kbac);
                            }
                            //change so_tkco                    
                            if (!so_tkco.isEmpty() && "so_tkco".equals(node.getNodeName())) {
                                node.setTextContent(so_tkco);
                            }
                            //change ma_khoan                    
                            if (!ma_khoan.isEmpty() && "ma_khoan".equals(node.getNodeName())) {
                                node.setTextContent(ma_khoan);
                            }
                            //change ky_thue                    
                            if (!ky_thue.isEmpty() && "ky_thue".equals(node.getNodeName())) {
                                node.setTextContent(ky_thue);
                            }
                            //change so_ctu                    
                            if (!so_ctu.isEmpty() && "so_ctu".equals(node.getNodeName())) {
                                node.setTextContent(defaul_soct + so_ct);
                            }
                            //change khct                    
                            if (!khct.isEmpty() && "khct".equals(node.getNodeName())) {
                                node.setTextContent(khct);
                            }
                            //khct_soct
                            if ("khct_soct".equals(node.getNodeName())) {
                                node.setTextContent(khct + defaul_soct + so_ct);
                            }

                            //info NNT
                            //change tin                    
                            if ("tin".equals(node.getNodeName())) {
                                node.setTextContent(tin.get(s).toString());
                            }
                        }

                    } // End check element node
                    System.out.println("transaction :" + s);
                }
            } // End Transaction_No

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file));
            transformer.transform(source, result);
        } catch (SAXParseException err) {
            err.printStackTrace();
        }
    }

    /**
     * read info treasury payment in file XML
     * @param file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public void writeXML_TMuc(String file)
            throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // normalize text representation
            Document doc = docBuilder.parse(new File(file));
            doc.getDocumentElement().normalize();
            /**************************************************************
             * Detail Treasury payment                                    *      
             **************************************************************/
            NodeList listOfPersons = doc.getElementsByTagName("Transaction");

            // loop transaction
            for (int s = 0; s < listOfPersons.getLength(); s++) {
                //change info on file NNT

                Node chungTuNode = listOfPersons.item(s);

                Element firstCQTElement = (Element) chungTuNode;

                // Tiểu mục
                NodeList chungTuList = firstCQTElement.getElementsByTagName("ma_tmuc");
                Element chungTuElement = (Element) chungTuList.item(0);
                NodeList textAgeList = chungTuElement.getChildNodes();

                // Loại bỏ tiểu mục values null
                if (textAgeList.getLength() != 0) {
                    forTMuc_ = Integer.parseInt(((Node) textAgeList.item(0)).getNodeValue());
                    
                }

                System.out.println("tmuc: "+forTMuc_);
                
                if (array_tmuc.indexOf(forTMuc_) != 0) {
        //            Node.removeChild(node);
                     
                    //removeAll(doc, listOfPersons.item(s), "Transaction");
                    
                    //listOfPersons.item(s).removeChild(chungTuNode);
                    chungTuNode.removeChild(chungTuNode);
                }

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file));
            transformer.transform(source, result);
        } catch (SAXParseException err) {
            err.printStackTrace();
        }
    }
    /**
     * remove all file
     * @param node
     * @param nodeType
     * @param name 
     */
    public static void removeAll(Node node, short nodeType, String name) {
    if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
      node.getParentNode().removeChild(node);
    } else {
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
        removeAll(list.item(i), nodeType, name);
      }
    }
    }
    
    /**
     * Thay đổi thông tin tiểu mục theo folder chứa file xml VD: 1010/name.xml
     * <p> read info treasury payment in file XML
     * @param srcfolder
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public void modifyTMuc(String srcfolder)
            throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            // normalize text representation
            File s_folder = new File(srcfolder);
            File dirFile;
            String ma_tmuc = "";
            String c_file = "";
            for (File src : s_folder.listFiles()) {


                // mã tiểu mục lấy theo tên folder
                ma_tmuc = src.getName();

                dirFile = new File(s_folder + "\\" + src.getName());

                //Scand list file on folder
                for (File l : dirFile.listFiles()) {

                    //file đang chỉnh sửa
                    c_file = dirFile + "\\" + l.getName();

                    Document doc = docBuilder.parse(new File(c_file));
                    doc.getDocumentElement().normalize();

                    /**************************************************************
                     * Detail Treasury payment                                    *      
                     **************************************************************/
                    NodeList listOfPersons = doc.getElementsByTagName("Transaction");

                    for (int s = 0; s < listOfPersons.getLength(); s++) {

                        Node chungTuNode = listOfPersons.item(s);
                        // loop the child node
                        NodeList list = chungTuNode.getChildNodes();

                        for (int q = 0; q < list.getLength(); q++) {
                            Node node = list.item(q);
                            if (!"#text".equals(node.getNodeName())) {
                                //ma_tmuc
                                if ("ma_tmuc".equals(node.getNodeName())) {
                                    node.setTextContent(ma_tmuc);
                                }
                            }

                        } // End check element node

                    } // End Transaction_No

                    // write the content into xml file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(c_file));
                    transformer.transform(source, result);
                    //Hiển thị thông báo cho từng file khi hoàn thành
//                    SupportTreasuryPaymView.getDoneFile(c_file);
                    System.out.println("done file " + c_file);

                }//end scand file in sub forder

            } // end scand forder

        } catch (SAXParseException err) {
            err.printStackTrace();
        }

    }
        
}