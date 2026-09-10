package com.miuniversidad.buscaminas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para guardar las estadísticas globales del jugador
 */
public class GameStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalPartidas = 0;
    private int partidas_ganadas = 0;
    private int partidas_perdidas = 0;
    private long tiempoTotalJuego = 0;  // en milisegundos
    private long mejorTiempo = Long.MAX_VALUE;
    private List<GameScore> historialPuntuaciones = new ArrayList<>();

    public GameStatistics() {}

    // Método para registrar una victoria
    public void registrarVictoria(long tiempoPartida) {
        totalPartidas++;
        partidas_ganadas++;
        tiempoTotalJuego += tiempoPartida;
        if (tiempoPartida < mejorTiempo) {
            mejorTiempo = tiempoPartida;
        }
    }

    // Método para registrar una derrota
    public void registrarDerrota(long tiempoPartida) {
        totalPartidas++;
        partidas_perdidas++;
        tiempoTotalJuego += tiempoPartida;
    }

    // Agregar a historial
    public void agregarAlHistorial(GameScore score) {
        historialPuntuaciones.add(score);
        // Mantener solo las últimas 100 partidas
        if (historialPuntuaciones.size() > 100) {
            historialPuntuaciones.remove(0);
        }
    }

    // Getters y Setters
    public int getTotalPartidas() {
        return totalPartidas;
    }

    public int getPartidosGanadas() {
        return partidas_ganadas;
    }

    public int getPartidosPerdidas() {
        return partidas_perdidas;
    }

    public long getTiempoTotalJuego() {
        return tiempoTotalJuego;
    }

    public long getMejorTiempo() {
        return mejorTiempo == Long.MAX_VALUE ? 0 : mejorTiempo;
    }

    public List<GameScore> getHistorialPuntuaciones() {
        return historialPuntuaciones;
    }

    public double getWinRate() {
        if (totalPartidas == 0) return 0;
        return (double) partidas_ganadas / totalPartidas * 100;
    }

    public void setTotalPartidas(int totalPartidas) {
        this.totalPartidas = totalPartidas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
    }

    public void setTiempoTotalJuego(long tiempoTotalJuego) {
        this.tiempoTotalJuego = tiempoTotalJuego;
    }

    public void setMejorTiempo(long mejorTiempo) {
        this.mejorTiempo = mejorTiempo;
    }

    public void setHistorialPuntuaciones(List<GameScore> historialPuntuaciones) {
        this.historialPuntuaciones = historialPuntuaciones;
    }

    /**
     * Clase interna para representar una puntuación en el historial
     */
    public static class GameScore implements Serializable {
        private static final long serialVersionUID = 1L;

        public long timestamp;
        public long tiempoPartida;
        public boolean gano;
        public int minasEvitadas;
        public int casillasDescubiertas;

        public GameScore(long tiempoPartida, boolean gano, int minasEvitadas, int casillasDescubiertas) {
            this.timestamp = System.currentTimeMillis();
            this.tiempoPartida = tiempoPartida;
            this.gano = gano;
            this.minasEvitadas = minasEvitadas;
            this.casillasDescubiertas = casillasDescubiertas;
        }
    }
}
