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
 * Created by user on 11/14/2016.
 */

public class ClassBackground extends AsyncTask<String,Void,String> {
    Context ctx;
    private SQLiteHandler db;

    public ClassBackground( Context ctx ) {


        this.ctx =ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String ajout_url = "http://teacherkit.000webhostapp.com/TeacherKit/AjoutClass.php";
        db = new SQLiteHandler(ctx);
        String matricule = params[0];
        String matiere = params[1];
        String datedebut = params[2];
        String datefin = params[3];
        String description = params[4];
        String Couleur=params[5];
        System.out.println(Couleur+"********");
        String id_user =db.getUserDetails().get("uid");
        System.out.println(matricule+11111);

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
            String data = URLEncoder.encode("matricule", "UTF-8") +"="+ URLEncoder.encode(matricule, "UTF-8")+"&" +
                    URLEncoder.encode("matiere", "UTF-8") + "=" + URLEncoder.encode(matiere, "UTF-8") + "&"+
                    URLEncoder.encode("datedebut", "UTF-8") + "=" + URLEncoder.encode(datedebut, "UTF-8") + "&"+
                    URLEncoder.encode("datefin", "UTF-8") + "=" + URLEncoder.encode(datefin, "UTF-8") + "&"+
                    URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")+ "&"+
                    URLEncoder.encode("couleur", "UTF-8") + "=" + URLEncoder.encode(Couleur, "UTF-8")+ "&"+
                    URLEncoder.encode("id_user", "UTF-8") + "=" + URLEncoder.encode(id_user, "UTF-8");

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