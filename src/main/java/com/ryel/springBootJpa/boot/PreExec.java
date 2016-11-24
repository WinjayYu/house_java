package com.ryel.springBootJpa.boot;

import java.io.*;

/**
 * maven打包时候执行方法
 * Created by Administrator on 2016/1/28.
 */
public class PreExec {




    public static void main(String[] args)throws Exception {


        String baseDir = args[0];
        String sampleUrl = args[1];


        //替换docJs的配置文件

            initApiDocTask(baseDir,sampleUrl);
    }


    public static void initApiDocTask(String baseDir, String url)throws Exception {
        File file = new File(baseDir,"/apidoc.json");
        String stcUrlLine = "\"url\":";
        String stcSampleUrlLine = "\"sampleUrl\":";
        String replaceUrlStr = "  \"url\": \""+url+"\",";
        String replaceSampleUrlStr = "  \"sampleUrl\": \""+url+"\",";
        replaceFileLine(file,stcUrlLine,replaceUrlStr);
        replaceFileLine(file,stcSampleUrlLine,replaceSampleUrlStr);

        System.out.println(baseDir+"/generateapi.bat");
        if(!isWindows()){
            return;
        }
        Process process =  Runtime.getRuntime().exec(baseDir+"\\generateapi.bat");

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
        // 读取一行，存储于字符串列表中
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            line = line.trim();
            // do something here
            System.out.println(line);
        }


    }




    public static void replaceFileLine(File file, String srcLine, String desLine)throws Exception {
        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();

        // 替换
        String line = null;

        while ( (line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            //line = line.replaceAll(srcStr, replaceStr);
            if(line.contains(srcLine)){
                line=desLine;
            }

            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
        }

        // 关闭 输入流
        bufIn.close();
        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
    }


    public static boolean isWindows() {
        boolean flag = false;
        if (System.getProperties().getProperty("os.name").toUpperCase()
                .indexOf("WINDOWS") != -1) {
            flag = true;
        }
        return flag;
    }
}
