package com.miuniversidad.buscaminas;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager para persistencia de datos usando SharedPreferences y JSON (Gson)
 * Maneja guardado/carga de:
 * - Estado de partida en progreso
 * - Logros desbloqueados
 * - Estadísticas del jugador
 */
public class DataManager {
    private static final String PREFS_NAME = "BuscaminasPrefs";
    private static final String KEY_SAVED_GAME = "saved_game_state";
    private static final String KEY_ACHIEVEMENTS = "achievements";
    private static final String KEY_STATISTICS = "statistics";

    private SharedPreferences prefs;
    private Gson gson;

    public DataManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new GsonBuilder().create();
    }

    /**
     * Guarda el estado actual de la partida
     */
    public void saveGameState(GameState gameState) {
        String json = gson.toJson(gameState);
        prefs.edit().putString(KEY_SAVED_GAME, json).apply();
    }

    /**
     * Carga el estado guardado de la partida (si existe)
     */
    public GameState loadGameState() {
        String json = prefs.getString(KEY_SAVED_GAME, null);
        if (json == null) {
            return null;
        }
        try {
            return gson.fromJson(json, GameState.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina el estado guardado de la partida
     */
    public void clearSavedGame() {
        prefs.edit().remove(KEY_SAVED_GAME).apply();
    }

    /**
     * Verifica si hay una partida guardada
     */
    public boolean hasSavedGame() {
        return prefs.contains(KEY_SAVED_GAME);
    }

    /**
     * Guarda el mapa de logros
     */
    public void saveAchievements(Map<String, Achievement> achievements) {
        String json = gson.toJson(achievements);
        prefs.edit().putString(KEY_ACHIEVEMENTS, json).apply();
    }

    /**
     * Carga el mapa de logros
     */
    public Map<String, Achievement> loadAchievements() {
        String json = prefs.getString(KEY_ACHIEVEMENTS, null);
        if (json == null) {
            return null;
        }
        try {
            return gson.fromJson(json, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda las estadísticas del jugador
     */
    public void saveStatistics(GameStatistics statistics) {
        String json = gson.toJson(statistics);
        prefs.edit().putString(KEY_STATISTICS, json).apply();
    }

    /**
     * Carga las estadísticas del jugador
     */
    public GameStatistics loadStatistics() {
        String json = prefs.getString(KEY_STATISTICS, null);
        if (json == null) {
            return new GameStatistics();
        }
        try {
            return gson.fromJson(json, GameStatistics.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new GameStatistics();
        }
    }

    /**
     * Limpia todos los datos guardados (para testing)
     */
    public void clearAllData() {
        prefs.edit().clear().apply();
    }
}
