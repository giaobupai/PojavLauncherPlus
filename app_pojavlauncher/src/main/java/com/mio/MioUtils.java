package com.mio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.mio.fragments.MioPlusFragment;
import com.mio.modpack.CurseforgeModpackManifest;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.Tools;

import org.apache.commons.io.IOUtils;
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
import java.util.List;
import java.util.Map;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
    
    public static void installModPack(Activity activity,String path){
        ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        PojavApplication.sExecutorService.execute(() -> {
            try {
                final File modPackFile = new File(path);
                if(!MioUtils.unZip(modPackFile,modPackFile.getAbsolutePath().replace(".zip","").replace(" ",""))){
                    activity.runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(activity,"解压失败",Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                File manifest=new File(modPackFile.getAbsolutePath().replace(".zip","").replace(" ",""),"manifest.json");
                if (!manifest.exists()){
                    activity.runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(activity,"未找到manifest.json文件",Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                CurseforgeModpackManifest modpackManifest=new Gson().fromJson(Tools.read(manifest.getAbsolutePath()),CurseforgeModpackManifest.class);
                List<CurseforgeModpackManifest.Files> filesList=modpackManifest.getFiles();
                activity.runOnUiThread(()->dialog.dismiss());
                MioDownloadTask mioDownloadTask=new MioDownloadTask(activity, new MioDownloadTask.Feedback() {
                    @Override
                    public void onFinished(Map<String, String> failedFile) {
                        CurseforgeModpackManifest.Minecraft minecraft = modpackManifest.getMinecraft();
                        AlertDialog alertDialog=new AlertDialog.Builder(activity)
                                .setTitle("提示")
                                .setMessage("整合包相关文件已下载完成当前整合包需要MC版本:"+minecraft.getVersion()+"  Mod加载器版本:"+minecraft.getModLoaders().get(0).getId()+"。整合包文件下载位置为"+Tools.DIR_GAME_HOME+"/整合包/整合包名，请手动下载目标MC版本以及Mod加载器再将文件放到正确位置。")
                                .setPositiveButton("确定",(d,i)->{
                                })
                                .create();
                        alertDialog.show();

                    }

                    @Override
                    public void onCancelled() {
                        Toast.makeText(activity,"已取消下载",Toast.LENGTH_LONG).show();
                    }
                });
                mioDownloadTask.setModsPath(manifest.getParent()+"/mods/");
                mioDownloadTask.execute(filesList);

            }catch(IOException e) {
                activity.runOnUiThread(dialog::dismiss);
                Tools.showError(activity, e);
            }
        });
    }
}
