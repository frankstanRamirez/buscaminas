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
    private Switch swSound;
    private TextView tvSoundLabel;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        appPreferences = new AppPreferences(this);

        etPlayerName = findViewById(R.id.etPlayerName);
        spinnerDificultad = findViewById(R.id.spinnerDificultad);
        swSound = findViewById(R.id.swSound);
        tvSoundLabel = findViewById(R.id.tvSoundLabel);

        // Cargar valores actuales
        etPlayerName.setText(appPreferences.getPlayerName());
        swSound.setChecked(appPreferences.isSoundEnabled());

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

        swSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            appPreferences.setSoundEnabled(isChecked);
            actualizarLabelSonido();
        });

        actualizarLabelSonido();
    }

    private void actualizarLabelSonido() {
        String sonido = appPreferences.isSoundEnabled() ? "🔊 Activado" : "🔇 Desactivado";
        tvSoundLabel.setText("Sonidos: " + sonido);
    }
}
