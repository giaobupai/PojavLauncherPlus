package com.mio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mio.mod.curseforge.CurseAddon;

import net.kdt.pojavlaunch.R;

import java.util.List;

public class ModVersionAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> versionList;
    private List<List<CurseAddon.Data.LatestFilesIndexes>> modList;

    public ModVersionAdapter(Context context,List<String> versionList,List<List<CurseAddon.Data.LatestFilesIndexes>> modList){
        this.context=context;
        this.versionList=versionList;
        this.modList=modList;
    }
    @Override
    public int getGroupCount() {
        return versionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return modList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return versionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return modList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_download_version,parent,false);
        TextView textView = convertView.findViewById(R.id.item_mod_download_version_text);
        textView.setText(versionList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_download_version_mod,parent,false);
        TextView textView = convertView.findViewById(R.id.item_mod_download_version_mod_text);
        textView.setText(modList.get(groupPosition).get(childPosition).getFilename());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
