package com.android.miniproject.teacherkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Classe;

import java.util.List;

/**
 * Created by DELL PC on 23/12/2016.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<Classe> {
    private Context activity;
    private List<Classe> data;
    private LayoutInflater  inflater;
    Classe tempValues=null;
    public CustomSpinnerAdapter(Context context, int resource, List<Classe> objects) {
        super(context, resource, objects);
        activity = context;
        data     = objects;

         inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.custom_spinner_item, parent, false);
        tempValues = null;
        tempValues = (Classe) data.get(position);

        TextView label        = (TextView)row.findViewById(R.id.classe_name_spinner);
        TextView sub          = (TextView)row.findViewById(R.id.classe_detail_spinner);

            // Set values for spinner each row
            label.setText(tempValues.getMatricule());
            sub.setText(tempValues.getMatiere());

        return row ;
    }

}
