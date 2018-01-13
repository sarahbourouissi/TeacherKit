package com.android.miniproject.teacherkit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL PC on 26/12/2016.
 */
public class RecycleradapterEtudiantSeance extends RecyclerView.Adapter<RecycleradapterEtudiantSeance.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    private static List<Etudiant> mDataSett;
    private static Context mContext;
        private static seance seance ;

    public RecycleradapterEtudiantSeance(List<Etudiant> mDataSet, Context mContext, seance seance) {
        this.mDataSett = mDataSet;
        this.mContext=mContext;
        this.seance=seance;
       }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNom ,textViewprenom,textAbsent,textPresent,textRetard;
        public CircularImageView image;
        ImageView overflow;

        public ViewHolder(View v) {
            super(v);

             textAbsent = (TextView) v.findViewById(R.id.absent);
            textPresent = (TextView) v.findViewById(R.id.present);
            textRetard = (TextView) v.findViewById(R.id.retard);
            textViewNom = (TextView) v.findViewById(R.id.etudiant_nom_seance);
            textViewprenom = (TextView) v.findViewById(R.id.etudiant_prenom_seance);
            image =(CircularImageView) v.findViewById(R.id.imgetudiant);
            overflow = (ImageView) v.findViewById(R.id.overflowS);

            textAbsent.setVisibility(View.INVISIBLE);
            textPresent.setVisibility(View.INVISIBLE);
            textRetard.setVisibility(View.INVISIBLE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((textPresent.getVisibility() == View.INVISIBLE) && (textAbsent.getVisibility()==View.INVISIBLE) && (textRetard.getVisibility() == View.INVISIBLE) ){
                        textPresent.setVisibility(View.VISIBLE);
                        AjoutAbs();
                    }else if ((textPresent.getVisibility() == View.VISIBLE) && (textAbsent.getVisibility()==View.INVISIBLE) && (textRetard.getVisibility() == View.INVISIBLE)){
                        textPresent.setVisibility(View.INVISIBLE);
                        textAbsent.setVisibility(View.VISIBLE);
                        UpdateAbs("A");

                    }else if ((textPresent.getVisibility() == View.INVISIBLE) && (textAbsent.getVisibility()==View.VISIBLE) && (textRetard.getVisibility() == View.INVISIBLE)){
                       textAbsent.setVisibility(View.INVISIBLE);
                        textRetard.setVisibility(View.VISIBLE);
                        UpdateAbs("R");
                    }else {
                        textPresent.setVisibility(View.VISIBLE);
                        textRetard.setVisibility(View.INVISIBLE);
                        textAbsent.setVisibility(View.INVISIBLE);
                        UpdateAbs("P");
                    }

                }
            });
        }

        private void UpdateAbs(final String a) {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_UPDATEABS, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Abs Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            Toast.makeText(mContext,"yess",Toast.LENGTH_SHORT).show();
                        } else {

                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(mContext,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(mContext, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(mContext,
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("absence", a);
                    params.put("id_seance", ""+seance.getId());
                    params.put("id_etudiant", ""+mDataSett.get(getAdapterPosition()).getId());


                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, "update abs");

        }

        private void AjoutAbs() {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_AJOUTABS, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Abs Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            Toast.makeText(mContext,"yess",Toast.LENGTH_SHORT).show();
                                    } else {

                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(mContext,
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(mContext, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(mContext,
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_seance", ""+seance.getId());
                    params.put("id_etudiant", ""+mDataSett.get(getAdapterPosition()).getId());
                    params.put("absence", "P");

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, "etudiant present");

        }

        public TextView getTextViewNom() {
            return textViewNom;
        }
        public TextView getTextViewPrenom() {
            return textViewprenom;
        }
        public  CircularImageView getImageView(){ return image;}

    }
    @Override
    public RecycleradapterEtudiantSeance.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.etudiants_seance_card, viewGroup, false);

        return new RecycleradapterEtudiantSeance.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println(mDataSett.get(position).getNom());
        Log.d(TAG, "matricule" + mDataSett.get(position) + " set.");
        // Log.d(TAG, "matricule" + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        System.out.println(mDataSett.get(position).getNom());
        System.out.println(mDataSett.get(position).getPrenom());
        System.out.println(mDataSett.get(position).getNom() + "aaaaaaaaaa");
        // loading album cover using Glide library
        // Glide.with(mContext).load(mDataSett.get(position).getUrl_img()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.getImageView()) {

        try {


            Glide.with(mContext).load(mDataSett.get(position).getUrl_img()).dontAnimate().placeholder(R.drawable.avatar).into(holder.getImageView());

            holder.getImageView().setBorderWidth(15);
            holder.getImageView().addShadow();

        } catch (Exception e) {
            e.printStackTrace();
        }



        holder.getTextViewNom().setText( mDataSett.get(position).getNom());
        holder.getTextViewPrenom().setText( mDataSett.get(position).getPrenom());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, seance,mDataSett.get(position));
            }
        });


    }





    @Override
    public int getItemCount() {
        return mDataSett.size();
    }
    private void showPopupMenu(View view,seance s,Etudiant e ) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_seance, popup.getMenu());
        popup.setOnMenuItemClickListener(new RecycleradapterEtudiantSeance.MyMenuItemClickListener(s,e));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
seance s ;
        Etudiant etudiant;
        public MyMenuItemClickListener(seance s,Etudiant e) {
            this.s=s;
            etudiant =e;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.ajouter_comortement_seance:
                    Intent intent = new Intent(mContext,AjoutComportement.class);
                    intent.putExtra("etudiant",(Serializable) etudiant);
                    intent.putExtra("seance", (Serializable) s);
                    mContext.startActivity(intent);


                    return true;

                default:
            }
            return false;
        }
    }


}