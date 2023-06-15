package com.mio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.kdt.pojavlaunch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModManageAdapter extends BaseAdapter {

    private final Context context;
    private List<File> fileList;
    private List<String> infoList;

    public ModManageAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
        infoList=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_manage, parent, false);
            holder = new ViewHolder();
            holder.modName = convertView.findViewById(R.id.item_mod_name);
            holder.infoText = convertView.findViewById(R.id.item_mod_info);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.modName.setText(fileList.get(position).getName());
//        holder.infoText.setText(MioUtils.getModInfo(fileList.get(position).getAbsolutePath()));
        holder.checkBox.setChecked(!fileList.get(position).getAbsolutePath().endsWith(".disable"));
        holder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            File file=fileList.get(position);
            if (checked){
                if (file.getName().endsWith(".disable")){
                    file.renameTo(new File(file.getAbsolutePath().replace(".disable","")));
                }
            } else {
                file.renameTo(new File(file.getAbsolutePath()+".disable"));
            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

//    private void refrshInfo(){
//        new Thread(()->{
//            infoList=new ArrayList<>();
//            for (File file:fileList){
//                String info=MioUtils.getModInfo(file.getAbsolutePath());
//                if (Objects.isNull(info)){
//                    infoList.add("未知");
//                } else {
//                    infoList.add(info);
//                }
//            }
//        }).start();
//    }
    static class ViewHolder {
        TextView modName;
        TextView infoText;
        CheckBox checkBox;
    }
}
