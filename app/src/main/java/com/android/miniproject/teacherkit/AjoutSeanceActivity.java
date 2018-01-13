package com.android.miniproject.teacherkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.miniproject.teacherkit.dao.ClasseDao;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjoutSeanceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button dateButton;
    private Button timeButton;
    private Button Valider;
    private EditText SeanceTitle;
    private EditText SeanceDescroption;
    private Spinner ClasseChois;
    private List<Classe> classes;
    private ClasseDao classeDao;
    private SQLiteHandler db;
    private Classe classe;
    private String dateB;
    private String dateF;
    private String TimeB;
    private String TimeF;
    Boolean t = false ;
    Boolean d = false;
    Boolean c = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_seance);

        // Find our View instances

        dateButton = (Button) findViewById(R.id.date_button);
        timeButton = (Button) findViewById(R.id.time_button);
        SeanceTitle = (EditText) findViewById(R.id.seance_title);
        SeanceDescroption = (EditText) findViewById(R.id.seance_desc);
        ClasseChois = (Spinner) findViewById(R.id.spinner_classe);
        Valider = (Button) findViewById(R.id.btValider);
        db = new SQLiteHandler(getApplicationContext());
        classeDao = new ClasseDao();
        classes = new ArrayList<Classe>();



        initData();

        ClasseChois.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classe = new Classe();
                classe = classes.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AjoutSeanceActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AjoutSeanceActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mat = SeanceTitle.getText().toString().trim();
                final String des = SeanceDescroption.getText().toString().trim();
                final String startDate = dateB + " " + TimeB;
                final String endDate = dateB + " " + TimeF;

                if (c) {


                    if (mat.isEmpty()) {
                        SeanceTitle.setHint("champ obligatoir");
                        SeanceTitle.setHintTextColor(Color.RED);
                    }
                    if (des.isEmpty()) {
                        SeanceDescroption.setHint("champ obligatoir");
                        SeanceDescroption.setHintTextColor(Color.RED);
                    }
                    if(!t && d){
                        Toast.makeText(AjoutSeanceActivity.this, "il faut preciser la durée de la seance ! ", Toast.LENGTH_SHORT).show();
                    }else if (t && !d){
                        Toast.makeText(AjoutSeanceActivity.this, "il faut preciser la date de la seance ! ", Toast.LENGTH_SHORT).show();
                    }else if (!t && !d){
                        Toast.makeText(AjoutSeanceActivity.this, "il faut preciser la date et la durée de la seance ! ", Toast.LENGTH_SHORT).show();

                    }
                    if (!mat.isEmpty() && !des.isEmpty() && t && d) {
                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                AppConfig.URL_AJOUTERSEANCE, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean("error");
                                    if (!error) {
                                        Toast.makeText(getApplicationContext(), "yess ! ", Toast.LENGTH_LONG).show();

                                        onBackPressed();
                                    } else {

                                        String errorMsg = jObj.getString("error_msg");
                                        Toast.makeText(getApplicationContext(),
                                                errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),
                                        error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                // Posting params to register url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("matricule", mat);
                                params.put("startDate", startDate);
                                params.put("endDate", endDate);
                                params.put("id_classe", "" + classe.getIdClasse());
                                params.put("id_user", "" + db.getUserDetails().get("uid"));
                                params.put("description", des);


                                return params;
                            }

                        };
                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(strReq, "Ajouter Seance");

                    }

                } else {


                    Intent intent = new Intent(
                            AjoutSeanceActivity.this,
                            AjoutClasse.class);
                    startActivity(intent);
                    finish();
                }



            }
        });


    }

    private void initData() {
        classes.clear();
        String url = AppConfig.URL_LISTECLASE + db.getUserDetails().get("uid");
        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")) {
                    classeDao.getClasse(classes, response);

                    CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(getApplicationContext(),
                            R.layout.custom_spinner_item, classes);
                    ClasseChois.setAdapter(dataAdapter);
                    c = true;

                } else {
                    Valider.setText("Vous n'avez pas encore creer de classe \n Ajouter classe ");
                    c = false;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year ;
        d= true ;
        dateButton.setText(date);
        dateB = "" + year + "-" + (monthOfYear) + "-" + dayOfMonth;
        dateF = "" + yearEnd + "-" + (monthOfYearEnd) + "-" + dayOfMonthEnd;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
        String time = "" + hourString + "h" + minuteString + " To - " + hourStringEnd + "h" + minuteStringEnd;
        t = true ;
        TimeB = "" + hourString + ":" + minuteString + ":00";
        TimeF = "" + hourStringEnd + ":" + minuteStringEnd + ":00";
        timeButton.setText(time);
    }


}
