package com.example.gmaoapp.Models;

import net.sourceforge.jtds.jdbc.DateTime;

public class Mouvement {
    int idMvt;
    int numMvt;
    int idOt;
    String dateMvt;
    int idDemandeur;
    String typeMvt;

    public Mouvement(int numMvt, int idOt, String dateMvt, int idDemandeur, String typeMvt) {
        this.numMvt = numMvt;
        this.idOt = idOt;
        this.dateMvt = dateMvt;
        this.idDemandeur = idDemandeur;
        this.typeMvt = typeMvt;
    }

    public Mouvement(int numMvt, String dateMvt, int idDemandeur, String typeMvt) {
        this.numMvt = numMvt;
        this.dateMvt = dateMvt;
        this.idDemandeur = idDemandeur;
        this.typeMvt = typeMvt;
    }

    public int getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(int idMvt) {
        this.idMvt = idMvt;
    }

    public int getNumMvt() {
        return numMvt;
    }

    public void setNumMvt(int numMvt) {
        this.numMvt = numMvt;
    }

    public int getIdOt() {
        return idOt;
    }

    public void setIdOt(int idOt) {
        this.idOt = idOt;
    }

    public String getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(String dateMvt) {
        this.dateMvt = dateMvt;
    }

    public int getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(int idDemandeur) {
        this.idDemandeur = idDemandeur;
    }

    public String getTypeMvt() {
        return typeMvt;
    }

    public void setTypeMvt(String typeMvt) {
        this.typeMvt = typeMvt;
    }
}
