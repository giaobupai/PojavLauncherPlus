package com.mio;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MioUtils {
    public static String getModInfo(String path){
        try{
            InputStream in=new BufferedInputStream(new FileInputStream(path));
            ZipInputStream zin=new ZipInputStream(in);
            ZipFile zf=new ZipFile(path);
            //ZipEntry 类用于表示 ZIP 文件条目。
            ZipEntry ze;
            while((ze=zin.getNextEntry())!=null){
                if(ze.getName().equals("mcmod.info")){

                    BufferedReader br =new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line="";
                    String str="";
                    while ((line =br.readLine()) !=null) {
                        str+=line;
                    }
                    JSONObject jsonobj=new JSONObject(new JSONArray(str).get(0).toString());
                    return jsonobj.getString("name");
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }

    public static boolean unZip(File zipFile, String descDir) {
        boolean flag = false;
        Log.e("测试",descDir);
        if (!descDir.endsWith("/")){
            descDir+="/";
        }
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        try {
            // 指定编码，否则压缩包里面不能有中文目录
            zip = new ZipFile(zipFile, StandardCharsets.UTF_8);
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replace("/",
                        File.separator);
                File file = new File(outPath.substring(0,
                        outPath.lastIndexOf(File.separator)));
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            flag = true;
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
