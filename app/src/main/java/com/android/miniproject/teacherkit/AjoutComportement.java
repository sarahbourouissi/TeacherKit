package com.android.miniproject.teacherkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.miniproject.teacherkit.Entity.Etudiant;

public class AjoutComportement extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etremarque;
    Button btnajouter;
    Spinner spinner;
    String s;
    seance seance;
    Etudiant etudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajout_comportement);

        Intent intent = getIntent();
        seance=(seance)  intent.getExtras().getSerializable("seance");
        etudiant =(Etudiant) intent.getExtras().getSerializable("etudiant");


        etremarque= (EditText) findViewById(R.id.et_remarque);

       // Spinner spinner = (Spinner) findViewById(R.id.color_spinner);
        spinner=(Spinner)findViewById(R.id.color_spinner);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(new SpinnerAdapter(this));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.comportement, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    public Void ajouterComportement(View view){

        String remarque=etremarque.getText().toString();

        String id ="1";


        ComportementBackground c = new ComportementBackground(this);
       c.execute(s,remarque,etudiant.getId()+"",seance.getEnddate());

            onBackPressed();

        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println( parent.getItemAtPosition(position));
         s  = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
