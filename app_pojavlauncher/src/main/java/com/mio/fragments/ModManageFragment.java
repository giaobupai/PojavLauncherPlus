package com.mio.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mio.ModManageAdapter;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModManageFragment extends Fragment {
    public static final String TAG = "ModManageFragment";

    private ProgressDialog progressDialog;

    private Spinner versionSpinner;
    private ListView listView;
    private ModManageAdapter modManageAdapter;
    private List<File> fileList;

    public ModManageFragment() {
        super(R.layout.fragment_mio_plus_mod_manage);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        versionSpinner = view.findViewById(R.id.mod_manage_version_spinner);
        listView = view.findViewById(R.id.mod_manage_listview);

        List<String> list = new ArrayList<>();
        list.add("公用目录");
        list.addAll(Arrays.asList(Objects.requireNonNull(new File(Tools.DIR_HOME_VERSION).list())));

        ArrayAdapter<String> versionSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, list);
        versionSpinner.setAdapter(versionSpinnerAdapter);
        versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileList.clear();
                File[] files;
                if (list.get(i).equals("公用目录")) {
                    files = new File(Tools.DIR_GAME_NEW + "/mods").listFiles();
                } else {
                    files = new File(Tools.DIR_HOME_VERSION + "/" + list.get(i) + "/mods").listFiles();
                }
                if (Objects.isNull(files)) {
                    files = new File[0];
                    Toast.makeText(requireContext(), "当前所选择版本无mod", Toast.LENGTH_SHORT).show();
                }
                fileList.addAll(Arrays.asList(files));

                modManageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fileList = new ArrayList<>();
        File[] files = new File(Tools.DIR_HOME_VERSION).listFiles();
        if (Objects.isNull(files)) {
            files = new File[0];
            Toast.makeText(requireContext(), "当前所选择版本无mod", Toast.LENGTH_SHORT).show();
        }
        fileList.addAll(Arrays.asList(files));
        modManageAdapter = new ModManageAdapter(requireContext(), fileList);
        listView.setAdapter(modManageAdapter);
    }

}
