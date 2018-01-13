package com.android.miniproject.teacherkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Classe;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class AjoutEtudiant extends AppCompatActivity {
    EditText etnom,etprenom,etnum_tel,etmail,etnote;

    int idclasse=1;
    private ImageView imageView;
    int PICK_IMAGE_REQUEST=1;
    private Bitmap bitmap;
    private Button buttonChoose;
    private Button buttonUpload;
    private String UPLOAD_URL ="http://teacherkit.000webhostapp.com/TeacherKit/ajoutEtudiant.php";
    private String KEY_IMAGE = "path";
    private String KEY_NOM = "nom";
    private String KEY_PRENOM = "prenom";
    private String KEY_TEL = "num_tel";
    private String KEY_NOTE = "note";
    private String KEY_MAIL = "mail";
    private String KEY_IDCLASSE = "id_classe";
    Drawable iconAlerte;
    Validator validator;
    MaterialIconView iconViewphoto;
    String nom ;
    String prenom ;
    String numtel ;
    String mail ;
    String note ;
    String image;
    boolean VALID_MAIL=true,VALID_NOM=true,VALID_PRENOM=true,VALID_NOTE=true,VALID_TEL=true;
Classe classe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_etudiant);
        Intent intent = getIntent();
        classe = (Classe) intent.getExtras().getSerializable("classe");

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        etnom = (EditText) findViewById(R.id.et_nom);
        etprenom = (EditText) findViewById(R.id.et_prenom);
        etnum_tel= (EditText) findViewById(R.id.et_numtel);
        etmail= (EditText) findViewById(R.id.et_mail);
               imageView  = (ImageView) findViewById(R.id.imageView);
        iconViewphoto=(MaterialIconView)  findViewById(R.id.iconchoosephoto);
        validator=new Validator();

        iconAlerte = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.ALERT) // provide an icon
                .setColor(R.color.red) // set the icon color
                .setToActionbarSize() // set the icon size
                .build();



    }
    public void showFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(View v){
        //Showing the progress dialog

        nom = etnom.getText().toString().trim();
        prenom = etprenom.getText().toString().trim();
        numtel = etnum_tel.getText().toString().trim();
         mail = etmail.getText().toString().trim();



        if(!validator.validateEmail(mail)){
            etmail.setError("verifiez l'adresse mail !",iconAlerte);

            VALID_MAIL=false;
        }

 //       if(!validator.validateString(numtel)){
   //         etnum_tel.setError("introduire le numero de telephone",iconAlerte);
    //        VALID_TEL=false;
     //   }
       else if(!validator.validateString(nom)){
            etnom.setError("introduire le nom",iconAlerte);
            VALID_NOM=false;

        }
      else  if(!validator.validateString(prenom)){
            etprenom.setError("introduire le prenom",iconAlerte);
            VALID_PRENOM=false;
        }
 //       if(!validator.validateString(note)){
  //          etnote.setError("introduire une note ",iconAlerte);
   //         VALID_NOTE=false;
    //    }




        else {
            final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            //Showing toast message of the response
                            onBackPressed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

                            //Showing toast
                            Toast.makeText(AjoutEtudiant.this, "veuillez choisir une photo", Toast.LENGTH_LONG).show();


                            // System.out.println(volleyError.getMessage().toString());

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    if (!bitmap.equals("")){
                         image = getStringImage(bitmap);

                    }






                    //Getting Image Name


                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put(KEY_IMAGE, image);
                    params.put(KEY_NOM, nom);
                    params.put(KEY_PRENOM, prenom);
                    params.put(KEY_TEL, numtel);
                    params.put(KEY_MAIL, mail);
                    params.put(KEY_IDCLASSE, classe.getIdClasse()+"");


                    //Creating parameters

                    //returning parameters
                    return params;
                }
            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Adding request to the queue
            requestQueue.add(stringRequest);

        }


    }

   // public void ajoutEtudiant(View view){

     //   String nom =etnom.getText().toString();
     //   String prenom =etprenom.getText().toString();
     //   String num_tel =etnum_tel.getText().toString();
      //  String mail =etmail.getText().toString();
      //  String note =etnote.getText().toString();
     //   String id_classe=idclasse+"";

     //   EtudiantBackground backgroundTask = new EtudiantBackground(this);
     //   backgroundTask.execute(nom,prenom,num_tel,mail,note,id_classe);
    //}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
