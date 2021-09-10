package com.example.gmaoapp.Models;

import android.text.Editable;

import java.util.Date;

public class OrdreTravail {
    int idDI;

    String DateDemande;

    String desc;
    int idUser;
    int numOt;
    int cloture;
    int Lance;
    int idExecuteur;
    int idDemandeur;
    int idDegreUrgence;
    int idTypeTrav;
    int idPanne;
    String travEff;



    public String getTravEff() {
        return travEff;
    }

    public void setTravEff(String travEff) {
        this.travEff = travEff;
    }

    public String getDateDemande() {
        return DateDemande;
    }

    public void setDateDemande(String dateDemande) {
        DateDemande = dateDemande;
    }

    public int getIdPanne() {
        return idPanne;
    }

    public void setIdPanne(int idPanne) {
        this.idPanne = idPanne;
    }

    public int getIdTypeTrav() {
        return idTypeTrav;
    }

    public void setIdTypeTrav(int idTypeTrav) {
        this.idTypeTrav = idTypeTrav;
    }

    public int getIdDegreUrgence() {
        return idDegreUrgence;
    }

    public void setIdDegreUrgence(int idDegreUrgence) {
        this.idDegreUrgence = idDegreUrgence;
    }

    public int getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(int idDemandeur) {
        this.idDemandeur = idDemandeur;
    }

    public int getIdExecuteur() {
        return idExecuteur;
    }

    public void setIdExecuteur(int idExecuteur) {
        this.idExecuteur = idExecuteur;
    }

    public int getLance() {
        return Lance;
    }

    public void setLance(int lance) {
        Lance = lance;
    }

    public OrdreTravail(String dateDemande, int executeur, int nomDemandeur, int Panne, int typeTravail, String travEff) {
        idExecuteur = executeur;
        DateDemande = dateDemande;
        idDemandeur = nomDemandeur;

        this.idPanne=Panne;
        idTypeTrav=typeTravail;
        this.travEff=travEff;

    }
    public OrdreTravail(String dateDemande, int executeur, int DU, int typeTravail) {
        DateDemande = dateDemande;
        idExecuteur = executeur;
        idDegreUrgence = DU;
        idTypeTrav=typeTravail;
    }
    public OrdreTravail(int executeur, String dateDemande, int nomDemandeur, int numOt, int Panne, int typeTravail, int cloture, int Lance) {
        idExecuteur = executeur;
        DateDemande = dateDemande;
        this.idDemandeur = nomDemandeur;
        this.numOt = numOt;
        this.idPanne=Panne;
        idTypeTrav=typeTravail;
        this.cloture=cloture;
        this.Lance=Lance;
    }

    public OrdreTravail(String dateDemande, int executeur, int nomDemandeur, int degreUrgence, int panne, int typeTravail, String desc){
        idExecuteur = executeur;
        DateDemande = dateDemande;
        idDemandeur = nomDemandeur;
        idDegreUrgence = degreUrgence;
        idTypeTrav = typeTravail;
        idPanne = panne;
        this.desc = desc;

    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getCloture() {
        return cloture;
    }

    public void setCloture(int cloture) {
        this.cloture = cloture;
    }
    public int getNumOt() {
        return numOt;
    }

    public void setNumOt(int numOt) {
        this.numOt = numOt;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }


    public int getIdDI() {
        return idDI;
    }

    public void setIdDI(int idDI) {
        this.idDI = idDI;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
