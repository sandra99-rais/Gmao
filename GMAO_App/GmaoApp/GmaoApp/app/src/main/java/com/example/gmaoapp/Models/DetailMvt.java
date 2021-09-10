package com.example.gmaoapp.Models;

public class DetailMvt {
    int idDetMvt;
    int idMvt;
    int idPdt;
    int QteMvt;
    int qteTheo;
    int MP;
    Double PU;
    Double total;

    public DetailMvt(int idMvt, int idPdt, int qteMvt, Double PU, Double total) {
        this.idMvt = idMvt;
        this.idPdt = idPdt;
        QteMvt = qteMvt;
        this.PU = PU;
        this.total = total;
    }

    public DetailMvt(int idMvt, int idPdt, int qteMvt, int qteTheo, Double PU, int MP) {
        this.idMvt = idMvt;
        this.idPdt = idPdt;
        this.QteMvt = qteMvt;
        this.qteTheo = qteTheo;
        this.MP = MP;
        this.PU = PU;
    }

    public int getIdDetMvt() {
        return idDetMvt;
    }

    public void setIdDetMvt(int idDetMvt) {
        this.idDetMvt = idDetMvt;
    }

    public int getIdMvt() {
        return idMvt;
    }

    public void setIdMvt(int idMvt) {
        this.idMvt = idMvt;
    }

    public int getIdPdt() {
        return idPdt;
    }

    public void setIdPdt(int idPdt) {
        this.idPdt = idPdt;
    }

    public int getQteMvt() {
        return QteMvt;
    }

    public void setQteMvt(int qteMvt) {
        QteMvt = qteMvt;
    }

    public Double getPU() {
        return PU;
    }

    public void setPU(Double PU) {
        this.PU = PU;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getQteTheo() {
        return qteTheo;
    }

    public void setQteTheo(int qteTheo) {
        this.qteTheo = qteTheo;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

}
