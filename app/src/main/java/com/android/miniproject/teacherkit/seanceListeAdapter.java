package com.android.miniproject.teacherkit;


import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL PC on 13/12/2016.
 */

public class seanceListeAdapter extends ArrayAdapter<seance> {

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private Classe classe ;
    int layoutResourceId;
    List<seance> seances = null;
    ViewHolder holder = null;


    public seanceListeAdapter(Context context, int layoutResourceId, List<seance> seances) {
        super(context, layoutResourceId, seances);
        this.layoutResourceId = layoutResourceId;
        this.seances = seances;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        db = new SQLiteHandler(getContext());
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        row = inflater.inflate(layoutResourceId, parent, false);
        holder = new ViewHolder();
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        holder.delete = (Button) row.findViewById(R.id.delete);
        holder.tvTitre = (TextView) row.findViewById(R.id.tv_title);
        holder.tvstardate = (TextView) row.findViewById(R.id.tv_startdate);
        holder.tvduration = (TextView) row.findViewById(R.id.tv_duration);
        holder.tvclasse = (TextView) row.findViewById(R.id.tv_classe_name);
        final seance seance = seances.get(position);
        holder.tvTitre.setText(seance.getTitre());
        String[] date = seance.getStardate().split("-");
        String[] heure = date[2].split(" ");
        String[] min = heure[1].split(":");
        String[] dateF = seance.getEnddate().split("-");
        String[] heureF = dateF[2].split(" ");
        String[] minF = heureF[1].split(":");

        int durationh = Integer.parseInt(minF[0]) - Integer.parseInt(min[0]) ;
        int durationm = Integer.parseInt(minF[1]) - Integer.parseInt(min[1]) ;
        if (durationm < 0 ){durationm = 0 - durationm ;}
        if (durationh < 0 ){durationh = 0 - durationh ;}


        holder.tvduration.setText("duartion : "+durationh+":"+durationm  );

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_MATRICULECLASSE+seance.getId_classe(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                        JSONObject jObj = new JSONObject(response);
                       // JSONObject user = jObj.getJSONArray("classe");
                        Log.e("classe ",jObj.getString("matricule"));
                        holder.tvclasse.setText(jObj.getString("matricule"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        holder.tvstardate.setText("Start : " + heure[1]);
        row.setTag(holder);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteSeance(seance.getId());
                seances.remove(position);
                seanceListeAdapter.this.notifyDataSetChanged();




            }

            
        });
        AppController.getInstance().addToRequestQueue(strReq, "Matricule Classe");


        return row;
    }

    private void DeleteSeance(final int id) {
                       StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_DELETESEANCE, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Toast.makeText(getContext(), "yess ! ", Toast.LENGTH_LONG).show();
                                EmploisDuTempsFragment fragment =   EmploisDuTempsFragment.newInstance();
                                fragment.LodeEvent();

                            } else {

                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_seance", ""+id);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "supprimer seance");
    }


    public class ViewHolder {

        TextView tvTitre;
        TextView tvstardate;
        TextView tvduration;
        TextView tvclasse;
        Button delete;


    }



}
