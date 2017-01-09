package com.ryel.zaja.utils;



import com.ryel.zaja.utils.bean.FileBo;
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

    /**
     * 删除文件，可以是单个文件或文件夹
     * @param   fileName    待删除的文件名
     * @return 文件删除成功返回true,否则返回false
     */
    public static boolean delete(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("删除文件失败："+fileName+"文件不存在");
            return false;
        }else{
            if(file.isFile()){

                return deleteFile(fileName);
            }else{
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   fileName    被删除文件的文件名
     * @return 单个文件删除成功返回true,否则返回false
     */
    public static boolean deleteFile(String fileName){
        File file = new File(fileName);
        if(file.isFile() && file.exists()){
            file.delete();
            System.out.println("删除单个文件"+fileName+"成功！");
            return true;
        }else{
            System.out.println("删除单个文件"+fileName+"失败！");
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   dir 被删除目录的文件路径
     * @return  目录删除成功返回true,否则返回false
     */
    public static boolean deleteDirectory(String dir){
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if(!dir.endsWith(File.separator)){
            dir = dir+File.separator;
        }
        File dirFile = new File(dir);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if(!dirFile.exists() || !dirFile.isDirectory()){
            System.out.println("删除目录失败"+dir+"目录不存在！");
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for(int i=0;i<files.length;i++){
            //删除子文件
            if(files[i].isFile()){
                flag = deleteFile(files[i].getAbsolutePath());
                if(!flag){
                    break;
                }
            }
            //删除子目录
            else{
                flag = deleteDirectory(files[i].getAbsolutePath());
                if(!flag){
                    break;
                }
            }
        }

        if(!flag){
            System.out.println("删除目录失败");
            return false;
        }

        //删除当前目录
        if(dirFile.delete()){
            System.out.println("删除目录"+dir+"成功！");
            return true;
        }else{
            System.out.println("删除目录"+dir+"失败！");
            return false;
        }
    }
}

