package com.mio;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
