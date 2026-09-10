package com.miuniversidad.buscaminas;

import java.io.Serializable;

/**
 * Modelo para guardar el estado completo de una partida
 * Se puede serializar y guardar en SharedPreferences
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean[][] minas;           // Posición de las minas
    private boolean[][] descubiertas;    // Casillas descubiertas
    private int[][] minasAdyacentes;     // Contador de minas adyacentes
    private long tiempoElapsado;         // Tiempo transcurrido en ms
    private boolean juegoTerminado;
    private boolean gano;                // true si ganó, false si perdió
    private long timestampCreacion;      // Timestamp de cuando se creó la partida
    private int filas;
    private int columnas;
    private int totalMinas;

    public GameState(int filas, int columnas, int totalMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.totalMinas = totalMinas;
        this.minas = new boolean[filas][columnas];
        this.descubiertas = new boolean[filas][columnas];
        this.minasAdyacentes = new int[filas][columnas];
        this.tiempoElapsado = 0;
        this.juegoTerminado = false;
        this.gano = false;
        this.timestampCreacion = System.currentTimeMillis();
    }

    // Getters y Setters
    public boolean[][] getMinas() {
        return minas;
    }

    public void setMinas(boolean[][] minas) {
        this.minas = minas;
    }

    public boolean[][] getDescubiertas() {
        return descubiertas;
    }

    public void setDescubiertas(boolean[][] descubiertas) {
        this.descubiertas = descubiertas;
    }

    public int[][] getMinasAdyacentes() {
        return minasAdyacentes;
    }

    public void setMinasAdyacentes(int[][] minasAdyacentes) {
        this.minasAdyacentes = minasAdyacentes;
    }

    public long getTiempoElapsado() {
        return tiempoElapsado;
    }

    public void setTiempoElapsado(long tiempoElapsado) {
        this.tiempoElapsado = tiempoElapsado;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public boolean isGano() {
        return gano;
    }

    public void setGano(boolean gano) {
        this.gano = gano;
    }

    public long getTimestampCreacion() {
        return timestampCreacion;
    }

    public void setTimestampCreacion(long timestampCreacion) {
        this.timestampCreacion = timestampCreacion;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getTotalMinas() {
        return totalMinas;
    }

    public void setTotalMinas(int totalMinas) {
        this.totalMinas = totalMinas;
    }

    public boolean tieneSavedGame() {
        return timestampCreacion > 0 && tiempoElapsado > 0;
    }
}
