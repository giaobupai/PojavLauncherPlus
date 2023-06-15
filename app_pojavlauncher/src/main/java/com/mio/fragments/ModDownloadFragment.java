package com.mio.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;

public class ModDownloadFragment extends Fragment {
    public static final String TAG = "ModDownloadFragment";

    private ProgressDialog progressDialog;

    public ModDownloadFragment() {
        super(R.layout.fragment_mio_plus_mod_download);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
