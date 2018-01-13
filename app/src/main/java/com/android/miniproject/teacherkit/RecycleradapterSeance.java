package com.android.miniproject.teacherkit;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.miniproject.teacherkit.Entity.Abs;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aziz on 12/29/2016.
 */

public class RecycleradapterSeance extends RecyclerView.Adapter<RecycleradapterSeance.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    private static List<Abs> mDataSett;
    private static Context mContext;



    public RecycleradapterSeance(List<Abs> mDataSet, Context mContext) {
        this.mDataSett = mDataSet;
        this.mContext=mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textdateseance ,textnomseance;
        private LinearLayout item ;
        private CardView abscard;


        public ViewHolder(View v) {
            super(v);

            textnomseance = (TextView) v.findViewById(R.id.nom_seance_abs);
            textdateseance = (TextView) v.findViewById(R.id.date_seance_abs);
            item = (LinearLayout) v.findViewById(R.id.linearlayout_abs);
            abscard = (CardView) v.findViewById(R.id.abs_card);

        }




        public CardView getAbscard(){return abscard ;}
        public TextView getTextnomseance() {
            return textnomseance;
        }
        public TextView getTextdateseance() {
            return textdateseance;
        }
        public  LinearLayout getItem(){return item;}

    }
    @Override
    public RecycleradapterSeance.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Create a new view.
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.abs_etudiant_card, viewGroup, false);

        return new RecycleradapterSeance.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        System.out.println(mDataSett.get(position).getId_seance());
        System.out.println(mDataSett.get(position).getId_etudiant());
        System.out.println(mDataSett.get(position).getAbsence());

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_TITRESEANCE+mDataSett.get(position).getId_seance(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("", "Seance Response: " + response.toString());

                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray jarray = jObj.getJSONArray("seance");
                    for (int i =0 ; i< jarray.length();i++){
                        JSONObject classe = jarray.getJSONObject(i);
                        String Mat = classe.getString("titre");
                        String date = classe.getString("date_debut");
                        holder.textnomseance.setText(Mat);
                        holder.textdateseance.setText(date);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", "Login Error: " + error.getMessage());
                // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Matricule seance");



        if(mDataSett.get(position).getAbsence().equals("A")  ){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFF4444"));
        }else if (mDataSett.get(position).getAbsence().equals("P")) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FF99CC00"));
        }else if (mDataSett.get(position).getAbsence().equals("R"))
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFBB33"));
        }
    }





    @Override
    public int getItemCount() {
        return mDataSett.size();
    }


}
