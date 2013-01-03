/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.ziper;

import com.support.pit.paym.SupportTreasuryPaymApp;
import com.support.pit.paym.SupportTreasuryPaymView;
import com.support.pit.system.Message;
import com.support.pit.utility.GetFileFTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Administrator
 */
public class FolderZiper {

    static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;

        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);

        addFolderToZip("", srcFolder, zip);
        zip.flush();
        zip.close();
    }

    public static void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
        }
    }

    public static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
        File folder = new File(srcFolder);

        for (String fileName : folder.list()) {
            if (path.equals("")) {
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            } else {
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
            }
        }
    }

    public static void backupFolderFTP(String folderCopy, File fileBackup) {
        //Connection ftp
        FTPClient client = GetFileFTP.ftpConnection();

        FileOutputStream fos = null;
        try {
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            //Load all file on ftp -> local directory
            String[] names = client.listNames();

            for (String name : names) {

                fos = new FileOutputStream(folderCopy + name);
                //Download file on ftp
                client.retrieveFile(name, fos);
                //Delete file on ftp
                client.deleteFile(name);
            }

            //Zip folder
            zipFolder(folderCopy, fileBackup.getAbsolutePath().toString());

            //upload file -> ftp   
            client.changeWorkingDirectory(SupportTreasuryPaymApp.prop.getProperty("ftp.WorkingDirectoryBackup"));
            FileInputStream fis = new FileInputStream(fileBackup.getAbsolutePath());

            client.storeFile(fileBackup.getName(), fis);
            fis.close();

            //delete file backup on local        
            File file = new File(fileBackup.getAbsolutePath());
            file.delete();

            client.logout();
            
        } catch (Exception e) {
            SupportTreasuryPaymView.logger.log(Level.WARNING, Message.ERR_MESS_BACKUP_FTP, e.getMessage());
        }
        
    }

}
