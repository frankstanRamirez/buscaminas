package com.miuniversidad.buscaminas;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class AchievementsActivity extends AppCompatActivity {

    private LinearLayout containerLogros;
    private TextView tvTotalLogros;
    private AchievementManager achievementManager;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        containerLogros = findViewById(R.id.containerLogros);
        tvTotalLogros = findViewById(R.id.tvTotalLogros);

        dataManager = new DataManager(this);
        achievementManager = new AchievementManager(dataManager);

        mostrarLogros();
    }

    private void mostrarLogros() {
        containerLogros.removeAllViews();

        Map<String, Achievement> achievements = achievementManager.getAllAchievements();
        int completados = achievementManager.getCompletedAchievementsCount();
        float porcentaje = achievementManager.getAchievementPercentage();

        tvTotalLogros.setText(String.format("Logros: %d/%d (%.0f%%)", completados, achievements.size(), porcentaje));

        for (Achievement achievement : achievements.values()) {
            LinearLayout itemLogro = crearItemLogro(achievement);
            containerLogros.addView(itemLogro);
        }
    }

    private LinearLayout crearItemLogro(Achievement achievement) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(16, 12, 16, 12);
        itemLayout.setBackgroundColor(achievement.isCompletado() 
            ? getColor(R.color.casilla_vacia) 
            : getColor(R.color.casilla_cubierta));
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);
        itemLayout.setLayoutParams(params);

        // Título
        TextView tvTitulo = new TextView(this);
        tvTitulo.setText(achievement.getTitulo());
        tvTitulo.setTextSize(16f);
        tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitulo.setTextColor(getColor(R.color.header_title));
        itemLayout.addView(tvTitulo);

        // Descripción
        TextView tvDesc = new TextView(this);
        tvDesc.setText(achievement.getDescripcion());
        tvDesc.setTextSize(14f);
        tvDesc.setTextColor(getColor(R.color.header_subtitle));
        tvDesc.setPadding(0, 4, 0, 0);
        itemLayout.addView(tvDesc);

        // Estado
        TextView tvEstado = new TextView(this);
        tvEstado.setText(achievement.isCompletado() ? "✅ Desbloqueado" : "🔒 Bloqueado");
        tvEstado.setTextSize(12f);
        tvEstado.setTextColor(achievement.isCompletado() 
            ? getColor(R.color.numero_2) 
            : getColor(R.color.numero_3));
        tvEstado.setPadding(0, 4, 0, 0);
        itemLayout.addView(tvEstado);

        return itemLayout;
    }
}
