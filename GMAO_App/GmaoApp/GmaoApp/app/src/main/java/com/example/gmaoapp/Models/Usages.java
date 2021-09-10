package com.example.gmaoapp.Models;

public class Usages {
    int idUsages;
    String dateUsage;
    int usageRel;
    int idMachine;
    int idCompteur;

    public Usages(String dateUsage, int usageRel, int idMachine, int idCompteur) {
        this.dateUsage = dateUsage;
        this.usageRel = usageRel;
        this.idMachine = idMachine;
        this.idCompteur = idCompteur;
    }

    public int getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(int idMachine) {
        this.idMachine = idMachine;
    }

    public int getIdCompteur() {
        return idCompteur;
    }

    public void setIdCompteur(int idCompteur) {
        this.idCompteur = idCompteur;
    }
    public int getIdUsages() {
        return idUsages;
    }

    public void setIdUsages(int idUsages) {
        this.idUsages = idUsages;
    }

    public String getDateUsage() {
        return dateUsage;
    }

    public void setDateUsage(String dateUsage) {
        this.dateUsage = dateUsage;
    }

    public int getUsageRel() {
        return usageRel;
    }

    public void setUsageRel(int usageRel) {
        this.usageRel = usageRel;
    }
}
