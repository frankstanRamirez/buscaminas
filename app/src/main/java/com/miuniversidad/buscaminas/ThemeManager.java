package com.miuniversidad.buscaminas;

import android.app.Activity;
import android.os.Build;

/**
 * Manager para manejar temas (claro/oscuro) en la app
 */
public class ThemeManager {
    private AppPreferences appPreferences;
    private Activity activity;

    public ThemeManager(Activity activity, AppPreferences appPreferences) {
        this.activity = activity;
        this.appPreferences = appPreferences;
    }

    /**
     * Aplica el tema guardado a la actividad
     */
    public void applyTheme() {
        AppPreferences.Theme currentTheme = appPreferences.getTheme();
        applyTheme(currentTheme);
    }

    /**
     * Aplica un tema específico
     */
    public void applyTheme(AppPreferences.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ - usar configuración del sistema
            switch (theme) {
                case DARK:
                    activity.getWindow().setDecorFitsSystemWindows(true);
                    break;
                case LIGHT:
                    activity.getWindow().setDecorFitsSystemWindows(true);
                    break;
            }
        }
        
        // Guardar tema en preferencias
        appPreferences.setTheme(theme);
    }

    /**
     * Alterna entre tema claro y oscuro
     */
    public AppPreferences.Theme toggleTheme() {
        AppPreferences.Theme currentTheme = appPreferences.getTheme();
        AppPreferences.Theme newTheme = currentTheme == AppPreferences.Theme.LIGHT 
            ? AppPreferences.Theme.DARK 
            : AppPreferences.Theme.LIGHT;
        
        appPreferences.setTheme(newTheme);
        return newTheme;
    }

    /**
     * Obtiene el tema actual
     */
    public AppPreferences.Theme getCurrentTheme() {
        return appPreferences.getTheme();
    }

    /**
     * Verifica si el tema actual es oscuro
     */
    public boolean isDarkTheme() {
        return appPreferences.getTheme() == AppPreferences.Theme.DARK;
    }
}
