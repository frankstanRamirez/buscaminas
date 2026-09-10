package com.miuniversidad.buscaminas;

import java.io.Serializable;

/**
 * Modelo para un logro en el juego
 */
public class Achievement implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titulo;
    private String descripcion;
    private String icono;           // Emoji o referencia a recurso
    private boolean completado;
    private int progreso;           // 0-100 para logros progresivos
    private long tiempoCompletado;  // Timestamp cuando se completó

    public enum AchievementType {
        FIRST_WIN("first_win", "🏆 Primer Triunfo", "Gana tu primera partida"),
        SPEED_MASTER("speed_master", "⚡ Maestro de Velocidad", "Gana una partida en menos de 30 segundos"),
        MINESWEEPER_PRO("minesweeper_pro", "👑 Experto Buscaminas", "Gana 5 partidas"),
        PERFECT_GAME("perfect_game", "💯 Juego Perfecto", "Gana sin cometer errores"),
        SURVIVOR("survivor", "🛡️ Superviviente", "Continúa 3 partidas seguidas"),
        TIME_ATTACK("time_attack", "⏰ Desafío de Tiempo", "Completa 10 partidas"),
        COLLECTOR("collector", "🎯 Coleccionista", "Desbloquea 5 logros");

        public final String id;
        public final String titulo;
        public final String descripcion;

        AchievementType(String id, String titulo, String descripcion) {
            this.id = id;
            this.titulo = titulo;
            this.descripcion = descripcion;
        }
    }

    public Achievement(String id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completado = false;
        this.progreso = 0;
        this.tiempoCompletado = 0;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
        if (completado) {
            this.tiempoCompletado = System.currentTimeMillis();
            this.progreso = 100;
        }
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = Math.min(100, progreso);
        if (this.progreso >= 100) {
            setCompletado(true);
        }
    }

    public long getTiempoCompletado() {
        return tiempoCompletado;
    }

    public void setTiempoCompletado(long tiempoCompletado) {
        this.tiempoCompletado = tiempoCompletado;
    }
}
