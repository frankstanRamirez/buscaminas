package com.miuniversidad.buscaminas;

public class Casilla {

    private boolean tieneMina;
    private boolean descubierta;
    private boolean marcada;
    private int minasAdyacentes;

    public Casilla() {
        tieneMina = false;
        descubierta = false;
        marcada = false;
        minasAdyacentes = 0;
    }

    public boolean isTieneMina() {
        return tieneMina;
    }

    public void setTieneMina(boolean tieneMina) {
        this.tieneMina = tieneMina;
    }

    public boolean isDescubierta() {
        return descubierta;
    }

    public void setDescubierta(boolean descubierta) {
        this.descubierta = descubierta;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public void toggleMarcada() {
        this.marcada = !this.marcada;
    }

    public int getMinasAdyacentes() {
        return minasAdyacentes;
    }

    public void setMinasAdyacentes(int minasAdyacentes) {
        this.minasAdyacentes = minasAdyacentes;
    }
}
