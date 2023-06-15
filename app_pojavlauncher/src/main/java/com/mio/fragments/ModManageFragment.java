package com.mio.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;

public class ModManageFragment extends Fragment {
    public static final String TAG = "ModManageFragment";

    private ProgressDialog progressDialog;

    public ModManageFragment() {
        super(R.layout.fragment_mio_plus_mod_manage);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
