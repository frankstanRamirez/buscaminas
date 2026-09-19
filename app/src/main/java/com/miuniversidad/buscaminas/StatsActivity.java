package com.miuniversidad.buscaminas;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class StatsActivity extends AppCompatActivity {

    private TextView tvTotalPartidas;
    private TextView tvVictorias;
    private TextView tvDerrotas;
    private TextView tvWinRate;
    private TextView tvMejorTiempo;
    private TextView tvTiempoTotal;
    private LinearLayout containerHistorial;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Aplicar tema
        AppPreferences appPreferences = new AppPreferences(this);
        aplicarTemaCorrectamente(appPreferences);
        
        setContentView(R.layout.activity_stats);

        tvTotalPartidas = findViewById(R.id.tvTotalPartidas);
        tvVictorias = findViewById(R.id.tvVictorias);
        tvDerrotas = findViewById(R.id.tvDerrotas);
        tvWinRate = findViewById(R.id.tvWinRate);
        tvMejorTiempo = findViewById(R.id.tvMejorTiempo);
        tvTiempoTotal = findViewById(R.id.tvTiempoTotal);
        containerHistorial = findViewById(R.id.containerHistorial);

        dataManager = new DataManager(this);
        mostrarEstadisticas();
    }

    private void aplicarTemaCorrectamente(AppPreferences appPreferences) {
        AppPreferences.Theme tema = appPreferences.getTheme();
        int modoNocturno;
        
        if (tema == AppPreferences.Theme.DARK) {
            modoNocturno = AppCompatDelegate.MODE_NIGHT_YES;
        } else if (tema == AppPreferences.Theme.LIGHT) {
            modoNocturno = AppCompatDelegate.MODE_NIGHT_NO;
        } else {
            modoNocturno = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        
        AppCompatDelegate.setDefaultNightMode(modoNocturno);
    }

    private void mostrarEstadisticas() {
        GameStatistics stats = dataManager.loadStatistics();

        tvTotalPartidas.setText(String.format("Total Partidas: %d", stats.getTotalPartidas()));
        tvVictorias.setText(String.format("Victorias: %d", stats.getPartidosGanadas()));
        tvDerrotas.setText(String.format("Derrotas: %d", stats.getPartidosPerdidas()));
        tvWinRate.setText(String.format("Tasa de Victoria: %.1f%%", stats.getWinRate()));
        
        long mejorTiempo = stats.getMejorTiempo();
        tvMejorTiempo.setText(String.format("Mejor Tiempo: %s", formatearTiempo(mejorTiempo)));
        
        long tiempoTotal = stats.getTiempoTotalJuego();
        tvTiempoTotal.setText(String.format("Tiempo Total: %s", formatearTiempo(tiempoTotal)));

        // Mostrar historial
        containerHistorial.removeAllViews();
        for (GameStatistics.GameScore score : stats.getHistorialPuntuaciones()) {
            LinearLayout itemHistorial = crearItemHistorial(score);
            containerHistorial.addView(itemHistorial);
        }
    }

    private LinearLayout crearItemHistorial(GameStatistics.GameScore score) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(16, 8, 16, 8);
        itemLayout.setBackgroundColor(score.gano ? getColor(R.color.casilla_vacia) : getColor(R.color.casilla_mina_revelada));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 4, 0, 4);
        itemLayout.setLayoutParams(params);

        // Resultado
        TextView tvResultado = new TextView(this);
        tvResultado.setText(score.gano ? "✅ GANADO" : "❌ PERDIDO");
        tvResultado.setTextSize(14f);
        tvResultado.setTypeface(null, android.graphics.Typeface.BOLD);
        tvResultado.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        itemLayout.addView(tvResultado);

        // Tiempo
        TextView tvTiempo = new TextView(this);
        tvTiempo.setText(formatearTiempo(score.tiempoPartida));
        tvTiempo.setTextSize(12f);
        tvTiempo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        itemLayout.addView(tvTiempo);

        return itemLayout;
    }

    private String formatearTiempo(long milisegundos) {
        if (milisegundos == 0) return "N/A";
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        long segs = segundos % 60;
        
        if (minutos > 0) {
            return String.format("%d:%02d", minutos, segs);
        } else {
            return String.format("%ds", segs);
        }
    }
}
