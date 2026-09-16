package com.miuniversidad.buscaminas;

/**
 * Sistema de puntuación con combos y multipliers
 */
public class ScoreSystem {
    private int totalScore = 0;
    private int currentCombo = 0;
    private int maxCombo = 0;
    private static final int BASE_POINTS = 10;
    private static final int COMBO_MULTIPLIER = 2;

    public ScoreSystem() {
        reset();
    }

    /**
     * Añade puntos por revelar una casilla segura
     */
    public int addSafeClick(int minasAdyacentes) {
        currentCombo++;
        
        // Puntos base: 10 + (minas adyacentes * 5)
        int basePoints = BASE_POINTS + (minasAdyacentes * 5);
        
        // Aplicar multiplicador por combo
        double multiplier = 1.0 + (currentCombo / 10.0);
        int earnedPoints = (int) (basePoints * multiplier);
        
        totalScore += earnedPoints;
        
        // Actualizar max combo
        if (currentCombo > maxCombo) {
            maxCombo = currentCombo;
        }
        
        return earnedPoints;
    }

    /**
     * Penaliza por pisar una mina
     */
    public void hitMine() {
        currentCombo = 0;
        totalScore = Math.max(0, totalScore - 50);
    }

    /**
     * Marca una bandera
     */
    public int flagCell() {
        currentCombo++;
        int points = 5 + (currentCombo / 5);
        totalScore += points;
        return points;
    }

    /**
     * Bonus por limpiar una zona sin minas (0 adyacentes)
     */
    public int clearZoneBonus() {
        int bonus = 25 * (1 + currentCombo / 5);
        totalScore += bonus;
        return bonus;
    }

    /**
     * Bonus de victoria
     */
    public int victoryBonus(long tiempoMs) {
        int timeBonus = (int) Math.max(0, 1000 - (tiempoMs / 100));
        int comboBonus = maxCombo * 50;
        int totalBonus = timeBonus + comboBonus;
        totalScore += totalBonus;
        return totalBonus;
    }

    /**
     * Resetea el sistema
     */
    public void reset() {
        totalScore = 0;
        currentCombo = 0;
        maxCombo = 0;
    }

    // Getters
    public int getTotalScore() {
        return totalScore;
    }

    public int getCurrentCombo() {
        return currentCombo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    /**
     * Obtiene el multiplicador actual
     */
    public double getCurrentMultiplier() {
        return 1.0 + (currentCombo / 10.0);
    }

    /**
     * Verifica si hay streak activo
     */
    public boolean hasActiveStreak() {
        return currentCombo >= 5;
    }

    /**
     * Obtiene mensaje de combo
     */
    public String getComboMessage() {
        if (currentCombo < 5) return "";
        if (currentCombo < 10) return "¡BUENA RACHA! x" + String.format("%.1f", getCurrentMultiplier());
        if (currentCombo < 20) return "¡INCREÍBLE! x" + String.format("%.1f", getCurrentMultiplier());
        return "¡ÉPICO! x" + String.format("%.1f", getCurrentMultiplier());
    }
}
