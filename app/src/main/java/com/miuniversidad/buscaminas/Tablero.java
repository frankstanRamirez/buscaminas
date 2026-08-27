package com.miuniversidad.buscaminas;

import java.util.Random;
import java.util.Stack;

public class Tablero {

    private final int filas;
    private final int columnas;
    private final int totalMinas;
    private final Casilla[][] celdas;
    private boolean primerClickRealizado;

    public Tablero(int filas, int columnas, int totalMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.totalMinas = totalMinas;
        this.primerClickRealizado = false;

        celdas = new Casilla[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                celdas[f][c] = new Casilla();
            }
        }
        // RECORDAAA NO colocamos minas aun MARAVILLA solo vamo a esperamos el primer click
    }

    // coloca las minas en posiciones aleatorias, evitando la zona del primer click
    private void colocarMinas(int filaClick, int colClick) {
        Random random = new Random();
        int colocadas = 0;

        while (colocadas < totalMinas) {
            int f = random.nextInt(filas);
            int c = random.nextInt(columnas);

            // evitamos colocar mina en la casilla clickeada y sus 8 vecinos:)
            boolean esZonaSegura = Math.abs(f - filaClick) <= 1 && Math.abs(c - colClick) <= 1;

            if (!celdas[f][c].isTieneMina() && !esZonaSegura) {
                celdas[f][c].setTieneMina(true);
                colocadas++;
            }
        }
    }

    // calcula cuantas minas rodean cada casilla
    private void calcularAdyacentes() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (!celdas[f][c].isTieneMina()) {
                    celdas[f][c].setMinasAdyacentes(contarVecinos(f, c));
                }
            }
        }
    }

    private int contarVecinos(int fila, int col) {
        int cuenta = 0;

        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) continue;

                int nf = fila + df;
                int nc = col + dc;

                if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas) {
                    if (celdas[nf][nc].isTieneMina()) {
                        cuenta++;
                    }
                }
            }
        }
        return cuenta;
    }

    public Casilla getCasilla(int fila, int columna) {
        return celdas[fila][columna];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getTotalMinas() {
        return totalMinas;
    }

    // revela una casilla y aplica flood fill si tiene 0 minas adyacentes
    public void revelarCasilla(int fila, int columna) {
        // si es el primer click, colocar las minas ahora
        if (!primerClickRealizado) {
            colocarMinas(fila, columna);
            calcularAdyacentes();
            primerClickRealizado = true;
        }

        // verificar limites
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return;
        }

        Casilla casilla = celdas[fila][columna];

        // si ya esta descubierta o tiene mina, no hacer nada
        if (casilla.isDescubierta() || casilla.isTieneMina()) {
            return;
        }

        // usar una pila para evitar recursion profunda
        Stack<int[]> pila = new Stack<>();
        pila.push(new int[]{fila, columna});

        while (!pila.isEmpty()) {
            int[] pos = pila.pop();
            int f = pos[0];
            int c = pos[1];

            // verificar limites nuevamente
            if (f < 0 || f >= filas || c < 0 || c >= columnas) {
                continue;
            }

            Casilla actual = celdas[f][c];

            // si ya fue descubierta o tiene mina, saltar
            if (actual.isDescubierta() || actual.isTieneMina()) {
                continue;
            }

            // revelar esta casilla
            actual.setDescubierta(true);

            // si tiene 0 minas adyacentes, agregar sus 8 vecinos a la pila
            if (actual.getMinasAdyacentes() == 0) {
                for (int df = -1; df <= 1; df++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (df == 0 && dc == 0) continue;
                        pila.push(new int[]{f + df, c + dc});
                    }
                }
            }
            // si tiene numero > 0, se revela pero no expande (ya no agrega vecinos)
        }
    }
}