package com.android.miniproject.teacherkit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class classe_modifier extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText etmatricule,etmatiere,etdescription,etdatedebut,etdatefin;
    Button btnajouter ;
    FButton btncouleur;
    String Couleur;
    private TextView dateTextView;
    Button dateButton;
    MaterialIconView iconViewDateDebut;
    MaterialIconView iconViewDatefin;
    String datedebut,datefin;
    TextView datemodif ;
    Classe classe;
    String UPLOAD_URL="http://teacherkit.000webhostapp.com/TeacherKit/update_classe.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe_modifier);
        Intent intent = getIntent();
        classe = (Classe) intent.getExtras().getSerializable("classe");

        btnajouter= (Button) findViewById(R.id.btn_classe_modif);
        btncouleur= (FButton) findViewById(R.id.btn_dialog_modif);
        iconViewDateDebut=(MaterialIconView)  findViewById(R.id.icondatedebut_modif);

        Couleur = classe.getCouleur();
        datedebut = classe.getDatedebut();
        datefin = classe.getDatefin();

        etmatricule = (EditText) findViewById(R.id.et_matricule_modif);
        etmatiere = (EditText) findViewById(R.id.et_matiere_modif);
        etdescription= (EditText) findViewById(R.id.et_description_modif);
        datemodif = (TextView) findViewById(R.id.et_datedebut_modif);
        etmatiere.setText(classe.getMatiere());
        etmatricule.setText(classe.getMatricule());
        etdescription.setText(classe.getDescription());
        datemodif.setText(datedebut + " TO " +datefin);



        iconViewDateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        classe_modifier.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });










    }


    public void couleur(View v){

        //Create color picker view
        View view = this.getLayoutInflater().inflate(R.layout.color_picker_dialog, null);
        if (view == null) return;

        //Config picker
        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
        SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);
        final TextView hexCode = (TextView) view.findViewById(R.id.hex_code);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOldCenterColor(btncouleur.getButtonColor());
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int intColor) {
                String hexColor = Integer.toHexString(intColor).toUpperCase();
                hexCode.setText("#" + hexColor);
            }
        });

        //Config dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Choose your color");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Update color
                btncouleur.setButtonColor(picker.getColor());
                System.out.println(picker.getColor());
                //  String.format("#%06X", (0xFFFFFF & intColor))
                System.out.println(String.format("#%06X", (0xFFFFFF & picker.getColor())));
                Couleur =String.format("#%06X", (0xFFFFFF & picker.getColor()));
            }
        });
        builder.create().show();



    }


    public void Modifclasse(View v){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Intent intent = new Intent(classe_modifier.this,MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(classe_modifier.this, "verifiez...", Toast.LENGTH_LONG).show();


                        // System.out.println(volleyError.getMessage().toString());

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image;


                //Getting Image Name
                String matiere = etmatiere.getText().toString().trim();
                String matricule = etmatricule.getText().toString().trim();
                String description= etdescription.getText().toString().trim();


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put("couleur",Couleur );

                params.put("datedebut",datedebut );

                params.put("datefin",datefin );
                //Adding parameters
                params.put("matricule", matricule);
                params.put("matiere", matiere);
                params.put("descreption", description);


                params.put("id",classe.getIdClasse()+"");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        datemodif.setText(date);
        datedebut = "" + year + "-" + (monthOfYear) + "-" + dayOfMonth;
        datefin = "" + yearEnd + "-" + (monthOfYearEnd) + "-" + dayOfMonthEnd;
    }
}
