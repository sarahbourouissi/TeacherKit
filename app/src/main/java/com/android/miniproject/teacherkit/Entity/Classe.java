package com.android.miniproject.teacherkit.Entity;

import com.android.miniproject.teacherkit.AbstractDataProvider;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 11/23/2016.
 */

public class Classe extends  AbstractDataProvider.Data implements Serializable , SortedListAdapter.ViewModel {

    private int idClasse;
    private String matricule;
    private String matiere;
    private String datedebut;
    private String datefin;
    private String description;
    private int id_user;
    private String couleur;

    public Classe() {

    }



    public Classe(JSONObject j) {
        idClasse = j.optInt("id");
        matricule = j.optString("matricule");
        matiere = j.optString("matiere");
        datedebut = j.optString("datedebut");
        datefin = j.optString("datefin");
        description = j.optString("description");
        id_user = j.optInt("id_user");
        couleur=j.optString("couleur");

    }
    // public Card(JSONObject j) {
    //   id = j.optInt("id");
    // name = j.optString("name");
    // company = j.optString("company");
    // imgPath = j.optString("picture");
    //desc = j.optString("desc");
    //date = new Date(j.optLong("creation"));


    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getIdClasse() {
        return idClasse;
    }



    public void setIdClasse(int id) {
        this.idClasse = id;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(String datedebut) {
        this.datedebut = datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }



    @Override
    public long getId() {
        return idClasse;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setPinned(boolean pinned) {

    }

    @Override
    public boolean isPinned() {
        return false;
    }
}
