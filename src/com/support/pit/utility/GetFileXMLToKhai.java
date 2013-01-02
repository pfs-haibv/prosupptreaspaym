/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Administrator
 */
public class GetFileXMLToKhai {

    /**
     * Copy file từ thư mục đến thư mục
     * @param srcPath
     * @param dstPath
     * @throws IOException 
     */
    public static void copyDirectory(File srcPath, File dstPath) throws IOException {
        // bỏ trường hợp srcPath = dstPath
        if (!srcPath.equals(dstPath)) {
            if (srcPath.isDirectory()) {
                if (!dstPath.exists()) {
                    dstPath.mkdir();
                }

                String files[] = srcPath.list();
                for (int i = 0; i < files.length; i++) {
                    copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
                }
            } else {
                if (!srcPath.exists()) {
                    System.out.println("File or directory does not exist.");
                    System.exit(0);
                } else {
                    InputStream in = new FileInputStream(srcPath);
                    OutputStream out = new FileOutputStream(dstPath);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }

    /**
     * Tìm kiếm và copy file trong thư mục
     * @param srcPath
     * @param dstPath
     * @param find_file
     * @throws IOException 
     */
    public static void copyDirectory(File srcPath, File dstPath, String find_file) throws IOException {
        String[] file_copy = find_file.split(",");
        for (int f = 0; f < file_copy.length; f++) {
            // bỏ trường hợp srcPath = dstPath
            if (!srcPath.equals(dstPath)) {
                if (srcPath.isDirectory()) {
                    if (!dstPath.exists()) {
                        dstPath.mkdir();
                    }

                    String files[] = srcPath.list();
                    for (int i = 0; i < files.length; i++) {
                        //tìm file và thực hiện copy
                        if (files[i].equals(file_copy[f].trim())) {
                            copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
                        }
                    }
                } else {
                    if (!srcPath.exists()) {
                        System.out.println("File or directory does not exist.");
                        System.exit(0);
                    } else {
                        InputStream in = new FileInputStream(srcPath);
                        OutputStream out = new FileOutputStream(dstPath);

                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    }
                }
            }
        }
    }

    /**
     * check xml tờ khai
     * @throws IOException 
     */
    public static void checkXMLToKhai() throws IOException {
        File dirForder = new File("E:/PIT_IMPORT/Data");

        File dirFile;

        for (File f : dirForder.listFiles()) {
            dirFile = new File(dirForder + "/" + f.getName());
            for (File f_ : dirFile.listFiles()) {

                if (f_.getName().indexOf("XML") != -1) {
                    System.out.println(dirFile.getPath() + "\\" + f_.getName());
                    //copy file
                    GetFileXMLToKhai.copyDirectory(new File(dirFile.getPath()), new File("F:\\GetFileXML\\xml"), f_.getName());
                    //move file
                    //Utility.moveFiles(dirFile.getPath()+"\\"+f_.getName(), errFolder);
                }
            }
        }
    }
    /**
     * move file xml
     * @param list_file
     * @param dir_move_file
     * @throws IOException 
     */
    public static void moveFileXMLToKhai(String list_file, String dir_move_file) throws IOException {

        File dirForder = new File("E:/PIT_IMPORT/Data");

        File dirFile;

        for (String l_file : list_file.split(",")) {
            
            for (File f : dirForder.listFiles()) {
                dirFile = new File(dirForder + "/" + f.getName());
                for (File f_ : dirFile.listFiles()) {

                    if (f_.getName().equals(l_file)) {
                        //move file
                        Utility.moveFiles(dirFile.getPath()+"\\"+f_.getName(), dir_move_file);
                    }
                }
            }

        }
    }
    
}
