package com.miuniversidad.buscaminas;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;
    private static final int TOTAL_MINAS = 10;
    private static final int TAMANO_BOTON_DP = 44;

    private GridLayout gridTablero;
    private TextView tvEstado;
    private Chronometer cronometro;

    private Tablero tablero;
    private Button[][] botones;
    private boolean juegoTerminado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridTablero = findViewById(R.id.gridTablero);
        tvEstado = findViewById(R.id.tvEstado);
        cronometro = findViewById(R.id.cronometro);

        Button btnReiniciar = findViewById(R.id.btnReiniciar);
        btnReiniciar.setOnClickListener(v -> iniciarJuego());

        iniciarJuego();
    }

    private void iniciarJuego() {
        tablero = new Tablero(FILAS, COLUMNAS, TOTAL_MINAS);
        botones = new Button[FILAS][COLUMNAS];
        juegoTerminado = false;

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

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
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
                btn.setElevation(4f);

                final int fila = f;
                final int col = c;
                btn.setOnClickListener(v -> alPresionarCasilla(fila, col));

                botones[f][c] = btn;
                gridTablero.addView(btn);
            }
        }
    }

    private void alPresionarCasilla(int fila, int col) {
        if (juegoTerminado) return;

        Casilla casilla = tablero.getCasilla(fila, col);

        if (casilla.isDescubierta()) return;

        if (casilla.isTieneMina()) {
            casilla.setDescubierta(true);
            actualizarBoton(fila, col);

            juegoTerminado = true;
            cronometro.stop();
            tvEstado.setText("💥 Perdiste");
            tvEstado.setTextColor(getColor(R.color.estado_perdio));

            revelarTodasLasMinas();

        } else {
            // aplicar flood fill desde esta casilla
            tablero.revelarCasilla(fila, col);

            // actualizar visualmente TODAS las casillas que quedaron reveladas
            actualizarTableroVisual();

            if (verificarVictoria()) {
                juegoTerminado = true;
                cronometro.stop();
                tvEstado.setText("🎉 ¡Ganaste!");
                tvEstado.setTextColor(getColor(R.color.estado_gano));
            }
        }
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
}
