package com.mio.mod.curseforge;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CurseforgeAPI {
    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = "$2a$10$qqJ3zZFG5CDsVHk8eV5ft.2ywg2edBtHwS3gzFnw7CDe3X2cKpWZG";

    public CurseforgeAPI() {

    }

    public String getCategory() {
        return httpGet(PREFIX + "/v1/categories?gameId=432");
    }

    public List<CurseAddon.Data> searchMod(String name, boolean hasName) {
        String json = httpGet(PREFIX + "/v1/mods/search?gameId=432&classId=6&sortField=2&sortOrder=desc" + (hasName ? ("&searchFilter=" + name) : ""));
        if (json == null) {
            return null;
        }
        CurseAddon addon = new Gson().fromJson(json, CurseAddon.class);
        List<CurseAddon.Data> dataList = addon.getData();
        return dataList;
    }

    public CurseAddon.Data getModByID(int id) {
        /// v1/mods/{modId}
        try {
            String json = httpGet(PREFIX + "/v1/mods/" + id);
            JSONObject jsonObject = new JSONObject(json);
            CurseAddon.Data data = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), CurseAddon.Data.class);
            return data;
        } catch (Exception e) {

        }
        return null;
    }

    public void getFeaturedMods() {
        ///v1/mods/featured
    }

    public String getDownloadUrl(int modid,int fileid){
//        /v1/mods/{modId}/files/{fileId}/download-url
        try {
            String json = httpGet(PREFIX + "/v1/mods/"+modid+"/files/"+fileid+"/download-url");
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("data");
        } catch (Exception e) {
            Log.e("测试",e.toString());
        }
        return null;
    }

    private String httpGet(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-API-KEY", apiKey);
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            return response.toString();
        } catch (Exception e) {
            Log.e("测试",e.toString());
        }
        return null;
    }
}
