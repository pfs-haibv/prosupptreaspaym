/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.config;

import com.support.pit.datatype.MapCqt;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Message;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class LoadMapDMuc {

    
    //Lưu thông tin file xml map cqt
    public static ArrayList<MapCqt> arr_cqt = new ArrayList<MapCqt>();
    
    public static void loadDMucCQT(File file) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            // Header payment
            NodeList listOfCQT = doc.getElementsByTagName("info_cqt");

            for (int s = 0; s < listOfCQT.getLength(); s++) {
                Node infoNodeCqt = listOfCQT.item(s);
                //Check ELEMENT NODE
                if (infoNodeCqt.getNodeType() == Node.ELEMENT_NODE) {
                    Element cqtListElement = (Element) infoNodeCqt;
                    NodeList nl_map_cqt;
                    Element e_map_cqt;
                    // mã cqt
                    nl_map_cqt      = cqtListElement.getElementsByTagName("ma_cqt");
                    e_map_cqt       = (Element) nl_map_cqt.item(0);
                    NodeList nl_ma_cqt      = e_map_cqt.getChildNodes();
                    // mã cqthu
                    nl_map_cqt      = cqtListElement.getElementsByTagName("ma_cqthu");
                    e_map_cqt       = (Element) nl_map_cqt.item(0);
                    NodeList nl_ma_cqthu    = e_map_cqt.getChildNodes();
                    // mã kho bạc
                    nl_map_cqt      = cqtListElement.getElementsByTagName("ma_kb");
                    e_map_cqt       = (Element) nl_map_cqt.item(0);
                    NodeList nl_ma_kb    = e_map_cqt.getChildNodes();
                    // lcn_owner
                    nl_map_cqt      = cqtListElement.getElementsByTagName("lcn_owner");
                    e_map_cqt       = (Element) nl_map_cqt.item(0);
                    NodeList nl_lcn_owner    = e_map_cqt.getChildNodes();
                    // tên cqt
                    nl_map_cqt      = cqtListElement.getElementsByTagName("ten_cqt");
                    e_map_cqt       = (Element) nl_map_cqt.item(0);
                    NodeList nl_ten_cqt    = e_map_cqt.getChildNodes();
                    
                    //List info map -> array list info
                    MapCqt mapcqt = new MapCqt();
                    
                    mapcqt.setMa_cqt(((Node) nl_ma_cqt.item(0)).getNodeValue());
                    //check value is not null
                    if (nl_ma_cqthu.getLength() != 0)
                    mapcqt.setMa_cqthu(((Node) nl_ma_cqthu.item(0)).getNodeValue());
                    //check value is not null
                    if (nl_ma_kb.getLength() != 0)
                    mapcqt.setMa_kb(((Node) nl_ma_kb.item(0)).getNodeValue());
                    //check value is not null
                    if (nl_lcn_owner.getLength() != 0)
                    mapcqt.setLcn_owner(((Node) nl_lcn_owner.item(0)).getNodeValue());
                    //check value is not null
                    if (nl_ten_cqt.getLength() != 0)
                    mapcqt.setTen_cqt(((Node) nl_ten_cqt.item(0)).getNodeValue());
                    
                    arr_cqt.add(mapcqt);
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {           
            SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_LOAD_DMUC, e.getMessage());
        }
    }
    
 
}
