package com.mio.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mio.FabricDownload;
import com.mio.ForgeDownload;
import com.mio.OptifineDownload;

import net.kdt.pojavlaunch.JMinecraftVersionList;
import net.kdt.pojavlaunch.JavaGUILauncherActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MioPlusFragment extends Fragment {
    public static final String TAG = "MioPlusFragment";

    private ProgressDialog progressDialog;

    public MioPlusFragment() {
        super(R.layout.fragment_mio_plus);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Button changeDownloadSourceButton = view.findViewById(R.id.change_download_source_button);
        Button downloadForgeButton = view.findViewById(R.id.download_forge_button);
        Button downloadFabricButton = view.findViewById(R.id.download_fabric_button);
        Button downloadOptfineButton = view.findViewById(R.id.download_optfine_button);

        String[] sourceItems = new String[]{"官方源", "BMCLAPI", "MCBBS"};
        changeDownloadSourceButton.setText("切换下载源(当前为" + sourceItems[LauncherPreferences.DEFAULT_PREF.getInt("downloadSource", 0)] + ")");
        changeDownloadSourceButton.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择")
                    .setItems(sourceItems, (d, i) -> {
                        SharedPreferences.Editor editor = LauncherPreferences.DEFAULT_PREF.edit();
                        editor.putInt("downloadSource", i);
                        editor.apply();
                        changeDownloadSourceButton.setText("切换下载源(当前为" + sourceItems[i] + ")");
                    }).setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });
        downloadForgeButton.setOnClickListener(v -> {
            progressDialog.show();
            JMinecraftVersionList versionList = (JMinecraftVersionList) ExtraCore.getValue(ExtraConstants.RELEASE_TABLE);
            List<String> list = new ArrayList<>();
            for (JMinecraftVersionList.Version version : versionList.versions) {
                if (version.type.equals("release")) {
                    list.add(version.id);
                }
            }
            String[] items = list.toArray(new String[0]);
            progressDialog.dismiss();
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择版本")
                    .setItems(items, (dialogInterface, i) -> {
                        progressDialog.show();
                        new Thread(() -> {
                            ForgeDownload forgeDownload = new ForgeDownload(items[i]);
                            List<String> forgeList = forgeDownload.getForgeVersions();
                            String[] forgeItems = forgeList.toArray(new String[0]);
                            Arrays.sort(forgeItems, (o1, o2) -> {
                                if (!isHigher(o1, o2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            });
                            requireActivity().runOnUiThread(() -> {
                                progressDialog.dismiss();
                                AlertDialog tempDialog = new AlertDialog.Builder(requireContext())
                                        .setTitle("请选择Forge版本")
                                        .setItems(forgeItems, (d, j) -> {
                                            download(forgeDownload.getDownloadLink(forgeItems[j]), Tools.DIR_GAME_HOME + "/forge-installer-" + forgeItems[j] + ".jar",true);
                                        })
                                        .setNegativeButton("取消", null)
                                        .create();
                                tempDialog.show();
                            });
                        }).start();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });

        downloadFabricButton.setOnClickListener(v -> {
            progressDialog.show();
            new Thread(() -> {
                FabricDownload fabricDownload = new FabricDownload();
                List<String> fabricList = fabricDownload.getInstallerVersion();
                String[] items = fabricList.toArray(new String[0]);
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    AlertDialog dialog = new AlertDialog.Builder(requireContext())
                            .setTitle("请选择fabric版本")
                            .setItems(items, (dialogInterface, i) -> {
                                download(fabricDownload.getDownloadLink(items[i]), Tools.DIR_GAME_HOME + "/fabric-installer-" + items[i] + ".jar",true);
                            })
                            .setNegativeButton("取消", null)
                            .create();
                    dialog.show();
                });
            }).start();
        });
        downloadOptfineButton.setOnClickListener(v -> {
            JMinecraftVersionList versionList = (JMinecraftVersionList) ExtraCore.getValue(ExtraConstants.RELEASE_TABLE);
            List<String> list = new ArrayList<>();
            for (JMinecraftVersionList.Version version : versionList.versions) {
                if (version.type.equals("release")) {
                    list.add(version.id);
                }
            }
            String[] versions=new File(Tools.DIR_HOME_VERSION).list();
            AlertDialog dialog1=new AlertDialog.Builder(requireContext())
                    .setTitle("请选择高清修复安装的位置")
                    .setItems(versions,(dd,k)->{
                        String[] items = list.toArray(new String[0]);
                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setTitle("请选择版本")
                                .setItems(items, (dialogInterface, i) -> {
                                    progressDialog.show();
                                    new Thread(() -> {
                                        OptifineDownload optifineDownload = new OptifineDownload(items[i]);
                                        List<String> optList = optifineDownload.getVersion();
                                        Collections.reverse(optList);
                                        String[] optItems = optList.toArray(new String[0]);
                                        requireActivity().runOnUiThread(() -> {
                                            progressDialog.dismiss();
                                            AlertDialog tempDialog = new AlertDialog.Builder(requireContext())
                                                    .setTitle("请选择高清修复版本")
                                                    .setItems(optItems, (d, j) -> {
                                                        download(optifineDownload.getDownloadLink(optItems[j]), Tools.DIR_HOME_VERSION+"/" +versions[k]+"/mods/" + "/optfine-" + optItems[j].replace("/", "-") + ".jar",false);
                                                    })
                                                    .setNegativeButton("取消", null)
                                                    .create();
                                            tempDialog.show();
                                        });
                                    }).start();
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        dialog.show();
                    }).setNegativeButton("取消",null)
                    .create();
            dialog1.show();


        });
    }

    private void download(String url, String dest,boolean install) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("下载进度：0%");
        progressDialog.show();
        new Thread(() -> {
            try {
                DownloadUtils.downloadFileMonitored(url, dest, new byte[1024], (curr, max) -> requireActivity().runOnUiThread(() -> {
                    int percent = curr * 100 / max;
                    progressDialog.setTitle("下载进度：" + percent + "%");
                    if (percent == 100) {
                        progressDialog.dismiss();
                        progressDialog = new ProgressDialog(requireContext());
                        if (install){
                            Intent intent = new Intent(requireActivity(), JavaGUILauncherActivity.class);
                            intent.putExtra("modFile", new File(dest));
                            requireActivity().startActivity(intent);
                        } else {
                            requireActivity().runOnUiThread(()->{
                                Toast.makeText(requireContext(),"下载完成" ,Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static boolean isHigher(String version1, String version2) {
        if (version1.equals(version2)) {
            return false;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return true;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return false;
                }
            }
            return false;
        } else {
            return diff > 0 ? true : false;
        }
    }
}
