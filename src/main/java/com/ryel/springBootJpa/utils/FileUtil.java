package com.ryel.springBootJpa.utils;



import com.ryel.springBootJpa.utils.bean.FileBo;
import org.apache.commons.io.FileUtils;


import java.io.*;


/**
 * 文件操作工具类
 * Created by wangbin on 2016/6/27.
 */
public class FileUtil {
    public static String getFileExt(String fileName){
        int index=fileName.lastIndexOf('.');
        if(index==-1){
            return "";
        }
        String ext=fileName.substring(index);
        return ext;
    }
    /**
     * 判断一个文件是不是图片后缀
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName){
        return fileName.matches("(?i).+?\\.(jpg|gif|bmp|jpeg|png)");
    }
    public static FileBo save(String originalFileName, InputStream inputStream, String path, String fileName)throws IOException{
        String ext = getFileExt(originalFileName);
        if(isBlank(fileName)){
            fileName = originalFileName;
        }else{
            fileName = fileName+ext;
        }
        File lastFile = new File(path,fileName);
        FileUtils.copyInputStreamToFile(inputStream,lastFile);
        FileBo fileBo = new FileBo(lastFile,fileName,path,ext);
        return fileBo;
    }
    public static String readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            StringBuffer sb = new StringBuffer();
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r'&&((char) tempchar) != '\n') {
                    sb.append(String.valueOf((char) tempchar));
                }
            }
            reader.close();
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}

