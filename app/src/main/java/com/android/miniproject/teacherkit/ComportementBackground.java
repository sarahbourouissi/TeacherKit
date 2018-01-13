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
 * Created by user on 12/7/2016.
 */

public class ComportementBackground extends AsyncTask<String,Void,String> {

    Context ctx;
    public ComportementBackground(Context ctx) {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {

        String ajout_url = "http://teacherkit.000webhostapp.com/TeacherKit/ajoutcomportement.php";

        String seduite = params[0];
        String remarque = params[1];

        String id_etudiant =params[2];
        String dateSeance=params[3];

        System.out.println(seduite+11111);


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
            String data = URLEncoder.encode("seduite", "UTF-8") +"="+ URLEncoder.encode(seduite, "UTF-8")+"&" +
                    URLEncoder.encode("remarque", "UTF-8") + "=" + URLEncoder.encode(remarque, "UTF-8") + "&"+
                    URLEncoder.encode("id_etudiant", "UTF-8") + "=" + URLEncoder.encode(id_etudiant, "UTF-8") + "&"+
                    URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(dateSeance, "UTF-8")  ;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
            return "Add Success...";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (ProtocolException e) {
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
