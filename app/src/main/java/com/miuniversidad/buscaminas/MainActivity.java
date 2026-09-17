package com.miuniversidad.buscaminas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int TAMANO_BOTON_DP = 44;

    private GridLayout gridTablero;
    private TextView tvEstado;
    private Chronometer cronometro;

    private Tablero tablero;
    private Button[][] botones;
    private boolean juegoTerminado;
    private long tiempoInicio;
    private long tiempoElapsado;
    
    private int FILAS = 8;
    private int COLUMNAS = 8;
    private int TOTAL_MINAS = 10;

    private DataManager dataManager;
    private AchievementManager achievementManager;
    private GameStatistics gameStatistics;
    private AppPreferences appPreferences;
    private SoundManager soundManager;
    private Leaderboard leaderboard;
    private GameDifficulty currentDifficulty;
    private ScoreSystem scoreSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridTablero = findViewById(R.id.gridTablero);
        tvEstado = findViewById(R.id.tvEstado);
        cronometro = findViewById(R.id.cronometro);

        Button btnReiniciar = findViewById(R.id.btnReiniciar);
        Button btnLogros = findViewById(R.id.btnLogros);
        Button btnEstadisticas = findViewById(R.id.btnEstadisticas);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        Button btnConfiguracion = findViewById(R.id.btnConfiguracion);

        try {
            // Inicializar managers
            dataManager = new DataManager(this);
            appPreferences = new AppPreferences(this);
            soundManager = new SoundManager(this);
            achievementManager = new AchievementManager(dataManager);
            gameStatistics = dataManager.loadStatistics();
            leaderboard = dataManager.loadLeaderboard();
            currentDifficulty = appPreferences.getDifficulty();

            btnReiniciar.setOnClickListener(v -> iniciarJuego());
            btnLogros.setOnClickListener(v -> abrirLogros());
            btnEstadisticas.setOnClickListener(v -> abrirEstadisticas());
            btnLeaderboard.setOnClickListener(v -> abrirLeaderboard());
            btnConfiguracion.setOnClickListener(v -> abrirConfiguracion());

            // Verificar si hay partida guardada
            if (dataManager.hasSavedGame()) {
                mostrarDialogoCargarPartida();
            } else {
                iniciarJuego();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay error, simplemente iniciar nuevo juego
            iniciarJuego();
        }
    }

    private void mostrarDialogoCargarPartida() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Partida Guardada");
        builder.setMessage("¿Deseas continuar tu partida anterior?");
        builder.setPositiveButton("Continuar", (dialog, which) -> cargarPartida());
        builder.setNegativeButton("Nueva Partida", (dialog, which) -> iniciarJuego());
        builder.show();
    }

    private void cargarPartida() {
        try {
            GameState savedState = dataManager.loadGameState();
            if (savedState != null) {
                // Restaurar tablero desde estado guardado
                tablero = new Tablero(savedState.getFilas(), savedState.getColumnas(), savedState.getTotalMinas());
                restaurarEstadoTablero(savedState);
                
                tiempoElapsado = savedState.getTiempoElapsado();
                juegoTerminado = savedState.isJuegoTerminado();
                
                // Crear UI
                botones = new Button[FILAS][COLUMNAS];
                crearBotones(dpAPx(TAMANO_BOTON_DP), 0);
                
                // Actualizar vista
                actualizarTableroVisual();
                tvEstado.setText("En juego");
                cronometro.setBase(SystemClock.elapsedRealtime() - tiempoElapsado);
                cronometro.start();
            } else {
                iniciarJuego();
            }
        } catch (Exception e) {
            e.printStackTrace();
            iniciarJuego();
        }
    }

    private void restaurarEstadoTablero(GameState savedState) {
        boolean[][] minas = savedState.getMinas();
        boolean[][] descubiertas = savedState.getDescubiertas();
        int[][] minasAdyacentes = savedState.getMinasAdyacentes();
        
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casilla = tablero.getCasilla(f, c);
                casilla.setTieneMina(minas[f][c]);
                casilla.setDescubierta(descubiertas[f][c]);
                casilla.setMinasAdyacentes(minasAdyacentes[f][c]);
            }
        }
    }

    private void iniciarJuego() {
        // Obtener dificultad desde preferencias
        currentDifficulty = appPreferences.getDifficulty();
        FILAS = currentDifficulty.filas;
        COLUMNAS = currentDifficulty.columnas;
        TOTAL_MINAS = currentDifficulty.minas;
        
        tablero = new Tablero(FILAS, COLUMNAS, TOTAL_MINAS);
        botones = new Button[FILAS][COLUMNAS];
        scoreSystem = new ScoreSystem();
        juegoTerminado = false;
        tiempoElapsado = 0;
        tiempoInicio = System.currentTimeMillis();

        tvEstado.setText("En juego");
        tvEstado.setTextColor(getColor(R.color.estado_jugando));

        // reiniciar cronometro
        cronometro.stop();
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();

        gridTablero.removeAllViews();
        gridTablero.setColumnCount(COLUMNAS);
        gridTablero.setRowCount(FILAS);

        int tamPx = dpAPx(TAMANO_BOTON_DP);
        crearBotones(tamPx, 0);
        
        // Limpiar partida guardada anterior
        dataManager.clearSavedGame();
    }

    private void crearBotones(int tamPx, int indice) {
        if (indice >= FILAS * COLUMNAS) {
            return;
        }

        int f = indice / COLUMNAS;
        int c = indice % COLUMNAS;

        Button btn = new Button(this);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(f), GridLayout.spec(c));
        params.width = tamPx;
        params.height = tamPx;
        params.setMargins(2, 2, 2, 2);
        btn.setLayoutParams(params);

        btn.setText("");
        btn.setTextSize(14f);
        btn.setTypeface(null, android.graphics.Typeface.BOLD);
        btn.setPadding(0, 0, 0, 0);
        btn.setBackgroundColor(getColor(R.color.casilla_cubierta));
        btn.setTextColor(Color.WHITE);
        btn.setElevation(2f);

        final int fila = f;
        final int col = c;
        btn.setOnClickListener(v -> alPresionarCasilla(fila, col));
        btn.setOnLongClickListener(v -> {
            alPresionarLargo(fila, col);
            return true;
        });

        botones[f][c] = btn;
        gridTablero.addView(btn);

        gridTablero.postDelayed(() -> crearBotones(tamPx, indice + 1), 5);
    }

    private void alPresionarCasilla(int fila, int col) {
        if (juegoTerminado) return;

        Casilla casilla = tablero.getCasilla(fila, col);

        if (casilla.isDescubierta()) return;

        Button btn = botones[fila][col];
        AnimationUtils.clickAnimation(btn);

        if (casilla.isTieneMina()) {
            casilla.setDescubierta(true);
            actualizarBoton(fila, col);

            juegoTerminado = true;
            cronometro.stop();
            tiempoElapsado = System.currentTimeMillis() - tiempoInicio;
            
            tvEstado.setText("💥 Perdiste");
            tvEstado.setTextColor(getColor(R.color.estado_perdio));

            revelarTodasLasMinas();
            soundManager.playLose();
            AnimationUtils.shakeAnimation(gridTablero);
            
            // Registrar derrota
            gameStatistics.registrarDerrota(tiempoElapsado);
            dataManager.saveStatistics(gameStatistics);
            achievementManager.checkAchievementsForGameEnd(false, tiempoElapsado, 
                gameStatistics.getTotalPartidas(), gameStatistics.getPartidosGanadas(), gameStatistics);
            
            // Limpiar partida guardada
            dataManager.clearSavedGame();

        } else {
            // Sonido de click
            soundManager.playClick();
            
            // Agregar puntos
            int points = scoreSystem.addSafeClick(casilla.getMinasAdyacentes());
            mostrarMensajePuntos(points);
            
            // aplicar flood fill desde esta casilla
            tablero.revelarCasilla(fila, col);

            // actualizar visualmente TODAS las casillas que quedaron reveladas
            actualizarTableroVisual();

            if (verificarVictoria()) {
                juegoTerminado = true;
                cronometro.stop();
                tiempoElapsado = System.currentTimeMillis() - tiempoInicio;
                
                tvEstado.setText("🎉 ¡Ganaste!");
                tvEstado.setTextColor(getColor(R.color.estado_gano));
                soundManager.playWin();
                AnimationUtils.celebrateAnimation(tvEstado);
                AnimationUtils.pulseAnimation(gridTablero);
                
                // Registrar victoria
                gameStatistics.registrarVictoria(tiempoElapsado);
                GameStatistics.GameScore score = new GameStatistics.GameScore(
                    tiempoElapsado, true, 0, FILAS * COLUMNAS - TOTAL_MINAS);
                gameStatistics.agregarAlHistorial(score);
                dataManager.saveStatistics(gameStatistics);
                
                // Agregar al leaderboard
                String playerName = appPreferences.getPlayerName();
                int finalScore = scoreSystem.getTotalScore() + scoreSystem.victoryBonus(tiempoElapsado);
                leaderboard.addEntry(playerName, tiempoElapsado, currentDifficulty, finalScore);
                dataManager.saveLeaderboard(leaderboard);
                
                achievementManager.checkAchievementsForGameEnd(true, tiempoElapsado, 
                    gameStatistics.getTotalPartidas(), gameStatistics.getPartidosGanadas(), gameStatistics);
                
                // Limpiar partida guardada
                dataManager.clearSavedGame();
            } else {
                // Guardar estado actual
                guardarPartida();
            }
        }
    }

    /**
     * Maneja el click largo (long press) para marcar/desmarcar banderas
     */
    private void alPresionarLargo(int fila, int col) {
        if (juegoTerminado) return;

        Casilla casilla = tablero.getCasilla(fila, col);

        // Solo puedes marcar casillas no descubiertas
        if (casilla.isDescubierta()) return;

        casilla.toggleMarcada();
        soundManager.playFlag();
        
        Button btn = botones[fila][col];
        if (casilla.isMarcada()) {
            btn.setText("🚩");
            btn.setTextSize(18f);
        } else {
            btn.setText("");
            btn.setTextSize(14f);
        }
    }

    private void guardarPartida() {
        GameState gameState = new GameState(FILAS, COLUMNAS, TOTAL_MINAS);
        
        // Copiar estado actual del tablero
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casilla = tablero.getCasilla(f, c);
                gameState.getMinas()[f][c] = casilla.isTieneMina();
                gameState.getDescubiertas()[f][c] = casilla.isDescubierta();
                gameState.getMinasAdyacentes()[f][c] = casilla.getMinasAdyacentes();
            }
        }
        
        gameState.setTiempoElapsado(System.currentTimeMillis() - tiempoInicio);
        gameState.setJuegoTerminado(juegoTerminado);
        
        dataManager.saveGameState(gameState);
    }

    /**
     * Muestra un mensaje flotante de puntos ganados
     */
    private void mostrarMensajePuntos(int puntos) {
        // Este método se puede mejorar con un Toast o un TextVi flotante
        // Por ahora es un placeholder
        String comboMsg = scoreSystem.getComboMessage();
        if (!comboMsg.isEmpty()) {
            // Mostrar en log para debugging
            android.util.Log.d("Buscaminas", comboMsg + " - +" + puntos + " puntos");
        }
    }

    private void abrirLogros() {
        Intent intent = new Intent(this, AchievementsActivity.class);
        startActivity(intent);
    }

    private void abrirEstadisticas() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void abrirLeaderboard() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    private void abrirConfiguracion() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // actualiza el boton visual de una casilla especifica
    private void actualizarBoton(int fila, int col) {
        Casilla casilla = tablero.getCasilla(fila, col);
        Button btn = botones[fila][col];

        if (!casilla.isDescubierta()) return;

        btn.setEnabled(false);

        if (casilla.isTieneMina()) {
            btn.setText("💣");
            btn.setTextSize(18f);
            btn.setBackgroundColor(getColor(R.color.casilla_mina));
        } else {
            int adyacentes = casilla.getMinasAdyacentes();
            if (adyacentes == 0) {
                btn.setText("");
                btn.setBackgroundColor(getColor(R.color.casilla_vacia));
            } else {
                btn.setText(String.valueOf(adyacentes));
                btn.setTextSize(16f);
                btn.setBackgroundColor(getColor(R.color.casilla_numero));
                btn.setTextColor(colorPorNumero(adyacentes));
            }
        }
    }

    // recorre todo el tablero y actualiza los botones de las casillas descubiertas
    private void actualizarTableroVisual() {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                actualizarBoton(f, c);
            }
        }
    }

    private void revelarTodasLasMinas() {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casilla = tablero.getCasilla(f, c);
                if (casilla.isTieneMina() && !casilla.isDescubierta()) {
                    botones[f][c].setText("💣");
                    botones[f][c].setTextSize(18f);
                    botones[f][c].setBackgroundColor(getColor(R.color.casilla_mina_revelada));
                    botones[f][c].setEnabled(false);
                }
            }
        }
    }

    private boolean verificarVictoria() {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casilla = tablero.getCasilla(f, c);
                if (!casilla.isTieneMina() && !casilla.isDescubierta()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int colorPorNumero(int numero) {
        switch (numero) {
            case 1: return getColor(R.color.numero_1);
            case 2: return getColor(R.color.numero_2);
            case 3: return getColor(R.color.numero_3);
            case 4: return getColor(R.color.numero_4);
            case 5: return getColor(R.color.numero_5);
            case 6: return getColor(R.color.numero_6);
            case 7: return getColor(R.color.numero_7);
            case 8: return getColor(R.color.numero_8);
            default: return Color.BLACK;
        }
    }

    private int dpAPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
        }
    }
}
