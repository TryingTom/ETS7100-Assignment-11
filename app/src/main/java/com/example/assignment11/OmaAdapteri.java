package com.example.assignment11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OmaAdapteri extends ArrayAdapter {
    private ArrayList<String> lista;
    private Context context;

    public OmaAdapteri(@NonNull Context context, int resource, @NonNull ArrayList<String> lista) {
        super(context, resource, (List<String>) lista);
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            View v = LayoutInflater.from(context).inflate(R.layout.adapter_layout, parent,false);
            TextView textView = v.findViewById(R.id.adapterText);
            final CheckBox checkBox = v.findViewById(R.id.adapterBox);

            textView.setText(lista.get(position));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // tästä voisi tehdä jotain.
                }
            });

            convertView = v;
        }

        return convertView;
    }
}
