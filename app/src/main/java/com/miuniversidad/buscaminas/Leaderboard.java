package com.miuniversidad.buscaminas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modelo para el Leaderboard de mejores tiempos
 */
public class Leaderboard implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_ENTRIES = 50;

    private List<LeaderboardEntry> entries;

    public Leaderboard() {
        this.entries = new ArrayList<>();
    }

    /**
     * Agrega una nueva entrada al leaderboard
     */
    public boolean addEntry(String playerName, long tiempoMs, GameDifficulty difficulty, int score) {
        LeaderboardEntry entry = new LeaderboardEntry(playerName, tiempoMs, difficulty, score);
        
        // Solo agregar si está entre los mejores
        if (entries.size() < MAX_ENTRIES || tiempoMs < entries.get(entries.size() - 1).tiempoMs) {
            entries.add(entry);
            // Ordenar por tiempo (ascendente)
            Collections.sort(entries);
            
            // Mantener solo los mejores
            if (entries.size() > MAX_ENTRIES) {
                entries = new ArrayList<>(entries.subList(0, MAX_ENTRIES));
            }
            return true;
        }
        return false;
    }

    /**
     * Obtiene los top N del leaderboard
     */
    public List<LeaderboardEntry> getTop(int cantidad) {
        return new ArrayList<>(entries.subList(0, Math.min(cantidad, entries.size())));
    }

    /**
     * Obtiene todos los entries
     */
    public List<LeaderboardEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Limpia el leaderboard
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Clase interna para una entrada del leaderboard
     */
    public static class LeaderboardEntry implements Comparable<LeaderboardEntry>, Serializable {
        private static final long serialVersionUID = 1L;

        public String playerName;
        public long tiempoMs;
        public GameDifficulty difficulty;
        public long timestamp;
        public int score;

        public LeaderboardEntry(String playerName, long tiempoMs, GameDifficulty difficulty, int score) {
            this.playerName = playerName != null ? playerName : "Anónimo";
            this.tiempoMs = tiempoMs;
            this.difficulty = difficulty;
            this.score = score;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(LeaderboardEntry other) {
            // Ordenar por tiempo (menor es mejor)
            return Long.compare(this.tiempoMs, other.tiempoMs);
        }

        public String getTiempoFormato() {
            long segundos = tiempoMs / 1000;
            long minutos = segundos / 60;
            long segs = segundos % 60;
            
            if (minutos > 0) {
                return String.format("%d:%02d", minutos, segs);
            } else {
                return String.format("%ds", segs);
            }
        }
    }
}
