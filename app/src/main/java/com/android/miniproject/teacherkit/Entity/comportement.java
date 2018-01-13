package com.android.miniproject.teacherkit.Entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/7/2016.
 */

public class comportement implements Serializable {

    private int id;
    private String date;
    private String seduite;
    private String note;
    private String remarque;


    public comportement() {
    }

    public comportement(JSONObject j) {

        id = j.optInt("id");
        seduite=j.optString("seduite");
        note=j.optString("note");
        remarque=j.optString("remarque");
        date=j.optString("Date");


    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeduite() {
        return seduite;
    }

    public void setSeduite(String seduite) {
        this.seduite = seduite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public String toString() {
        return "comportement{" +
                "id=" + id +
                ", seduite='" + seduite + '\'' +
                ", note='" + note + '\'' +
                ", remarque='" + remarque + '\'' +
                '}';
    }
}
