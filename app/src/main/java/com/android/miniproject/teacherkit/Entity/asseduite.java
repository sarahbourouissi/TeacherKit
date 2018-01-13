package com.android.miniproject.teacherkit.Entity;

import org.json.JSONObject;

/**
 * Created by user on 12/22/2016.
 */

public class asseduite {

    private String asseduite ;
    private int id;

    public asseduite(JSONObject j) {
       id= j.optInt("id");
        asseduite = j.optString("seduite");

    }

    public String getAsseduite() {
        return asseduite;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "asseduite{" +
                "asseduite='" + asseduite + '\'' +
                ", nb_occurence=" + id +
                '}';
    }
}
