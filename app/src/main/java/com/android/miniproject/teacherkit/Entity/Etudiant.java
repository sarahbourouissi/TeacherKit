package com.android.miniproject.teacherkit.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 11/23/2016.
 */

public class Etudiant implements Serializable , SortedListAdapter.ViewModel,Parcelable {


    private int id;
    private String nom;
    private String prenom;
    private  String note;
    private  String mail ;
    private  int num_tel;
    private  String Url_img;



    public Etudiant() {
    }
    public Etudiant(JSONObject j) {
        id = j.optInt("id");
        nom=j.optString("nom");
        prenom=j.optString("prenom");
        note=j.optString("note");
        mail=j.optString("mail");
        num_tel = j.optInt("num_tel");
        Url_img= j.optString("image");

    }

    protected Etudiant(Parcel in) {
        id = in.readInt();
        nom = in.readString();
        prenom = in.readString();
        note = in.readString();
        mail = in.readString();
        num_tel = in.readInt();
        Url_img = in.readString();
    }

    public static final Creator<Etudiant> CREATOR = new Creator<Etudiant>() {
        @Override
        public Etudiant createFromParcel(Parcel in) {
            return new Etudiant(in);
        }

        @Override
        public Etudiant[] newArray(int size) {
            return new Etudiant[size];
        }
    };

    public String getUrl_img() {
        return Url_img;
    }

    public void setUrl_img(String url_img) {
        Url_img = url_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(note);
        dest.writeString(mail);
        dest.writeInt(num_tel);
        dest.writeString(Url_img);
    }
}
