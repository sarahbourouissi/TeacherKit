package com.android.miniproject.teacherkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class etudiant_modifier extends AppCompatActivity {


    EditText etnom, etprenom, etnum_tel, etmail, etnote;
    MaterialIconView iconViewphoto;
    private ImageView imageView;
    Button buttonUpload;
    Etudiant etudiant;
    private Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL = "http://teacherkit.000webhostapp.com/TeacherKit/update_etudiant.php";
    private String KEY_IMAGE = "path";
    private String KEY_NOM = "nom";
    private String KEY_PRENOM = "prenom";
    private String KEY_TEL = "num_tel";
    private String KEY_NOTE = "note";
    private String KEY_MAIL = "mail";
    private String KEY_ID = "id";
    Boolean i = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etudiant_modifier);
        Intent intent = getIntent();
        etudiant = (Etudiant) intent.getExtras().getSerializable("etudiant");


        buttonUpload = (Button) findViewById(R.id.buttonUploadmodifier);
        etnom = (EditText) findViewById(R.id.et_modif_nom);
        etprenom = (EditText) findViewById(R.id.et_modif_prenom);
        etnum_tel = (EditText) findViewById(R.id.et_modif_numtel);
        etmail = (EditText) findViewById(R.id.et_modif_mail);
        imageView = (ImageView) findViewById(R.id.imageViewmodifier);
        iconViewphoto = (MaterialIconView) findViewById(R.id.iconchoosephotomodifier);

        etnom.setText(etudiant.getNom());
        etprenom.setText(etudiant.getPrenom());
        etnum_tel.setText(etudiant.getNum_tel() + "");
        etmail.setText(etudiant.getMail() + "");

        System.out.println(etudiant.getUrl_img());

        try {
            Glide.with(this)
                    .load(etudiant.getUrl_img())
                    .placeholder(R.drawable.avatar)
                    .crossFade()
                    .into(imageView);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showFileChooserModif(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    public void uploadImageModif(View v) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("json reponse", s);
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println(volleyError);
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(etudiant_modifier.this, "verifier", Toast.LENGTH_LONG).show();


                        // System.out.println(volleyError.getMessage().toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image;
                if (!i) {
                    image = etudiant.getUrl_img();
                } else {
                    image = getStringImage(bitmap);
                }

                //Getting Image Name
                String nom = etnom.getText().toString().trim();
                String prenom = etprenom.getText().toString().trim();
                String numtel = etnum_tel.getText().toString().trim();
                String mail = etmail.getText().toString().trim();


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NOM, nom);
                params.put(KEY_PRENOM, prenom);
                params.put(KEY_TEL, numtel);
                params.put(KEY_MAIL, mail);
                params.put(KEY_ID, etudiant.getId() + "");
                params.put("oldpath", etudiant.getUrl_img() + "");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                i = true;
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                System.out.println("hhhh");
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                i = false;
            }
        }
    }

}
