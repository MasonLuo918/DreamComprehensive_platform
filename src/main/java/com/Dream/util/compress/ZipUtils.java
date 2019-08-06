package com.Dream.util.compress;

import org.apache.tools.zip.ZipUtil;

import java.io.*;
import java.util.zip.*;

public class ZipUtils {
    public static final String EXT = ".zip";

    private static final String BASE_DIR = "";

    private static final String PATH = File.separator;

    private static final int BUFFER = 1024;

    /**
     * 将文件进行压缩
     * 压缩后的文件放在同级目录下
     * @param srcFile
     * @throws Exception
     */
    public static void compress(File srcFile) throws Exception{
        String name = srcFile.getName();
        String basePath = srcFile.getParent();
        String destPath = basePath + File.separator + name + EXT;
        compress(srcFile, destPath);
    }

    /**
     * 将压缩文件添加到destFile文件中
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void compress(File srcFile, File destFile) throws Exception{
        FileOutputStream fos = new FileOutputStream(destFile);
        // Zip的输出流
        ZipOutputStream zos = new ZipOutputStream(fos);
        // 真正进行压缩的操作
        compress(srcFile, zos, BASE_DIR);
        zos.flush();
        zos.close();
    }

    /**
     * 将文件输出到制定destPath文件中(带有.zip)
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void compress(File srcFile, String destPath) throws Exception{
        compress(srcFile, new File(destPath));
    }

    /**
     * 进行文件压缩，并且判断是文件还是文件夹，根据不同的类型进行压缩
     * 并且利用其进行操作
     * @param srcFile
     * @param zos
     * @param basePath
     * @throws Exception
     */
    private static void compress(File srcFile, ZipOutputStream zos,
                                 String basePath) throws Exception{
        if(srcFile.isDirectory()){
            compressDir(srcFile,zos,basePath);
        }else{
            compressFile(srcFile,zos,basePath);
        }
    }

    /**
     * 对源文件路径的文件进行压缩
     * @param srcPath
     * @throws Exception
     */
    public static void compress(String srcPath) throws Exception{
        File srcFile = new File(srcPath);

        compress(srcFile);
    }

    public static void compress(String srcPath, String destPath)
            throws Exception {
        File srcFile = new File(srcPath);

        compress(srcFile, destPath);
    }

    /**
     * 对文件夹里面的内容进行压缩，并且递归压缩该文件夹
     * @param dir
     * @param zos
     * @param basePath
     * @throws Exception
     */
    private static void compressDir(File dir, ZipOutputStream zos,
                                    String basePath) throws Exception{
        File[] files = dir.listFiles();

        // 构建空目录
        if(files.length < 1){
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);
            zos.putNextEntry(entry);
            zos.closeEntry();
        }
        for(File file: files){
            //递归压缩
            compress(file, zos, basePath + dir.getName() + PATH);
        }
    }

    /**
     * 压缩一个文件
     * @param file
     * @param zos
     * @param dir 文件的上层目录
     * @throws Exception
     */
    private static void compressFile(File file, ZipOutputStream zos,
                                     String dir) throws Exception{

        /**
         * 压缩包内文件名定义
         *
         * <pre>
         * 如果有多级目录，那么这里就需要给出包含目录的文件名
         * 如果用WinRAR打开压缩包，中文名将显示为乱码
         * </pre>
         */
        ZipEntry entry = new ZipEntry(dir + file.getName());
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int count = 0;
        byte data[] = new byte[BUFFER];
        while((count = bis.read(data, 0, BUFFER)) != -1){
            zos.write(data, 0, count);
        }
        bis.close();
        zos.closeEntry();
    }

    /**
     * 将一个路径的文件转换成文件
     * 解压缩
     * @param srcPath
     * @throws Exception
     */
    public static void decompress(String srcPath) throws Exception{
        File srcFile = new File(srcPath);
        decompress(srcFile);
    }

    /**
     * 压缩一个文件（目录）
     * 到同级的目录下
     * @param srcFile
     * @throws Exception
     */
    public static void decompress(File srcFile) throws Exception{
        // 获取需解压文件的父目录,将当前文件解压到同级目录中
        String basePath = srcFile.getParent();
        decompress(srcFile, basePath);
    }

    /**
     * 将srcFile解压缩到destFile中
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void decompress(File srcFile, File destFile) throws Exception{
        // 创建一个Zip输入流,将文件解压到destFile中
        ZipInputStream zis = new ZipInputStream(new
                FileInputStream(srcFile));
        decompress(destFile, zis);
        zis.close();
    }

    /**
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void decompress(File srcFile, String destPath) throws Exception{
        decompress(srcFile, new File(destPath));
    }

    /**
     * @param srcPath
     * @param destPath
     * @throws Exception
     */
    public static void decompress(String srcPath, String destPath) throws Exception{
        File srcFile = new File(srcPath);
        decompress(srcFile, destPath);
    }

    public static void decompress(File destFile, ZipInputStream zis) throws Exception{
        ZipEntry entry = null;

        while((entry = zis.getNextEntry()) != null){
            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            //文件检查
            fileProber(dirFile);

            if(entry.isDirectory()){
                dirFile.mkdirs();
            }else{
                decompressFile(dirFile, zis);
            }
            zis.closeEntry();
        }
    }

    /**
     * 当父目录不存在的时候，创建目录
     * @param dirFile
     */
    private static void fileProber(File dirFile){
        File parentFile = dirFile.getParentFile();
        if(!parentFile.exists()){
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    /**
     * 解压缩单个文件
     * @param destFile
     * @param zis
     * @throws Exception
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws Exception{
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile)
        );
        int count;
        byte data[] = new byte[BUFFER];
        while((count = zis.read(data, 0, BUFFER)) != -1){
            bos.write(data, 0, count);
        }
        bos.close();
    }

    public static void main(String[] args) throws Exception {
        decompress("/Users/belle/Desktop/material.zip");
    }
}
