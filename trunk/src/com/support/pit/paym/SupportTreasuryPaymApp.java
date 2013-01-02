/*
 * SupportTreasuryPaymApp.java
 */
package com.support.pit.paym;

import com.support.pit.conn.ConnectDB;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SupportTreasuryPaymApp extends SingleFrameApplication {

        //Connection oracle
    public static Connection connORA = null;
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new SupportTreasuryPaymView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SupportTreasuryPaymApp
     */
    public static SupportTreasuryPaymApp getApplication() {
        return Application.getInstance(SupportTreasuryPaymApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) throws IOException, SQLException {
        try {
            connORA = ConnectDB.getConnORA();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),
                                    "Lỗi kết nối CSDL",
                                    JOptionPane.ERROR_MESSAGE);            
    }
        launch(SupportTreasuryPaymApp.class, args);

    }
}
