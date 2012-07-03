/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.serializable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.support.pit.table_map.ztb_map_cqt;
import com.support.pit.datatype.MapCqt;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author Administrator
 */
public class SerializableDemo {

    /**
     * set data table map cqt
     * @throws IOException 
     */
    public static void setDataMapCQT() throws IOException {
        FileOutputStream f_o_name = new FileOutputStream("table/ztb_map_cqt.map");
        ObjectOutputStream out = new ObjectOutputStream(f_o_name);
        for (int i = 0; i < ztb_map_cqt.map_cqt.length; i++) {
            for (int j = 0; j < 3; j++) {
                MapCqt cqt_ = new MapCqt();
                cqt_.setMa_cqt(ztb_map_cqt.map_cqt[i][0]);
                cqt_.setMa_kb(ztb_map_cqt.map_cqt[i][1]);
                cqt_.setTen_cqt(ztb_map_cqt.map_cqt[i][2]);
                out.writeObject(cqt_);
            }
        }
        out.close();
    }

    /**
     * get data table map cqt 
     * @throws IOException 
     */
    public static void getDataMapCQT() throws IOException {
        MapCqt cqt_ = null;
        try {
            FileInputStream fileIn = new FileInputStream("table/ztb_map_cqt.map");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cqt_ = (MapCqt) in.readObject();
            while ((cqt_ = (MapCqt) in.readObject()) != null) {
                System.out.println("cqt:" + cqt_.getMa_cqt() + ", " + cqt_.getMa_kb() + ", " + cqt_.getTen_cqt());
            }
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println(".Employee class not found.");
            c.printStackTrace();
            return;
        }
    }
    
    
    public static String[] getDataMapCQT(String ma_kb) throws IOException {
        String result[] = new String[2];
        MapCqt cqt_ = null;
        try {
            FileInputStream fileIn = new FileInputStream("table/ztb_map_cqt.map");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cqt_ = (MapCqt) in.readObject();
            while ((cqt_ = (MapCqt) in.readObject()) != null) {
                //trường hợp tồn tại ma_kb
                    if (cqt_.getMa_kb().equals(ma_kb)) {
                        //ma_cqt
                        result[0] = cqt_.getMa_cqt();
                        //ten cqt
                        result[1] = cqt_.getTen_cqt();
                        //thoát khi tìm thấy
                        break;
                    }
            }
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();           
        } catch (ClassNotFoundException c) {
            System.out.println(".Employee class not found.");
            c.printStackTrace();            
        }
        
        return result;
    }
    
    
}
