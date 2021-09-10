package com.example.gmaoapp.Models;

public class Equipements {
    int Ot;
    int idMachine;
    int idEns;
    int idSsEns;
    int idEtat;
    int idSec;

    public Equipements( int idMachine, int idEns, int idSsEns, int idEtat,int idSec) {

        this.idMachine = idMachine;
        this.idEns = idEns;
        this.idSsEns = idSsEns;
        this.idEtat = idEtat;
        this.idSec=idSec;
    }

    public int getIdSec() {
        return idSec;
    }

    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    public Equipements(int idMachine,int idSec) {

        this.idMachine = idMachine;
        this.idSec = idSec;

    }public Equipements(int idMachine) {

        this.idMachine = idMachine;

    }

    public int getOt() {
        return Ot;
    }

    public void setOt(int Ot) {
        this.Ot = Ot;
    }

    public int getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(int idMachine) {
        this.idMachine = idMachine;
    }

    public int getIdEns() {
        return idEns;
    }

    public void setIdEns(int idEns) {
        this.idEns = idEns;
    }

    public int getIdSsEns() {
        return idSsEns;
    }

    public void setIdSsEns(int idSsEns) {
        this.idSsEns = idSsEns;
    }

    public int getIdEtat() {
        return idEtat;
    }

    public void setIdEtat(int idEtat) {
        this.idEtat = idEtat;
    }
}
