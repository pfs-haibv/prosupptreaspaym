/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.config;

import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
public class ConfigSupportTreasPaym {

    public ConfigSupportTreasPaym() {
    }
    static String PROPERTIES_CONFIG_DB_ORACLE = "CONFIG_TREASURY_PAYMENT";

    static {
        Properties prop = new Properties();
        //Connect oracle
        prop.setProperty("db.name", "PE1");
        prop.setProperty("db.class", "oracle.jdbc.driver.OracleDriver");
        prop.setProperty("db.url", "jdbc:oracle:thin:@10.64.85.52:1527/PE1");
        prop.setProperty("db.user", "SAP_READ");
        prop.setProperty("db.password", "123456");
        //Connect ftp
        prop.setProperty("ftp.url", "10.64.85.28");
        prop.setProperty("ftp.user", "px1adm");
        prop.setProperty("ftp.password", "Gdt$2012");
        prop.setProperty("ftp.WorkingDirectory", "/DataPaym/Archive");
        prop.setProperty("ftp.WorkingDirectoryBackup", "/DataPaym/Backup");
        prop.setProperty("ftp.WorkingDirectoryDataFile", "/DataPaym/Datafile");
        prop.setProperty("ftp.WorkingDirectoryDataPaym", "/DataPaym");
        createDataFile(PROPERTIES_CONFIG_DB_ORACLE, "CONF", prop);
    }

    /**
     * Create data file properties oracle
     * @param name
     * @param suffix
     * @param properties 
     */
    static void createDataFile(String name, String suffix, Properties properties) {
        File cfg = new File("mapping/"+ name + "." + suffix);
        if (!cfg.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.storeToXML(fos, "Information config Treasury payment ", "utf-8");
                fos.close();
                PROPERTIES_CONFIG_DB_ORACLE.startsWith("100");
            } catch (Exception e) {
                SupportTreasuryPaymView.logger.log(Level.WARNING, "Unable to create the destination file " + cfg.getName(), e);
            }
        }
    }

    /**
     * get infomation config
     * @return Properties
     */
    public static Properties getProp() {
        //Create file config
        ConfigSupportTreasPaym conf = new ConfigSupportTreasPaym();
        
        //get properties
        Properties prop = new Properties();
        InputStream is;
        try {
            is = new FileInputStream("mapping/CONFIG_TREASURY_PAYMENT.CONF");
            prop.loadFromXML(is);
        } catch (IOException e) {
           SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_GET_PROPERTIES, e.getMessage());
        }

        return prop;

    }
}
