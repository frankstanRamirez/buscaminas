package com.miuniversidad.buscaminas;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager para manejar la lógica de logros
 */
public class AchievementManager {
    private Map<String, Achievement> achievements;
    private DataManager dataManager;

    public AchievementManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeAchievements();
    }

    /**
     * Inicializa todos los logros disponibles
     */
    private void initializeAchievements() {
        achievements = new HashMap<>();

        // Crear logros iniciales
        achievements.put("first_win", new Achievement("first_win", "🏆 Primer Triunfo", "Gana tu primera partida"));
        achievements.put("speed_master", new Achievement("speed_master", "⚡ Maestro de Velocidad", "Gana una partida en menos de 30 segundos"));
        achievements.put("minesweeper_pro", new Achievement("minesweeper_pro", "👑 Experto Buscaminas", "Gana 5 partidas"));
        achievements.put("perfect_game", new Achievement("perfect_game", "💯 Juego Perfecto", "Gana sin errores"));
        achievements.put("survivor", new Achievement("survivor", "🛡️ Superviviente", "Sobrevive 3 partidas seguidas"));
        achievements.put("time_attack", new Achievement("time_attack", "⏰ Desafío de Tiempo", "Completa 10 partidas"));
        achievements.put("collector", new Achievement("collector", "🎯 Coleccionista", "Desbloquea 5 logros"));

        // Cargar logros guardados si existen
        Map<String, Achievement> savedAchievements = dataManager.loadAchievements();
        if (savedAchievements != null && !savedAchievements.isEmpty()) {
            achievements.putAll(savedAchievements);
        }
    }

    /**
     * Desbloquea un logro específico
     */
    public boolean unlockAchievement(String achievementId) {
        Achievement achievement = achievements.get(achievementId);
        if (achievement != null && !achievement.isCompletado()) {
            achievement.setCompletado(true);
            saveAchievements();
            return true;
        }
        return false;
    }

    /**
     * Actualiza el progreso de un logro
     */
    public void updateAchievementProgress(String achievementId, int progress) {
        Achievement achievement = achievements.get(achievementId);
        if (achievement != null) {
            achievement.setProgreso(progress);
            saveAchievements();
        }
    }

    /**
     * Verifica y desbloquea logros basado en el resultado de la partida
     */
    public void checkAchievementsForGameEnd(boolean gano, long tiempoPartida, 
                                           int totalPartidas, int totalVictorias,
                                           GameStatistics stats) {
        if (gano) {
            // Primer triunfo
            if (totalVictorias == 1) {
                unlockAchievement("first_win");
            }

            // Maestro de velocidad (menos de 30 segundos)
            if (tiempoPartida < 30000) {
                unlockAchievement("speed_master");
            }

            // Experto (5 victorias)
            if (totalVictorias >= 5) {
                unlockAchievement("minesweeper_pro");
            }
        }

        // Desafío de tiempo (10 partidas)
        if (totalPartidas >= 10) {
            unlockAchievement("time_attack");
        }

        // Coleccionista (5 logros desbloqueados)
        int completados = getCompletedAchievementsCount();
        if (completados >= 5) {
            unlockAchievement("collector");
        }
    }

    /**
     * Obtiene todos los logros
     */
    public Map<String, Achievement> getAllAchievements() {
        return new HashMap<>(achievements);
    }

    /**
     * Obtiene un logro específico
     */
    public Achievement getAchievement(String id) {
        return achievements.get(id);
    }

    /**
     * Obtiene el número de logros completados
     */
    public int getCompletedAchievementsCount() {
        int count = 0;
        for (Achievement achievement : achievements.values()) {
            if (achievement.isCompletado()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Obtiene el porcentaje de logros completados
     */
    public float getAchievementPercentage() {
        return (float) getCompletedAchievementsCount() / achievements.size() * 100;
    }

    /**
     * Guarda los logros
     */
    private void saveAchievements() {
        dataManager.saveAchievements(achievements);
    }

    /**
     * Reinicia todos los logros (para testing)
     */
    public void resetAchievements() {
        for (Achievement achievement : achievements.values()) {
            achievement.setCompletado(false);
            achievement.setProgreso(0);
        }
        saveAchievements();
    }
}
