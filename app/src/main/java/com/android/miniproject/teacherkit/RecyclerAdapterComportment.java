package com.android.miniproject.teacherkit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.comportement;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 12/7/2016.
 */

public class RecyclerAdapterComportment  extends RecyclerView.Adapter<RecyclerAdapterComportment.ViewHolder> {


    private static final String TAG = "CustomAdapter";
    private static List<comportement> mDataSett;
    private static Context mContext;



    RecyclerAdapterComportment(List<comportement> c, Context m){

        this.mDataSett=c;
        mContext=m;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewRemarque;
        private  CardView mCardView ;
        public ImageView overflow;
        TextView tvDate;
        public ViewHolder(View v) {
            super(v);


            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");







                }
            });

            overflow=(ImageView) v.findViewById(R.id.overflow_comportement);
            tvDate=(TextView) v.findViewById(R.id.date_comportement);

            textViewRemarque = (TextView) v.findViewById(R.id.idremarque);


            mCardView= (CardView) v.findViewById(R.id.comportement_card);
        }
        public CardView getmCardView(){return mCardView;}

        public TextView getTextViewRemarque() {
            return textViewRemarque;
        }
        public TextView getTextViewDate(){return  tvDate;}


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comportement_card, viewGroup, false);
     // v.setElevation(getPixelsFromDPs(6));
        return new ViewHolder(v);
    }
    protected int getPixelsFromDPs(int dps){
       Resources r = mContext.getResources();
       int  px = (int) (TypedValue.applyDimension(
              TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        System.out.println(mDataSett.get(position).getId());

        Log.d(TAG, "matricule" + mDataSett.get(position) + " set.");
        // Log.d(TAG, "matricule" + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        System.out.println(mDataSett.get(position).getNote());
        System.out.println(mDataSett.get(position).getRemarque());
        System.out.println(mDataSett.get(position).getSeduite()+"aaaaaaaaaa");
        System.out.println(mDataSett.get(position).getDate());
        viewHolder.getTextViewDate().setText(mDataSett.get(position).getDate());
        viewHolder.getTextViewRemarque().setText( mDataSett.get(position).getRemarque());

        if (mDataSett.get(position).getSeduite().equals("strict")){

            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FF99CC00"));
        }else if(mDataSett.get(position).getSeduite().equals("passable")){

            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFBB33"));
        }else{
            //nul
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFF4444"));

        }
        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(viewHolder.overflow,mDataSett.get(position).getId(),position);

            }
        });










    }
    private void showPopupMenu(final View view,int id,int position ) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_comporetement, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id,position));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int idcomportement;
        int position;
        public MyMenuItemClickListener(int id,int position) {
            this.idcomportement=id;
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
                case R.id.supprimer_comportement:
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                    delete(idcomportement);
                    mDataSett.remove(position);
                    RecyclerAdapterComportment.this.notifyDataSetChanged();

                    return true;



                default:
            }
            return false;
        }
    }
    public void delete(final int id) {


        //perform delete
        System.out.println(id+"***********************");

        String delete_url = "http://teacherkit.000webhostapp.com/TeacherKit/delete_comportement.php";
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






    @Override
    public int getItemCount() {
        return mDataSett.size();
    }
}
