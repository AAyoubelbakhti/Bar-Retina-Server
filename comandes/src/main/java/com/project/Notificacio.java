package com.project;

public class Notificacio {
    private int idComanda;
    private String estatComanda;
    private String dataComanda;
    private String comanda;
    private boolean pagada;
    private int idTaula;
    private double preuComanda;
    private int idCambrer;
    private boolean llegida;

    public Notificacio(int idComanda, String estatComanda, String dataComanda, String comanda, boolean pagada, int idTaula, double preuComanda, int idCambrer) {
        this.idComanda = idComanda;
        this.estatComanda = estatComanda;
        this.dataComanda = dataComanda;
        this.comanda = comanda;
        this.pagada = pagada;
        this.idTaula = idTaula;
        this.preuComanda = preuComanda;
        this.idCambrer = idCambrer;
        this.llegida = false;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public String getEstatComanda() {
        return estatComanda;
    }

    public String getDataComanda() {
        return dataComanda;
    }

    public String getComanda() {
        return comanda;
    }

    public boolean isPagada() {
        return pagada;
    }

    public int getIdTaula() {
        return idTaula;
    }

    public double getPreuComanda() {
        return preuComanda;
    }

    public int getIdCambrer() {
        return idCambrer;
    }

    public boolean isLlegida() {
        return llegida;
    }

    public void setLlegida(boolean llegida) {
        this.llegida = llegida;
    }

    @Override
    public String toString() {
        return "Notificacio{" +
                "idComanda=" + idComanda +
                ", estatComanda='" + estatComanda + '\'' +
                ", dataComanda='" + dataComanda + '\'' +
                ", comanda='" + comanda + '\'' +
                ", pagada=" + pagada +
                ", idTaula=" + idTaula +
                ", preuComanda=" + preuComanda +
                ", idCambrer=" + idCambrer +
                ", llegida=" + llegida +
                '}';
    }
}
