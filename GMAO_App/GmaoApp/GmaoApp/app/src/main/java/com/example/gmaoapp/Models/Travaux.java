package com.example.gmaoapp.Models;

public class Travaux {

    int id;
    String typeTravaux;

    public Travaux(int id, String typeTravaux) {
        this.id = id;
        this.typeTravaux = typeTravaux;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeTravaux() {
        return typeTravaux;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }
}
