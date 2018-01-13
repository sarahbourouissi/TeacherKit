package com.android.miniproject.teacherkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.android.miniproject.teacherkit.Entity.Classe;
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
 * Created by user on 12/5/2016.
 */

public class RecycleradapterEtudiant extends RecyclerView.Adapter<RecycleradapterEtudiant.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    private static List<Etudiant> mDataSett;
    private static Context mContext;
    private int id;
    List<String> couleurs;
    ProgressDialog PD;
    Classe classe;



    public RecycleradapterEtudiant(List<Etudiant> mDataSet, Context mContext, List<String> couleurs, Classe classe) {
        this.mDataSett = mDataSet;
        this.mContext=mContext;
        this.couleurs=couleurs;
        this.classe=classe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView textViewNom ,textViewprenom;
        public ImageView  overflow;
        public CircularImageView image;


        public ViewHolder(View v) {
            super(v);


            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    Intent intent = new Intent( mContext, etudiant_detail.class);
                    System.out.println(mDataSett.get(getAdapterPosition()).getPrenom());
        intent.putExtra("etudiant",(Serializable) mDataSett.get(getAdapterPosition()));
                    intent.putExtra("classe",classe);
                 //   intent.putExtra("classe",);

                    mContext.startActivity(intent);






                }
            });

           textViewNom = (TextView) v.findViewById(R.id.etudiant_nom);
            textViewprenom = (TextView) v.findViewById(R.id.etudiant_prenom);
            image =(CircularImageView) v.findViewById(R.id.img);
            overflow = (ImageView) v.findViewById(R.id.overflow);
        }

        public TextView getTextViewNom() {
            return textViewNom;
        }
        public TextView getTextViewPrenom() {
            return textViewprenom;
        }
        public CircularImageView getImageView(){ return image;}

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.etudiants_card, viewGroup, false);
        PD = new ProgressDialog(mContext);
        PD.setMessage("please wait.....");
        PD.setCancelable(false);

        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        System.out.println(mDataSett.get(position).getNom());
        Log.d(TAG, "matricule" + mDataSett.get(position) + " set.");
        // Log.d(TAG, "matricule" + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        System.out.println(mDataSett.get(position).getNom());
        System.out.println(mDataSett.get(position).getPrenom());
        System.out.println(mDataSett.get(position).getNom()+"aaaaaaaaaa");

        // loading album cover using Glide library
    try {


        System.out.println(mDataSett.get(position).getUrl_img()+5555);
        Glide.with(mContext).load(mDataSett.get(position).getUrl_img()).dontAnimate().placeholder(R.drawable.avatar).into(viewHolder.getImageView());
        viewHolder.getImageView().setBorderColor(Color.parseColor(couleurs.get(position)));
        viewHolder.getImageView().setBorderWidth(15);
        viewHolder.getImageView().addShadow();

         }catch (Exception e) {
        e.printStackTrace();
    }


       viewHolder.getTextViewNom().setText( mDataSett.get(position).getNom());
       viewHolder.getTextViewPrenom().setText( mDataSett.get(position).getPrenom());
        System.out.println(couleurs.size()+"**********");
        //System.out.println(couleurs.get(position));

     //  viewHolder.getImageView().setBackgroundColor(Color.parseColor(couleurs.get(position)));





        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder.overflow,mDataSett.get(position ),position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mDataSett.size();
    }
    private void showPopupMenu(final View view,Etudiant e,int position ) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.etudiant_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new   MyMenuItemClickListener(e,position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
Etudiant etudiant;
        int position;
        public MyMenuItemClickListener(Etudiant e,int position)  {
this.etudiant=e;
            this.position=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    System.out.println(item.getItemId());
                    return false;
                }
            });
            switch (menuItem.getItemId()) {
                case R.id.supprimer_etudiant:
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                    delete(etudiant.getId());
               mDataSett.remove(position);
                    RecycleradapterEtudiant.this.notifyDataSetChanged();






                    return true;

                case R.id.update_etudiant:
                    Intent intent2 = new Intent( mContext, etudiant_modifier.class);
                    intent2.putExtra("etudiant", (Serializable) etudiant);
                    mContext.startActivity(intent2);



                default:
            }
            return false;
        }
    }

    public void delete(final int id) {


        //perform delete
        System.out.println(id+"***********************");

        String delete_url = "http://teacherkit.000webhostapp.com/TeacherKit/delete_etudiant.php";
        System.out.println("55555555555555555555555555555555");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                delete_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("jsonte3wedhni ", response);
                try {
                    Log.e("jsonte3wedhni ", response);

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(mContext, "yess ! ", Toast.LENGTH_LONG).show();



                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mContext,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ""+id);

                return params;
            }

        };

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq,"supp etudiant");;
    }


}


