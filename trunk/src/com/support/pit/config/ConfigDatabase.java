/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.config;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ConfigDatabase {

    public ConfigDatabase() {
    }

    static String PROPERTIES_CONFIG_DB_ORACLE = "CONFIG_DATABASE_ORACLE";
    static Logger logger;

    static {
        Properties prop_oracle = new Properties();
        prop_oracle.setProperty("db.name", "PE1");
        prop_oracle.setProperty("db.class", "oracle.jdbc.driver.OracleDriver");
        prop_oracle.setProperty("db.url", "jdbc:oracle:thin:@10.64.85.52:1527/PE1");
        prop_oracle.setProperty("db.user", "SAP_READ");
        prop_oracle.setProperty("db.password", "123456");
        createDataFile(PROPERTIES_CONFIG_DB_ORACLE, "CONF", prop_oracle);
    }

    /**
     * Create data file properties oracle
     * @param name
     * @param suffix
     * @param properties 
     */
    static void createDataFile(String name, String suffix, Properties properties) {

        File cfg = new File(name + "." + suffix);
        if (!cfg.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.storeToXML(fos, "Configuration for connect database convert PSCD");
                fos.close();
                PROPERTIES_CONFIG_DB_ORACLE.startsWith("100");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to create the destination file " + cfg.getName(), e);
            }
        }

    }
    
    
}
