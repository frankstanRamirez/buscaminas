package com.miuniversidad.buscaminas;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manager para las preferencias de la aplicación
 */
public class AppPreferences {
    private static final String PREFS_NAME = "BuscaminasAppPrefs";
    private static final String KEY_THEME = "theme";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_PLAYER_NAME = "player_name";

    private SharedPreferences prefs;

    public enum Theme {
        LIGHT("light"),
        DARK("dark");

        public final String value;

        Theme(String value) {
            this.value = value;
        }

        public static Theme fromString(String value) {
            for (Theme theme : Theme.values()) {
                if (theme.value.equals(value)) {
                    return theme;
                }
            }
            return LIGHT;
        }
    }

    public AppPreferences(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Tema
    public void setTheme(Theme theme) {
        prefs.edit().putString(KEY_THEME, theme.value).apply();
    }

    public Theme getTheme() {
        String themeStr = prefs.getString(KEY_THEME, Theme.LIGHT.value);
        return Theme.fromString(themeStr);
    }

    // Sonidos
    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    // Dificultad
    public void setDifficulty(GameDifficulty difficulty) {
        prefs.edit().putString(KEY_DIFFICULTY, difficulty.name()).apply();
    }

    public GameDifficulty getDifficulty() {
        String diffStr = prefs.getString(KEY_DIFFICULTY, GameDifficulty.EASY.name());
        return GameDifficulty.fromString(diffStr);
    }

    // Nombre de jugador
    public void setPlayerName(String name) {
        prefs.edit().putString(KEY_PLAYER_NAME, name).apply();
    }

    public String getPlayerName() {
        return prefs.getString(KEY_PLAYER_NAME, "Jugador");
    }
}
