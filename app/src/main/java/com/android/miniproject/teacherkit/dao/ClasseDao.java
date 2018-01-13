package com.android.miniproject.teacherkit.dao;

import android.util.Log;

import com.android.miniproject.teacherkit.Entity.Classe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 11/30/2016.
 */

public class ClasseDao  {
    public ClasseDao() {
    }

    public void getClasse(List<Classe> classes, String jsonResponse) {
        try {
            JSONArray array = new JSONArray(jsonResponse);
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);

                Classe classe = new Classe(j);
                classes.add(classe);
/*                ObjectMapper mapper = new ObjectMapper();
                try {
                    Card card = mapper.readValue(j.toString(),Card.class);
                    cards.add(card);

                    String jsonCard = mapper.writeValueAsString(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
