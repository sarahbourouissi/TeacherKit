package com.android.miniproject.teacherkit.dao;

import com.android.miniproject.teacherkit.Entity.comportement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 12/7/2016.
 */

public class ComportementDao {

    public ComportementDao() {
    }
    public void getComportement(List<comportement> comportements, String jsonResponse){

        try {
            JSONArray array = new JSONArray(jsonResponse);
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);

                comportement c=new comportement(j);
                comportements.add(c);

            }




        }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
