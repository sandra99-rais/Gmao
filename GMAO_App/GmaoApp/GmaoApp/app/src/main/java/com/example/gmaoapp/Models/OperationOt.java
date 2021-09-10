package com.example.gmaoapp.Models;

public class OperationOt {
    public int idOpBt;
    public int idOt;
    public int idOp;
    public String dateOp;
    public int idGamme;
    public int idMachine;
    public String codeOp;
    public String codeMachine;
    public String codeGamme;
    public String typeOp;

    public OperationOt(int idOp, String dateOp,int idGamme,int idMachine,String codeOp,String codeMachine,String codeGamme,String typeOp) {
        this.idOp = idOp;
        this.dateOp = dateOp;
        this.idGamme=idGamme;
        this.idMachine=idMachine;
        this.codeOp=codeOp;
        this.codeMachine=codeMachine;
        this.codeGamme=codeGamme;
        this.typeOp=typeOp;

    }

    public int getIdOpBt() {
        return idOpBt;
    }

    public void setIdOpBt(int idOpBt) {
        this.idOpBt = idOpBt;
    }

    public int getIdOt() {
        return idOt;
    }

    public void setIdOt(int idOt) {
        this.idOt = idOt;
    }

    public int getIdOp() {
        return idOp;
    }

    public void setIdOp(int idOp) {
        this.idOp = idOp;
    }

    public String getDateOp() {
        return dateOp;
    }

    public void setDateOp(String dateOp) {
        this.dateOp = dateOp;
    }

    public int getIdGamme() {
        return idGamme;
    }

    public void setIdGamme(int idGamme) {
        this.idGamme = idGamme;
    }

    public int getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(int idMachine) {
        this.idMachine = idMachine;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getCodeMachine() {
        return codeMachine;
    }

    public void setCodeMachine(String codeMachine) {
        this.codeMachine = codeMachine;
    }

    public String getCodeGamme() {
        return codeGamme;
    }

    public void setCodeGamme(String codeGamme) {
        this.codeGamme = codeGamme;
    }

    public String getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(String typeOp) {
        this.typeOp = typeOp;
    }
}
