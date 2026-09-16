package com.miuniversidad.buscaminas;

/**
 * Enum para los niveles de dificultad del juego
 */
public enum GameDifficulty {
    EASY("Fácil", 8, 8, 10),
    MEDIUM("Medio", 10, 10, 30),
    HARD("Difícil", 12, 12, 60);

    public final String nombre;
    public final int filas;
    public final int columnas;
    public final int minas;

    GameDifficulty(String nombre, int filas, int columnas, int minas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
    }

    public static GameDifficulty fromString(String difficulty) {
        try {
            return GameDifficulty.valueOf(difficulty);
        } catch (IllegalArgumentException e) {
            return EASY; // Por defecto
        }
    }
}
