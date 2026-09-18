package com.miuniversidad.buscaminas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText etPlayerName;
    private Spinner spinnerDificultad;
    private Switch swTheme;
    private Switch swSound;
    private TextView tvThemeLabel;
    private TextView tvSoundLabel;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar tema ANTES de setContentView
        appPreferences = new AppPreferences(this);
        aplicarTema();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPlayerName = findViewById(R.id.etPlayerName);
        spinnerDificultad = findViewById(R.id.spinnerDificultad);
        swTheme = findViewById(R.id.swTheme);
        swSound = findViewById(R.id.swSound);
        tvThemeLabel = findViewById(R.id.tvThemeLabel);
        tvSoundLabel = findViewById(R.id.tvSoundLabel);

        // Cargar valores actuales
        etPlayerName.setText(appPreferences.getPlayerName());
        swSound.setChecked(appPreferences.isSoundEnabled());
        
        // Cargar tema actual (true = oscuro, false = claro)
        boolean isDarkTheme = appPreferences.getTheme() == AppPreferences.Theme.DARK;
        swTheme.setChecked(isDarkTheme);

        // Configurar spinner de dificultad
        String[] dificultades = {"Fácil (8x8, 10 minas)", "Medio (10x10, 30 minas)", "Difícil (12x12, 60 minas)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dificultades);
        spinnerDificultad.setAdapter(adapter);
        
        // Seleccionar la dificultad actual
        GameDifficulty currentDiff = appPreferences.getDifficulty();
        int diffIndex = 0;
        if (currentDiff == GameDifficulty.MEDIUM) diffIndex = 1;
        else if (currentDiff == GameDifficulty.HARD) diffIndex = 2;
        spinnerDificultad.setSelection(diffIndex);

        // Listeners
        etPlayerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appPreferences.setPlayerName(etPlayerName.getText().toString());
            }
        });

        spinnerDificultad.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                GameDifficulty[] dificultades = {GameDifficulty.EASY, GameDifficulty.MEDIUM, GameDifficulty.HARD};
                appPreferences.setDifficulty(dificultades[position]);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        swTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.Theme nuevoTema = isChecked ? AppPreferences.Theme.DARK : AppPreferences.Theme.LIGHT;
            appPreferences.setTheme(nuevoTema);
            actualizarLabelTema();
            
            // Reiniciar actividad para aplicar tema
            recreate();
        });

        swSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            appPreferences.setSoundEnabled(isChecked);
            actualizarLabelSonido();
        });

        actualizarLabelTema();
        actualizarLabelSonido();
    }

    private void actualizarLabelTema() {
        AppPreferences.Theme tema = appPreferences.getTheme();
        String label = (tema == AppPreferences.Theme.DARK) ? "🌙 Tema: Oscuro" : "☀️ Tema: Claro";
        tvThemeLabel.setText(label);
    }

    private void actualizarLabelSonido() {
        String sonido = appPreferences.isSoundEnabled() ? "🔊 Activado" : "🔇 Desactivado";
        tvSoundLabel.setText("Sonidos: " + sonido);
    }

    private void aplicarTema() {
        AppPreferences.Theme tema = appPreferences.getTheme();
        if (tema == AppPreferences.Theme.DARK) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }
}
