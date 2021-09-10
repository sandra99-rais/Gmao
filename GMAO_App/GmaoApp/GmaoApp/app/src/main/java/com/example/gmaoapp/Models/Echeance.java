package com.example.gmaoapp.Models;

public class Echeance {
    int idEcheance;
    int dureeTravEch;
    int idGamme;
    String dateEcheance;

    public Echeance(int idEcheance, int dureeTravEch, int idGamme, String dateEcheance) {
        this.idEcheance = idEcheance;
        this.dureeTravEch = dureeTravEch;
        this.idGamme = idGamme;
        this.dateEcheance = dateEcheance;
    }

    public int getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(int idEcheance) {
        this.idEcheance = idEcheance;
    }

    public int getDureeTravEch() {
        return dureeTravEch;
    }

    public void setDureeTravEch(int dureeTravEch) {
        this.dureeTravEch = dureeTravEch;
    }

    public int getIdGamme() {
        return idGamme;
    }

    public void setIdGamme(int idGamme) {
        this.idGamme = idGamme;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

}
