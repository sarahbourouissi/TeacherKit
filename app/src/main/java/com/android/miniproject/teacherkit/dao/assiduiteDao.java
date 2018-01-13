package com.android.miniproject.teacherkit.dao;

import com.android.miniproject.teacherkit.Entity.asseduite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 12/22/2016.
 */

public class assiduiteDao {

    public assiduiteDao() {
    }

    public void getAssiduite(List<asseduite> asseduites , String jsonResponse){


                try {

                    JSONArray array = new JSONArray(jsonResponse);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);
                        asseduite c =new asseduite(j);
                        asseduites.add(c);
                    }


                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }



    }

