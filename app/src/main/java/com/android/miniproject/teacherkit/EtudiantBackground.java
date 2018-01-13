package com.android.miniproject.teacherkit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 11/15/2016.
 */

public class EtudiantBackground extends AsyncTask<String,Void,String> {

    Context ctx;



    public EtudiantBackground( Context ctx ) {

        this.ctx =ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String ajout_url = "http://10.0.2.2/teacherkit/ajoutEtudiant.php";
        String nom = params[0];
        String prenom = params[1];
        String numtel  = params[2] ;
        String mail = params[3];
        String note = params[4];
        String idclasse = params[5];

        try {
            URL url = new URL(ajout_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println("cnnnnnnnnnx+++++++++++");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            System.out.println("okkk");
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("nom", "UTF-8") +"="+ URLEncoder.encode(nom, "UTF-8")+"&" +
                    URLEncoder.encode("prenom", "UTF-8") + "=" + URLEncoder.encode(prenom, "UTF-8") + "&"+
                    URLEncoder.encode("num_tel", "UTF-8") + "=" + URLEncoder.encode(numtel, "UTF-8") + "&"+
                    URLEncoder.encode("note", "UTF-8") + "=" + URLEncoder.encode(note, "UTF-8") + "&"+
                    URLEncoder.encode("id_classe", "UTF-8") + "=" + URLEncoder.encode(idclasse, "UTF-8");


            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();

            return "Add Success...";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
