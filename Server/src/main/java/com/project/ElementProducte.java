package com.project;

public class ElementProducte {
    String id;
    String nom;
    double preu;
    String descripcio;
    String imatge;
    String categoria;


    public ElementProducte(String id, String nom, double preu, String descripcio, String imatge, String categoria) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.descripcio = descripcio;
        this.imatge = imatge;
        this.categoria = categoria;
    }
}

