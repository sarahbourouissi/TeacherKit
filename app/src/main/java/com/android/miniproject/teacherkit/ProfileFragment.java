package com.android.miniproject.teacherkit;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by DELL PC on 02/01/2017.
 */
public class ProfileFragment extends Fragment {
private TextView nbClasse ;
    private EditText nameUser ;
    private EditText emailUser ;
    private EditText passwordUser ;
    private static SQLiteHandler db;
    private Button modifier ;
    private SessionManager session;
    private ImageButton edit ;

    public ProfileFragment(){

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        db = new SQLiteHandler(getContext());
        String id =db.getUserDetails().get("uid");
        nbClasse = (TextView) v.findViewById(R.id.nbclasse);
        modifier = (Button) v.findViewById(R.id.btnEng);
        edit = (ImageButton) v.findViewById(R.id.btnEdit);
        nameUser = (EditText) v.findViewById(R.id.username1);
        emailUser = (EditText) v.findViewById(R.id.email1);
        passwordUser = (EditText) v.findViewById(R.id.pass1);

        nameUser.setHint(db.getUserDetails().get("name"));
        emailUser.setHint(db.getUserDetails().get("email"));
        nameUser.setEnabled(false);
        emailUser.setEnabled(false);
        passwordUser.setEnabled(false);
        session = new SessionManager(getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        modifier.setVisibility(View.INVISIBLE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifier.setVisibility(View.VISIBLE);
                nameUser.setEnabled(true);
                emailUser.setEnabled(true);
                passwordUser.setEnabled(true);
            }
        });

        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, AppConfig.URL_LISTECLASE+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray array = new JSONArray(response);
                        nbClasse.setText(""+array.length()+" Classes");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error setting note : " + error.getMessage());
                if (error instanceof TimeoutError) {
                    System.out.println("erreur");
                }

            }
        }));


        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameUser.getText().toString().trim();
                String email = emailUser.getText().toString().trim();
                final String password = passwordUser.getText().toString().trim();
                if (name.isEmpty()) {
                    name = db.getUserDetails().get("name");
                }
                if (email.isEmpty()) {
                    email = db.getUserDetails().get("email");
                }
                if (password.isEmpty()) {

                    final String finalName = name;
                    final String finalEmail = email;

                    new AlertDialog.Builder(getContext())
                            .setTitle("Modification Profile")
                            .setMessage("voulez vous modifier votre compte ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    StringRequest strReq = new StringRequest(Request.Method.POST,
                                            AppConfig.URL_UPDATEUSER, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                boolean error = jObj.getBoolean("error");
                                                if (!error) {
                                                    db.updateUser(finalName,finalEmail,db.getUserDetails().get("uid"));
                                                    session.setLogin(false);
                                                    db.deleteUsers();
                                                    Intent i = new Intent(getActivity(),LoginActivity.class);
                                                    startActivity(i);
                                                } else {


                                                    String errorMsg = jObj.getString("error_msg");
                                                    Toast.makeText(getContext(),
                                                            errorMsg, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
                                            params.put("name", finalName);
                                            params.put("email", finalEmail);
                                            params.put("id_user",db.getUserDetails().get("uid"));

                                            return params;
                                        }

                                    };

                                    // Adding request to request queue
                                    AppController.getInstance().addToRequestQueue(strReq, "update User");

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                } else {
                    final String finalName = name;
                    final String finalEmail = email;

                    new AlertDialog.Builder(getContext())
                            .setTitle("Modification Profile")
                            .setMessage("voulez vous modifier votre compte ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    StringRequest strReq = new StringRequest(Request.Method.POST,
                                            AppConfig.URL_UPDATEUSERPASSWORD, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println(response);
                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                boolean error = jObj.getBoolean("error");
                                                if (!error) {
                                                    db.updateUser(finalName,finalEmail,db.getUserDetails().get("uid"));
                                                    session.setLogin(false);
                                                    db.deleteUsers();
                                                    Intent i = new Intent(getActivity(),LoginActivity.class);
                                                    startActivity(i);

                                                } else {

                                                    // Error occurred in registration. Get the error
                                                    // message
                                                    String errorMsg = jObj.getString("error_msg");
                                                    Toast.makeText(getContext(),
                                                            errorMsg, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
                                            params.put("name", finalName);
                                            params.put("email", finalEmail);
                                            params.put("password", password);
                                            params.put("id_user",db.getUserDetails().get("uid"));

                                            return params;
                                        }

                                    };

                                    // Adding request to request queue
                                    AppController.getInstance().addToRequestQueue(strReq, "update User Password");

                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            }
        });
        return v;
    }
}
