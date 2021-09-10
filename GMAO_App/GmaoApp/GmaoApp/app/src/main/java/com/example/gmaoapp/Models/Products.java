package com.example.gmaoapp.Models;

public class Products {
    int idPdt;
    String codePdt;
    String desginationPdt;
    int qte;
    double prix;


    public Products(String codePdt, String desginationPdt, int qte, double prix) {
        this.codePdt = codePdt;
        this.desginationPdt = desginationPdt;
        this.qte = qte;
        this.prix = prix;
    }

    public int getIdPdt() {
        return idPdt;
    }

    public void setIdPdt(int idPdt) {
        this.idPdt = idPdt;
    }

    public String getCodePdt() {
        return codePdt;
    }

    public void setCodePdt(String codePdt) {
        this.codePdt = codePdt;
    }

    public String getDesginationPdt() {
        return desginationPdt;
    }

    public void setDesginationPdt(String desginationPdt) {
        this.desginationPdt = desginationPdt;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}

