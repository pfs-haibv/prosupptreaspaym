/*
 * SupportTreasuryPaymView.java
 */
package com.support.pit.paym;

import com.support.pit.serializable.SerializableDemo;
import com.support.pit.system.Constants;
import com.support.pit.utility.Utility;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * The application's main frame.
 */
public class SupportTreasuryPaymView extends FrameView {

    public SupportTreasuryPaymView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {

        if (aboutBox == null) {
            JFrame mainFrame = SupportTreasuryPaymApp.getApplication().getMainFrame();
            aboutBox = new SupportTreasuryPaymAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SupportTreasuryPaymApp.getApplication().show(aboutBox);

    }

    @Action
    public void setSerializable() throws IOException{
        SerializableDemo.setDataMapCQT();
    }    
    
    @Action
    public void getDeserilizable() throws IOException{
        SerializableDemo.getDataMapCQT();
    }
    
    @Action
    public void getPartFile() {
        getDirectory(Constants.PART_MODIFY_FILES);
    }

    @Action
    public void getSrcFolder() {
        getDirectory(Constants.PART_MODIFY_SRC_FOLDER);
    }

    @Action
    public void getPartFileNNT() {
        getDirectory(Constants.PART_MODIFY_INFO_NNT);
    }

    @Action
    public void PartLogFile() {
        getDirectory(Constants.PART_INFO_PAYM_EXP_FILE);
    }

    @Action
    public void PartScandFolder() {
        getDirectory(Constants.PART_INFO_PAYM_SRC_FORDER);
    }

    @Action
    public void PartFolderTK() {
        getDirectory(Constants.PART_CREATE_FORM_CQT);
    }

    @Action
    public void PartFolderInFTK() {
        getDirectory(Constants.PART_CREATE_FORM_MST);
    }
   
    @Action
    public void PartPaymErr() {
        getDirectory(Constants.PART_INFO_PAYM_ERR_FORDER);
    }   
    
    /**
     * Hiển thị thông tin file xml, xem code @see FindInfoXML
     */
    @Action
    public void InfoTreasuryPaym() {
        String source_file = txtScandFolder.getText();
        String log_file = txtFileLog.getText();

        String type_excel = "";

        if (radXLSX.isSelected()) {
            type_excel = Constants.TYPE_EXCEL_2007;
        } else {
            type_excel = Constants.TYPE_EXCEL_2003;
        }

        try {
            switch (type_excel) {
                
                case Constants.TYPE_EXCEL_2007:
                    FindInfoXML.readXML(source_file, log_file, Constants.TYPE_EXCEL_2007);
                    lblSuc1.setText("Hoàn thành lấy thông tin file XML, kiểm tra thông tin trong " + log_file + ".");
                    break;

                case Constants.TYPE_EXCEL_2003:
                    FindInfoXML.readXML(source_file, log_file, Constants.TYPE_EXCEL_2003);
                    lblSuc1.setText("Hoàn thành lấy thông tin file XML, kiểm tra thông tin trong " + log_file + ".");
                    break;

                default:
                    lblSuc1.setText("Không tồn tại file để kiểm tra");
                    break;                    
            }

        } catch (ParserConfigurationException ex) {
            lblSuc1.setText(" Kiểm tra lại đường dẫn khi quét file " + source_file + ".");
        } catch (SAXException ex) {
            lblSuc1.setText(" Kiểm tra lại đường dẫn khi quét file " + source_file + ".");
        } catch (IOException ex) {
            lblSuc1.setText(" Kiểm tra lại đường dẫn khi quét file " + source_file + ".");
            JOptionPane.showMessageDialog(btnCheckXML,
                    ex.getMessage(),
                    "Lại lỗi rồi các thầy kho bạc ơi!!! Các thầy củ hành em quá vậy trời",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            lblSuc1.setText(" Kiểm tra lại đường dẫn khi quét file" + source_file + ".");
            JOptionPane.showMessageDialog(btnCheckXML,
                    ex.getMessage(),
                    "Lại lỗi rồi các thầy kho bạc ơi!!! Các thầy củ hành em quá vậy trời",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lưu file xml sau khi sửa
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    @Action
    public void saveXML() throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        //file name
        String f_name = txtMdfFile.getText();
        String f_nnt = txtInfoNNT.getText();
        /******************************************************
         * Change Header
         ******************************************************/
        //change filed hdr
        String filed_hdr = cboHdr.getSelectedItem().toString();
        //change value hdr
        String val_hdr = txtValHdr.getText();
        /******************************************************
         * Change Detail
         ******************************************************/
        String tran_no = txttran_no.getText();
        String ma_cqthu = txtma_cqthu.getText();
        String so_ctu = txtso_ctu.getText();
        String ngay_kbac = txtngay_kbac.getText();
        String so_tkno = txtso_tkno.getText();
        String ma_chuong = txtma_chuong.getText();
        String ma_tmuc = cboma_tmuc.getSelectedItem().toString();
        String so_tien = txtso_tien.getText();
        String ngay_kb = txtngay_kb.getText();
        String ma_kbac = txtma_kbac.getText();
        String khct = txtkhct.getText();
        String so_tkco = txtso_tkco.getText();
        String ma_khoan = txtma_khoan.getText();
        String ky_thue = txtky_thue.getText();
        //Save xml
        CheckMdyXML mdyXML = new CheckMdyXML();
        mdyXML.writeXML(f_name, filed_hdr, val_hdr, f_nnt, tran_no, ma_cqthu, so_ctu,
                ngay_kbac, so_tkno, ma_chuong, ma_tmuc, so_tien, ngay_kb, ma_kbac,
                khct, so_tkco, ma_khoan, ky_thue);
        lblSuc.setText("DONE!!!");

    }

    /**
     * Lấy dữ liệu tờ khai theo mẫu
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void getDataToKhai() throws FileNotFoundException, IOException {

        /* Lấy thông tin mẫu tờ khai số 1 */
        String line = null;

        BufferedReader buff_mautk1_1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau1/append.txt")), "UTF8"));
        BufferedReader buff_mautk1_2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau1/append_2.txt")), "UTF8"));
        BufferedReader buff_mautk1_3 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau1/append_3.txt")), "UTF8"));

        while ((line = buff_mautk1_1.readLine()) != null) {
            mautk1_1.append(line);
        }
        while ((line = buff_mautk1_2.readLine()) != null) {
            mautk1_2.append(line);
        }
        while ((line = buff_mautk1_3.readLine()) != null) {
            mautk1_3.append(line);
        }
        buff_mautk1_1.close();
        buff_mautk1_2.close();
        buff_mautk1_3.close();

        /*Lấy thông tin mẫu tờ khai số 2*/
        BufferedReader buff_mautk2_1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau2/append.txt")), "UTF8"));
        BufferedReader buff_mautk2_2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau2/append_2.txt")), "UTF8"));
        BufferedReader buff_mautk2_3 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/SuppPaym/data/DL_to_khai/mautk/mau2/append_3.txt")), "UTF8"));

        while ((line = buff_mautk2_1.readLine()) != null) {
            mautk2_1.append(line);
        }
        while ((line = buff_mautk2_2.readLine()) != null) {
            mautk2_2.append(line);
        }
        while ((line = buff_mautk2_3.readLine()) != null) {
            mautk2_3.append(line);
        }
        buff_mautk2_1.close();
        buff_mautk2_2.close();
        buff_mautk2_3.close();

    }

    /**
     * <p> Thay đổi thông tin tiểu mục theo folder chứa file xml
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    @Action
    public void changeXMLTMUC() throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        CheckMdyXML update_tm = new CheckMdyXML();
        update_tm.modifyTMuc(txtSourFolder.getText());
        lblSuc.setText("DONE!!!");
    }

    /**
     * <p> <b> Thực hiện check và lặp lại đến khi hết các file xml lỗi </b>
     * @param srcFolder
     * @param errFolder
     * @return check thành công
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ExceptionInInitializerError
     * @throws JCoException 
     */
    public static String checkParseXML(String srcFolder, String errFolder) throws SAXException, IOException, ParserConfigurationException, ExceptionInInitializerError {
        FileReader f_reader = null;
        BufferedReader br = null;
        XMLReader xr = null;
        File f_xml = null;
        File dirForder = null;
        try {
            // Forder file scand
            dirForder = new File(srcFolder);

            for (File l : dirForder.listFiles()) {

                // get file XML
                int endIndex = l.getName().length();
                int beginIndex = endIndex - 3;
                // Scan file có định dạng .XML
                if (l.getName().substring(beginIndex, endIndex).equals("xml")) {
                    f_xml = new File(dirForder + "\\" + l.getName());
                    InputSource input = null;
                    // Create SAX 2 parser...
                    xr = XMLReaderFactory.createXMLReader();
                    f_reader = new FileReader(f_xml);
                    br = new BufferedReader(f_reader);
                    input = new InputSource(br);
                    xr.parse(input);
                }
            }
        } catch (SAXParseException err_) {
            f_reader.close();
            //Move file error
            Utility.moveFiles(f_xml.toString(), errFolder);
            //Quét lại
            return checkParseXML(srcFolder, errFolder);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return "DONE !!!";
    }

    /**
     * <b> Check structure file xml </b>
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws JCoException
     */
    @Action
    public void checkXML() throws ParserConfigurationException, SAXException, IOException {
        try {
            Long s_check = System.currentTimeMillis();
            lblSuc1.setText("PLEASE WAIT ... CHECKING & COPPING PARSE FILE XML.");
            // *** 01.Thực hiện check file xml ***
            lblSuc1.setText(checkParseXML(txtScandFolder.getText(), txtErrFolder.getText()));
            lblSuc1.setText("Completed within " + (System.currentTimeMillis() - s_check) / 1000 + " seconds!!!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *<b> Tạo dữ liệu tờ khai theo mã số thuế </b>
     *<p> quét thư mục chứa các file text trong file chứa các mst của cqt đó.
     *<p> thực hiện tạo thư mục theo tên file text (tức là mã cqt)
     *<p> tạo các file dữ liệu tờ khai trong thư mục cqt với dữ liệu là dữ liệu trong file text
     *<p> thực hiện xong chuyển file sang thư mục backup.
     * @throws IOException 
     */
    @Action
    public void createDataTK() throws IOException, InterruptedException {
        //Lấy thông tin các mẫu tờ khai
        getDataToKhai();
        //Quét thư mục chứa thông tin cập nhật
        File dirForder = new File(txtInforTK.getText());
        File dirFile, f_txt, crtDataTK;
        //Scand list folder
        for (File f : dirForder.listFiles()) {

            //Dir forder chứa file txt
            dirFile = new File(txtInforTK.getText() + "\\" + f.getName());

            //Scand list file on folder
            for (File l : dirFile.listFiles()) {
                int endIndex = l.getName().length();
                int beginIndex = endIndex - 3;
                // Scan file có định dạng .TXT
                if (l.getName().substring(beginIndex, endIndex).toUpperCase().equals("TXT")) {

                    //File đang sử dụng
                    f_txt = new File(dirFile + "\\" + l.getName());
                    //mã cqt
                    String cqt = l.getName().substring(0, 5);
                    //Tạo thư mục cqt chứa các file tờ khai                  
                    String ma_cqt = l.getName().substring(0, 5);
                    String fld_tk = txtTKCreate.getText() + "\\" + ma_cqt;
                    crtDataTK = new File(fld_tk);
                    crtDataTK.mkdir();
                    //Đọc file dữ liệu
                    FileInputStream fis_txt = new FileInputStream(f_txt);
                    BufferedInputStream bis_txt = new BufferedInputStream(fis_txt);
                    DataInputStream dis_txt = new DataInputStream(bis_txt);
                    String line = "";
                    String flag = "X";
                    //Đặt tên file TK
                    String file_name = "";
                    int i = 0;
                    while ((line = dis_txt.readLine()) != null) {
                        i++;
                        file_name = ma_cqt + "-000000000000" + i + "-05TL-Y2011-L00.XML";

                        File file = new File(fld_tk + "\\" + file_name);
                        //Tạo file dữ liệu tờ khai
//                        try {
                        //Create file 
                        if (!file.exists()) {
                            file.createNewFile();
                            //write use buffering               
                            Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                            //ghi cho từng trường hợp, lần đầu ghi tờ khai mẫu 1, lần 2 ghi tờ khai mẫu 2 và lặp lại
                            //ghi cho mẫu tờ khai thứ 2
                            if (!flag.equals("")) {
                                StringBuffer write_ = new StringBuffer();
                                write_.append(mautk2_1.toString());
                                write_.append(line.replace("ï»¿", ""));
                                write_.append(mautk2_2.toString());
                                write_.append(cqt);
                                write_.append(mautk2_3.toString());

                                output.write(write_.toString().replace("﻿", ""));
                                flag = "";
                            } else {
                                StringBuffer write_ = new StringBuffer();
                                write_.append(mautk1_1.toString());
                                write_.append(line.replace("ï»¿", ""));
                                write_.append(mautk1_2.toString());
                                write_.append(cqt);
                                write_.append(mautk1_3.toString());

                                output.write(write_.toString().replace("﻿", ""));

                                flag = "X";
                            }

                            output.flush();
                            output.close();
                        }
                    }
                    lblMessTK.setText("Hoàn thành tạo dữ liệu cho cqt: " + cqt);
                }
            }
        }

        lblMessTK.setText("Quá trình tạo dữ liệu đã hoàn thành!");

    }

    /**
     * @desc display file choose
     * @param c 
     */
    public void getDirectory(String part) {

        fc.showOpenDialog(btnGetFile);
        File file = fc.getCurrentDirectory();
        switch (part) {

            case Constants.PART_MODIFY_FILES:
                file = fc.getSelectedFile();
                txtMdfFile.setText(file.getPath());
                break;

            case Constants.PART_MODIFY_INFO_NNT:
                file = fc.getSelectedFile();
                txtInfoNNT.setText(file.getPath());
                break;

            case Constants.PART_INFO_PAYM_EXP_FILE:
                file = fc.getSelectedFile();
                txtFileLog.setText(file.getPath());
                break;
            case Constants.PART_INFO_PAYM_ERR_FORDER:                
                txtErrFolder.setText(file.getPath());
                break;
                
            case Constants.PART_INFO_PAYM_SRC_FORDER:
                txtScandFolder.setText(file.getPath());
                break;

            case Constants.PART_MODIFY_SRC_FOLDER:
                txtSourFolder.setText(file.getPath());
                break;

            case Constants.PART_CREATE_FORM_CQT:
                txtTKCreate.setText(file.getPath());
                break;

            case Constants.PART_CREATE_FORM_MST:
                txtInforTK.setText(file.getPath());
                break;

            default:
                break;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabPay = new javax.swing.JTabbedPane();
        pnlCheckPaym = new javax.swing.JPanel();
        lblSuc = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtSourFolder = new javax.swing.JTextField();
        btnGetSrcFolder = new javax.swing.JButton();
        btnModifyTMuc = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnlHdr1 = new javax.swing.JPanel();
        lbltran_no = new javax.swing.JLabel();
        txttran_no = new javax.swing.JTextField();
        txtngay_kb = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtso_ctu = new javax.swing.JTextField();
        txtkhct = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtma_kbac = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtma_cqthu = new javax.swing.JTextField();
        txtngay_kbac = new javax.swing.JTextField();
        txtso_tkno = new javax.swing.JTextField();
        txtma_chuong = new javax.swing.JTextField();
        txtso_tien = new javax.swing.JTextField();
        txtso_tkco = new javax.swing.JTextField();
        txtma_khoan = new javax.swing.JTextField();
        txtky_thue = new javax.swing.JTextField();
        cboma_tmuc = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        txtInfoNNT = new javax.swing.JTextField();
        btnNNT = new javax.swing.JButton();
        pnlHdr = new javax.swing.JPanel();
        cboHdr = new javax.swing.JComboBox();
        txtValHdr = new javax.swing.JTextField();
        btnGetFile = new javax.swing.JButton();
        txtMdfFile = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnModify = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnCheckXML = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblSuc1 = new javax.swing.JLabel();
        btnChkXML = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtScandFolder = new javax.swing.JTextField();
        btnSndFld = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtFileLog = new javax.swing.JTextField();
        btnLogFile = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txtErrFolder = new javax.swing.JTextField();
        btnErrFld = new javax.swing.JButton();
        radXLSX = new javax.swing.JRadioButton();
        radXLS = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtInforTK = new javax.swing.JTextField();
        btnTKDulieu = new javax.swing.JButton();
        btnTK = new javax.swing.JButton();
        btnCloseTK = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txtTKCreate = new javax.swing.JTextField();
        btnCreateDT = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        btnRad = new javax.swing.ButtonGroup();

        mainPanel.setName("mainPanel"); // NOI18N

        tabPay.setName("tabPay"); // NOI18N

        pnlCheckPaym.setName("pnlCheckPaym"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.support.pit.paym.SupportTreasuryPaymApp.class).getContext().getResourceMap(SupportTreasuryPaymView.class);
        lblSuc.setFont(resourceMap.getFont("lblSuc.font")); // NOI18N
        lblSuc.setText(resourceMap.getString("lblSuc.text")); // NOI18N
        lblSuc.setName("lblSuc"); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        txtSourFolder.setName("txtSourFolder"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.support.pit.paym.SupportTreasuryPaymApp.class).getContext().getActionMap(SupportTreasuryPaymView.class, this);
        btnGetSrcFolder.setAction(actionMap.get("getSrcFolder")); // NOI18N
        btnGetSrcFolder.setText(resourceMap.getString("btnGetSrcFolder.text")); // NOI18N
        btnGetSrcFolder.setName("btnGetSrcFolder"); // NOI18N

        btnModifyTMuc.setAction(actionMap.get("changeXMLTMUC")); // NOI18N
        btnModifyTMuc.setText(resourceMap.getString("btnModifyTMuc.text")); // NOI18N
        btnModifyTMuc.setActionCommand(resourceMap.getString("btnModifyTMuc.actionCommand")); // NOI18N
        btnModifyTMuc.setName("btnModifyTMuc"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(14, 14, 14)
                .addComponent(txtSourFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnGetSrcFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addComponent(btnModifyTMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSourFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGetSrcFolder)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModifyTMuc))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        pnlHdr1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnlHdr1.border.title"))); // NOI18N
        pnlHdr1.setName("pnlHdr1"); // NOI18N

        lbltran_no.setText(resourceMap.getString("lbltran_no.text")); // NOI18N
        lbltran_no.setName("lbltran_no"); // NOI18N

        txttran_no.setText(resourceMap.getString("txttran_no.text")); // NOI18N
        txttran_no.setName("txttran_no"); // NOI18N

        txtngay_kb.setText(resourceMap.getString("txtngay_kb.text")); // NOI18N
        txtngay_kb.setToolTipText(resourceMap.getString("txtngay_kb.toolTipText")); // NOI18N
        txtngay_kb.setName("txtngay_kb"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtso_ctu.setText(resourceMap.getString("txtso_ctu.text")); // NOI18N
        txtso_ctu.setName("txtso_ctu"); // NOI18N

        txtkhct.setName("txtkhct"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        txtma_kbac.setName("txtma_kbac"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        txtma_cqthu.setName("txtma_cqthu"); // NOI18N

        txtngay_kbac.setText(resourceMap.getString("txtngay_kbac.text")); // NOI18N
        txtngay_kbac.setName("txtngay_kbac"); // NOI18N

        txtso_tkno.setText(resourceMap.getString("txtso_tkno.text")); // NOI18N
        txtso_tkno.setName("txtso_tkno"); // NOI18N

        txtma_chuong.setText(resourceMap.getString("txtma_chuong.text")); // NOI18N
        txtma_chuong.setName("txtma_chuong"); // NOI18N

        txtso_tien.setText(resourceMap.getString("txtso_tien.text")); // NOI18N
        txtso_tien.setName("txtso_tien"); // NOI18N

        txtso_tkco.setText(resourceMap.getString("txtso_tkco.text")); // NOI18N
        txtso_tkco.setName("txtso_tkco"); // NOI18N

        txtma_khoan.setText(resourceMap.getString("txtma_khoan.text")); // NOI18N
        txtma_khoan.setName("txtma_khoan"); // NOI18N

        txtky_thue.setText(resourceMap.getString("txtky_thue.text")); // NOI18N
        txtky_thue.setName("txtky_thue"); // NOI18N

        cboma_tmuc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1001", "1003", "1004", "1005", "1006", "1007", "1008", "1011", "1012", "1013", "1014", "1049" }));
        cboma_tmuc.setName("cboma_tmuc"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        txtInfoNNT.setName("txtInfoNNT"); // NOI18N

        btnNNT.setAction(actionMap.get("getPartFileNNT")); // NOI18N
        btnNNT.setText(resourceMap.getString("btnNNT.text")); // NOI18N
        btnNNT.setName("btnNNT"); // NOI18N

        javax.swing.GroupLayout pnlHdr1Layout = new javax.swing.GroupLayout(pnlHdr1);
        pnlHdr1.setLayout(pnlHdr1Layout);
        pnlHdr1Layout.setHorizontalGroup(
            pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHdr1Layout.createSequentialGroup()
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHdr1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbltran_no, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtso_tien, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboma_tmuc, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtma_chuong, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_tkno, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtngay_kbac, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtso_ctu, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtma_cqthu, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttran_no, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlHdr1Layout.createSequentialGroup()
                                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtngay_kb, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtma_kbac, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtkhct, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtso_tkco, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtma_khoan, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtky_thue, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnNNT, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlHdr1Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(txtInfoNNT, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHdr1Layout.setVerticalGroup(
            pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHdr1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbltran_no, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txttran_no, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtngay_kb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtma_cqthu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtma_kbac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtso_ctu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtkhct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtngay_kbac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtso_tkno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtso_tkco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtma_chuong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtma_khoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(txtky_thue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboma_tmuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtso_tien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(pnlHdr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNNT)
                    .addComponent(jLabel17)
                    .addComponent(txtInfoNNT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pnlHdr.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnlHdr.border.title"))); // NOI18N
        pnlHdr.setName("pnlHdr"); // NOI18N

        cboHdr.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Msg_RefID", "Original_Code", "Msg_ID" }));
        cboHdr.setName("cboHdr"); // NOI18N

        txtValHdr.setText(resourceMap.getString("txtValHdr.text")); // NOI18N
        txtValHdr.setName("txtValHdr"); // NOI18N

        javax.swing.GroupLayout pnlHdrLayout = new javax.swing.GroupLayout(pnlHdr);
        pnlHdr.setLayout(pnlHdrLayout);
        pnlHdrLayout.setHorizontalGroup(
            pnlHdrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHdrLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(cboHdr, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtValHdr, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );
        pnlHdrLayout.setVerticalGroup(
            pnlHdrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHdrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cboHdr, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtValHdr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnGetFile.setAction(actionMap.get("getPartFile")); // NOI18N
        btnGetFile.setText(resourceMap.getString("btnGetFile.text")); // NOI18N
        btnGetFile.setName("btnGetFile"); // NOI18N

        txtMdfFile.setText(resourceMap.getString("txtMdfFile.text")); // NOI18N
        txtMdfFile.setName("txtMdfFile"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        btnModify.setAction(actionMap.get("saveXML")); // NOI18N
        btnModify.setText(resourceMap.getString("btnModify.text")); // NOI18N
        btnModify.setName("btnModify"); // NOI18N

        btnClose.setAction(actionMap.get("quit")); // NOI18N
        btnClose.setText(resourceMap.getString("btnClose.text")); // NOI18N
        btnClose.setName("btnClose"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(14, 14, 14)
                                .addComponent(txtMdfFile, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnGetFile, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(pnlHdr, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlHdr1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnModify, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(171, 171, 171))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMdfFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGetFile, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHdr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHdr1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModify)
                    .addComponent(btnClose)))
        );

        jLabel9.getAccessibleContext().setAccessibleName(resourceMap.getString("jLabel9.AccessibleContext.accessibleName")); // NOI18N

        javax.swing.GroupLayout pnlCheckPaymLayout = new javax.swing.GroupLayout(pnlCheckPaym);
        pnlCheckPaym.setLayout(pnlCheckPaymLayout);
        pnlCheckPaymLayout.setHorizontalGroup(
            pnlCheckPaymLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckPaymLayout.createSequentialGroup()
                .addGroup(pnlCheckPaymLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCheckPaymLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCheckPaymLayout.createSequentialGroup()
                        .addGap(337, 337, 337)
                        .addComponent(lblSuc, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pnlCheckPaymLayout.setVerticalGroup(
            pnlCheckPaymLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckPaymLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCheckPaymLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSuc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabPay.addTab(resourceMap.getString("pnlCheckPaym.TabConstraints.tabTitle"), pnlCheckPaym); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        btnCheckXML.setAction(actionMap.get("InfoTreasuryPaym")); // NOI18N
        btnCheckXML.setText(resourceMap.getString("btnCheckXML.text")); // NOI18N
        btnCheckXML.setName("btnCheckXML"); // NOI18N

        jButton1.setAction(actionMap.get("quit")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        lblSuc1.setFont(resourceMap.getFont("lblSuc1.font")); // NOI18N
        lblSuc1.setName("lblSuc1"); // NOI18N

        btnChkXML.setAction(actionMap.get("checkXML")); // NOI18N
        btnChkXML.setText(resourceMap.getString("btnChkXML.text")); // NOI18N
        btnChkXML.setName("btnChkXML"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        txtScandFolder.setText(resourceMap.getString("txtScandFolder.text")); // NOI18N
        txtScandFolder.setName("txtScandFolder"); // NOI18N

        btnSndFld.setAction(actionMap.get("PartScandFolder")); // NOI18N
        btnSndFld.setText(resourceMap.getString("btnSndFld.text")); // NOI18N
        btnSndFld.setName("btnSndFld"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setToolTipText(resourceMap.getString("jLabel19.toolTipText")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        txtFileLog.setText(resourceMap.getString("txtFileLog.text")); // NOI18N
        txtFileLog.setName("txtFileLog"); // NOI18N

        btnLogFile.setAction(actionMap.get("PartLogFile")); // NOI18N
        btnLogFile.setText(resourceMap.getString("btnLogFile.text")); // NOI18N
        btnLogFile.setName("btnLogFile"); // NOI18N

        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        txtErrFolder.setText(resourceMap.getString("txtErrFolder.text")); // NOI18N
        txtErrFolder.setName("txtErrFolder"); // NOI18N

        btnErrFld.setAction(actionMap.get("PartPaymErr")); // NOI18N
        btnErrFld.setText(resourceMap.getString("btnErrFld.text")); // NOI18N
        btnErrFld.setName("btnErrFld"); // NOI18N

        btnRad.add(radXLSX);
        radXLSX.setSelected(true);
        radXLSX.setText(resourceMap.getString("radXLSX.text")); // NOI18N
        radXLSX.setName("radXLSX"); // NOI18N

        btnRad.add(radXLS);
        radXLS.setText(resourceMap.getString("radXLS.text")); // NOI18N
        radXLS.setName("radXLS"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel26)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(radXLSX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radXLS))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFileLog, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                            .addComponent(txtErrFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                            .addComponent(txtScandFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSndFld, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLogFile, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnErrFld, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtScandFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(btnSndFld))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtErrFolder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnErrFld, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtFileLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogFile, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radXLSX)
                    .addComponent(radXLS)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setAction(actionMap.get("setSerializable")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setAction(actionMap.get("getDeserilizable")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSuc1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(btnChkXML, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCheckXML, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSuc1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckXML, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnChkXML))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(99, 99, 99))
        );

        tabPay.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jLabel23.setText(resourceMap.getString("lblFolder.text")); // NOI18N
        jLabel23.setName("lblFolder"); // NOI18N

        txtInforTK.setText(resourceMap.getString("txtInforTK.text")); // NOI18N
        txtInforTK.setName("txtInforTK"); // NOI18N

        btnTKDulieu.setAction(actionMap.get("PartFolderInFTK")); // NOI18N
        btnTKDulieu.setText(resourceMap.getString("btnTKDulieu.text")); // NOI18N
        btnTKDulieu.setName("btnTKDulieu"); // NOI18N

        btnTK.setAction(actionMap.get("createDataTK")); // NOI18N
        btnTK.setText(resourceMap.getString("btnTK.text")); // NOI18N
        btnTK.setName("btnTK"); // NOI18N

        lblMessTK.setFont(resourceMap.getFont("lblMessTK.font")); // NOI18N
        lblMessTK.setName("lblMessTK"); // NOI18N

        btnCloseTK.setAction(actionMap.get("quit")); // NOI18N
        btnCloseTK.setText(resourceMap.getString("btnCloseTK.text")); // NOI18N
        btnCloseTK.setName("btnCloseTK"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        txtTKCreate.setText(resourceMap.getString("txtTKCreate.text")); // NOI18N
        txtTKCreate.setName("txtTKCreate"); // NOI18N

        btnCreateDT.setAction(actionMap.get("PartFolderTK")); // NOI18N
        btnCreateDT.setText(resourceMap.getString("btnCreateDT.text")); // NOI18N
        btnCreateDT.setName("btnCreateDT"); // NOI18N

        jLabel24.setFont(resourceMap.getFont("jLabel24.font")); // NOI18N
        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMessTK, javax.swing.GroupLayout.PREFERRED_SIZE, 767, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtTKCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCreateDT))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtInforTK, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addComponent(btnTK)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnCloseTK, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTKDulieu))))
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCreateDT)
                    .addComponent(txtTKCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnTKDulieu)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtInforTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)))
                .addGap(18, 18, 18)
                .addComponent(lblMessTK, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTK)
                    .addComponent(btnCloseTK))
                .addContainerGap(248, Short.MAX_VALUE))
        );

        tabPay.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 969, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 799, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckXML;
    private javax.swing.JButton btnChkXML;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCloseTK;
    private javax.swing.JButton btnCreateDT;
    private javax.swing.JButton btnErrFld;
    private javax.swing.JButton btnGetFile;
    private javax.swing.JButton btnGetSrcFolder;
    private javax.swing.JButton btnLogFile;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnModifyTMuc;
    private javax.swing.JButton btnNNT;
    private javax.swing.ButtonGroup btnRad;
    private javax.swing.JButton btnSndFld;
    private javax.swing.JButton btnTK;
    private javax.swing.JButton btnTKDulieu;
    private javax.swing.JComboBox cboHdr;
    private javax.swing.JComboBox cboma_tmuc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    public static final javax.swing.JLabel lblMessTK = new javax.swing.JLabel();
    private javax.swing.JLabel lblSuc;
    private javax.swing.JLabel lblSuc1;
    private javax.swing.JLabel lbltran_no;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel pnlCheckPaym;
    private javax.swing.JPanel pnlHdr;
    private javax.swing.JPanel pnlHdr1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JRadioButton radXLS;
    private javax.swing.JRadioButton radXLSX;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPay;
    private javax.swing.JTextField txtErrFolder;
    private javax.swing.JTextField txtFileLog;
    private javax.swing.JTextField txtInfoNNT;
    private javax.swing.JTextField txtInforTK;
    private javax.swing.JTextField txtMdfFile;
    private javax.swing.JTextField txtScandFolder;
    private javax.swing.JTextField txtSourFolder;
    private javax.swing.JTextField txtTKCreate;
    private javax.swing.JTextField txtValHdr;
    private javax.swing.JTextField txtkhct;
    private javax.swing.JTextField txtky_thue;
    private javax.swing.JTextField txtma_chuong;
    private javax.swing.JTextField txtma_cqthu;
    private javax.swing.JTextField txtma_kbac;
    private javax.swing.JTextField txtma_khoan;
    private javax.swing.JTextField txtngay_kb;
    private javax.swing.JTextField txtngay_kbac;
    private javax.swing.JTextField txtso_ctu;
    private javax.swing.JTextField txtso_tien;
    private javax.swing.JTextField txtso_tkco;
    private javax.swing.JTextField txtso_tkno;
    private javax.swing.JTextField txttran_no;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    JFileChooser fc = new JFileChooser();
    private JDialog aboutBox;
    //Thông tin mẫu tờ khai thứ 1
    private StringBuffer mautk1_1 = new StringBuffer();
    private StringBuffer mautk1_2 = new StringBuffer();
    private StringBuffer mautk1_3 = new StringBuffer();
    //Thông tin mẫu tờ khai thứ 2
    private StringBuffer mautk2_1 = new StringBuffer();
    private StringBuffer mautk2_2 = new StringBuffer();
    private StringBuffer mautk2_3 = new StringBuffer();
}
