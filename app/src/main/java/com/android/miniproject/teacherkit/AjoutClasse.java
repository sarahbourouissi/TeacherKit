package com.android.miniproject.teacherkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Calendar;
import java.util.Date;

import info.hoang8f.widget.FButton;

public class AjoutClasse extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener {
    EditText etmatricule,etmatiere,etdescription,etdatedebut,etdatefin;
    Button btnajouter ;
    FButton btncouleur;
    String Couleur;
    private TextView dateTextView;
    Button dateButton;
    MaterialIconView iconViewDateDebut;
    MaterialIconView iconViewDatefin;
    String datedebut,datefin;
    Validator validator;
    Drawable iconAlerte;
    boolean VALID_DECRIPTION=true,VALID_MATRICULE=true,VALID_MATIERE=true;
    String dated ;
    String datef ;
    TextView TVDate ;
    private Boolean c = false ;
    private Boolean d = false ;

    private Toast mToast;
    private Thread mThread;




    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_classe);
            validator=new Validator();


        btnajouter= (Button) findViewById(R.id.btn_ajouter);
        btncouleur= (FButton) findViewById(R.id.btn_dialog);
        iconViewDateDebut=(MaterialIconView)  findViewById(R.id.icondatedebut);

        TVDate = (TextView) findViewById(R.id.et_datedebut);

        etmatricule = (EditText) findViewById(R.id.et_matricule);
        etmatiere = (EditText) findViewById(R.id.et_matiere);
        etdescription= (EditText) findViewById(R.id.et_description);



        iconAlerte = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.ALERT) // provide an icon
                .setColor(R.color.red) // set the icon color
                .setToActionbarSize() // set the icon size
                .build();

iconViewDateDebut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                AjoutClasse.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
});












}




    public void ajoutClasse(View view){
        System.out.println("c bnn ");



        String m = etmatricule.getText().toString();
        String mat = etmatiere.getText().toString();
        System.out.println(mat+0000);
        String description = etdescription.getText().toString();
        if(m.isEmpty()){
            etmatricule.setHint("champ obligatoire ");
            etmatricule.setHintTextColor(Color.RED);
        }
        if(mat.isEmpty()){
            etmatiere.setHint("champ obligatoire ");
            etmatiere.setHintTextColor(Color.RED);
        }
        if (description.isEmpty()){
            etdescription.setHint("champ obligatoire ");
            etdescription.setHintTextColor(Color.RED);
        }
        if (!c) {
            Couleur = String.format("#%06X", (0xFFFFFF & -10368769));
            c =true ;
        }
        if(!d){
            Calendar now = Calendar.getInstance();
            dated = "" + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH)) + "-" + now.get(Calendar.DAY_OF_MONTH);
            datef = "" + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH)) + "-" + now.get(Calendar.DAY_OF_MONTH);
            d= true;
        }



        if(!m.isEmpty() && !mat.isEmpty() && !description.isEmpty() && d && c ){
            ClassBackground backgroundTask = new ClassBackground(this);
            System.out.println(Couleur + "aaaaaaaaaa");
            backgroundTask.execute(m, mat, dated, datef, description, Couleur);
            Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Invalid matricule et matiere  ", Toast.LENGTH_SHORT).show();
        }








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
    builder.setTitle("choisissez une couleur");
    builder.setCancelable(true);

    builder.setNegativeButton("sortir", null);
    builder.setPositiveButton("Validez", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //Update color
            btncouleur.setButtonColor(picker.getColor());
            System.out.println(picker.getColor());
          //  String.format("#%06X", (0xFFFFFF & intColor))
            System.out.println(String.format("#%06X", (0xFFFFFF & picker.getColor())));
            Couleur =String.format("#%06X", (0xFFFFFF & picker.getColor()));
            c= true ;
        }
    });
    builder.create().show();



}



    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        dated = "" + year + "-" + (monthOfYear) + "-" + dayOfMonth;
        datef = "" + yearEnd + "-" + (monthOfYearEnd) + "-" + dayOfMonthEnd;
        d = true ;
        TVDate.setText(date);
    }
}
