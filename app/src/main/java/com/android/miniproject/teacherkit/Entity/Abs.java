package com.android.miniproject.teacherkit.Entity;

/**
 * Created by Aziz on 12/29/2016.
 */
public class Abs {
    private int id_seance ;
    private int id_etudiant;
    private String absence ;

    public Abs(int id_seance, int id_etudiant, String absence) {
        this.id_seance = id_seance;
        this.id_etudiant = id_etudiant;
        this.absence = absence;
    }

    public int getId_seance() {
        return id_seance;
    }

    public void setId_seance(int id_seance) {
        this.id_seance = id_seance;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }
}
