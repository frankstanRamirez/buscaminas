package com.miuniversidad.buscaminas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private EditText etPlayerName;
    private Switch swTheme;
    private Switch swSound;
    private TextView tvThemeLabel;
    private AppPreferences appPreferences;
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        appPreferences = new AppPreferences(this);
        themeManager = new ThemeManager(this, appPreferences);
        
        // Aplicar tema ANTES de setContentView
        if (themeManager.isDarkTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPlayerName = findViewById(R.id.etPlayerName);
        swTheme = findViewById(R.id.swTheme);
        swSound = findViewById(R.id.swSound);
        tvThemeLabel = findViewById(R.id.tvThemeLabel);

        // Cargar valores actuales
        etPlayerName.setText(appPreferences.getPlayerName());
        swTheme.setChecked(themeManager.isDarkTheme());
        swSound.setChecked(appPreferences.isSoundEnabled());

        // Listeners
        etPlayerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appPreferences.setPlayerName(etPlayerName.getText().toString());
            }
        });

        swTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.Theme newTheme = isChecked 
                ? AppPreferences.Theme.DARK 
                : AppPreferences.Theme.LIGHT;
            appPreferences.setTheme(newTheme);
            
            // Aplicar tema
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            
            actualizarLabelTema();
            
            // Recrear después
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                recreate();
            }, 300);
        });

        swSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            appPreferences.setSoundEnabled(isChecked);
        });

        actualizarLabelTema();
    }

    private void actualizarLabelTema() {
        String tema = themeManager.isDarkTheme() ? "🌙 Oscuro" : "☀️ Claro";
        tvThemeLabel.setText("Tema: " + tema);
    }
}

