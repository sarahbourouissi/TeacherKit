package com.android.miniproject.teacherkit;

import java.io.Serializable;

/**
 * Created by DELL PC on 13/12/2016.
 */

public class seance implements Serializable {
    int id;
    String stardate;
    String enddate;
    String titre;
    String description;
    int id_classe;

    public seance(int id, String stardate, String enddate, String titre,String description, int id_classe) {
        this.id = id;
        this.stardate = stardate;
        this.enddate = enddate;
        this.titre = titre;
        this.description = description ;
        this.id_classe = id_classe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getStardate() {
        return stardate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getTitre() {
        return titre;
    }

    public int getId_classe() {
        return id_classe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setId_classe(int id_classe) {
        this.id_classe = id_classe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        seance seance = (seance) o;

        if (id != seance.id) return false;
        if (id_classe != seance.id_classe) return false;
        if (!stardate.equals(seance.stardate)) return false;
        if (!enddate.equals(seance.enddate)) return false;
        return titre.equals(seance.titre);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + stardate.hashCode();
        result = 31 * result + enddate.hashCode();
        result = 31 * result + titre.hashCode();
        result = 31 * result + id_classe;
        return result;
    }

    @Override
    public String toString() {
        return "seance{" +
                "id=" + id +
                ", stardate='" + stardate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", titre='" + titre + '\'' +
                ", id_classe=" + id_classe +
                '}';
    }
}
