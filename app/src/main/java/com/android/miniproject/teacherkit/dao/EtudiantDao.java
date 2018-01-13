package com.android.miniproject.teacherkit.dao;

import com.android.miniproject.teacherkit.Entity.Etudiant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 12/6/2016.
 */

public class EtudiantDao {
    public EtudiantDao() {
    }
 public void getStudent(List<Etudiant> etudiants, String jsonResponse){

try {
    JSONArray array = new JSONArray(jsonResponse);
    for (int i = 0; i < array.length(); i++) {
        JSONObject j = array.getJSONObject(i);

        Etudiant etudiant=new Etudiant(j);

        etudiants.add(etudiant);

}




     }
catch (JSONException e) {
    e.printStackTrace();
}


 }

}
