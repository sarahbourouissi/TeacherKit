package com.android.miniproject.teacherkit;

import android.os.AsyncTask;
import android.util.Log;

import com.android.miniproject.teacherkit.Entity.Etudiant;
import com.android.miniproject.teacherkit.Entity.asseduite;
import com.android.miniproject.teacherkit.dao.assiduiteDao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by user on 12/23/2016.
 */

public class GetAsseduiteBackground extends AsyncTask<List<Etudiant>, Void, String> {


    String jsonResponse;
    asseduite asseduite;
    List<asseduite> asseduites;
    assiduiteDao asseduiteDao;
    List<Etudiant> etudiants;
    static InputStream is;
    static String json;
    List<String> couleurs;
    int passable, excellent, nul;

    int i;

    @Override
    protected String doInBackground(List<Etudiant>... params) {
        etudiants = params[0];
        String url;


     url = "http://192.168.8.100/teacherkit/get_nbr_comportement.php" ;


     jsonResponse = getJSONFromUrl(url);

     if (jsonResponse != null) {
         Log.e("", jsonResponse);
         System.out.println(jsonResponse);
         asseduiteDao.getAssiduite(asseduites, jsonResponse);
         System.out.println("bowwww mchéé");
     }
     excellent =0;
     passable=0;
     nul=0;
     asseduites.clear();

     System.out.println(etudiants.get(i).getId() + "**************");
     System.out.println("bowwww");
     Log.e("etudiant", etudiants.get(i).getNom() );








        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }



    public String getJSONFromUrl(String url) {

        // Making HTTP request


        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // return JSON String
        return json;
    }


}
