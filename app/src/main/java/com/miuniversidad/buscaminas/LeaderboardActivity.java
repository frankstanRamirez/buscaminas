package com.miuniversidad.buscaminas;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private LinearLayout containerLeaderboard;
    private TextView tvTitulo;
    private DataManager dataManager;
    private Leaderboard leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Aplicar tema
        AppPreferences appPreferences = new AppPreferences(this);
        aplicarTemaCorrectamente(appPreferences);
        
        setContentView(R.layout.activity_leaderboard);

        containerLeaderboard = findViewById(R.id.containerLeaderboard);
        tvTitulo = findViewById(R.id.tvTitulo);

        dataManager = new DataManager(this);
        leaderboard = dataManager.loadLeaderboard();

        mostrarLeaderboard();
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

    private void mostrarLeaderboard() {
        containerLeaderboard.removeAllViews();

        List<Leaderboard.LeaderboardEntry> topEntries = leaderboard.getTop(10);

        if (topEntries.isEmpty()) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("📭 Aún no hay registros");
            tvEmpty.setTextSize(16f);
            tvEmpty.setPadding(16, 16, 16, 16);
            tvEmpty.setTextColor(getColor(R.color.header_subtitle));
            containerLeaderboard.addView(tvEmpty);
            return;
        }

        int posicion = 1;
        for (Leaderboard.LeaderboardEntry entry : topEntries) {
            LinearLayout itemLayout = crearItemLeaderboard(entry, posicion);
            containerLeaderboard.addView(itemLayout);
            posicion++;
        }
    }

    private LinearLayout crearItemLeaderboard(Leaderboard.LeaderboardEntry entry, int posicion) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(16, 12, 16, 12);
        itemLayout.setBackgroundColor(posicion <= 3 ? getColor(R.color.casilla_vacia) : getColor(R.color.background_light));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 4, 0, 4);
        itemLayout.setLayoutParams(params);

        // Posición/Medal
        TextView tvPosicion = new TextView(this);
        String medal = posicion == 1 ? "🥇" : posicion == 2 ? "🥈" : posicion == 3 ? "🥉" : "#" + posicion;
        tvPosicion.setText(medal);
        tvPosicion.setTextSize(18f);
        tvPosicion.setTypeface(null, android.graphics.Typeface.BOLD);
        tvPosicion.setPadding(0, 0, 16, 0);
        itemLayout.addView(tvPosicion);

        // Nombre
        TextView tvNombre = new TextView(this);
        tvNombre.setText(entry.playerName);
        tvNombre.setTextSize(16f);
        tvNombre.setTypeface(null, android.graphics.Typeface.BOLD);
        tvNombre.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        itemLayout.addView(tvNombre);

        // Dificultad
        TextView tvDificultad = new TextView(this);
        tvDificultad.setText(entry.difficulty.nombre);
        tvDificultad.setTextSize(12f);
        tvDificultad.setTextColor(getColor(R.color.header_subtitle));
        tvDificultad.setPadding(8, 0, 8, 0);
        itemLayout.addView(tvDificultad);

        // Tiempo
        TextView tvTiempo = new TextView(this);
        tvTiempo.setText(entry.getTiempoFormato());
        tvTiempo.setTextSize(14f);
        tvTiempo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTiempo.setTextColor(getColor(R.color.numero_1));
        itemLayout.addView(tvTiempo);

        return itemLayout;
    }
}
