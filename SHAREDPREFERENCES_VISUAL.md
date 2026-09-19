# SharedPreferences - Guía Visual para Entender

## 📱 PANTALLA 1: Settings (Configuración)

```
╔════════════════════════════════════╗
║     ⚙️ CONFIGURACIÓN               ║
╠════════════════════════════════════╣
║                                    ║
║ 👤 Nombre del Jugador              ║
║ ┌────────────────────────────────┐ ║
║ │ Frank                          │ ║ ← Se guarda en SharedPreferences
║ └────────────────────────────────┘ ║
║                                    ║
║ 🎮 Nivel de Dificultad             ║
║ ┌────────────────────────────────┐ ║
║ │ ▼ Fácil (8x8, 10 minas)       │ ║ ← Se guarda en SharedPreferences
║ └────────────────────────────────┘ ║
║                                    ║
║ 🌙 Tema: Oscuro          [   ●   ] ║ ← Se guarda en SharedPreferences
║   Cambia entre claro y oscuro      ║
║                                    ║
║ 🔊 Sonidos: Activado     [   ●   ] ║ ← Se guarda en SharedPreferences
║   Efectos de sonido del juego      ║
║                                    ║
╚════════════════════════════════════╝
```

---

## 💾 DENTRO DEL TELÉFONO: SharedPreferences

Cuando presionas **"Guardar"** o cambias los valores, Android almacena:

```xml
📁 SharedPreferences (archivo invisible en el teléfono)
   
   player_name = "Frank"
   theme = "DARK"
   sound_enabled = true
   difficulty = "MEDIUM"
```

**Esto se ve así en código:**

```java
SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

// GUARDAR
prefs.edit()
    .putString("player_name", "Frank")
    .putString("theme", "DARK")
    .putBoolean("sound_enabled", true)
    .putString("difficulty", "MEDIUM")
    .apply();

// RECUPERAR (la próxima vez que abras la app)
String nombre = prefs.getString("player_name", "Jugador");  // "Frank"
String tema = prefs.getString("theme", "LIGHT");             // "DARK"
boolean sonido = prefs.getBoolean("sound_enabled", true);    // true
String dificultad = prefs.getString("difficulty", "EASY");   // "MEDIUM"
```

---

## 🔄 FLUJO COMPLETO

### PRIMER USO (Sin datos guardados)
```
Usuario abre app por primera vez
    ↓
Settings muestra valores POR DEFECTO:
  - Nombre: "Jugador"
  - Dificultad: "EASY" (8x8)
  - Tema: "LIGHT" (Claro)
  - Sonidos: Activado
    ↓
Usuario modifica nombre → "Frank"
Usuario cambia dificultad → "MEDIUM"
Usuario cambia tema → "DARK"
Usuario desactiva sonidos
    ↓
SE GUARDA EN SharedPreferences:
  player_name = "Frank"
  difficulty = "MEDIUM"
  theme = "DARK"
  sound_enabled = false
```

### SEGUNDO USO (Datos ya guardados)
```
Usuario abre app de nuevo
    ↓
MainActivity lee SharedPreferences:
  - Dificultad: "MEDIUM" ← Crea tablero 10x10
    ↓
SettingsActivity lee SharedPreferences:
  - Nombre: "Frank" ← Muestra en EditText
  - Tema: "DARK" ← Aplica tema oscuro
  - Sonidos: false ← Switch desactivado
    ↓
TODO SE VE IGUAL QUE COMO LO DEJÓ
```

---

## 📊 TABLA DE DATOS

| Cuando se guarda | Qué se guarda | Dónde se recupera |
|------------------|---------------|------------------|
| Usuario escribe nombre en Settings | `player_name` | Leaderboard, MainActivity |
| Usuario elige dificultad | `difficulty` | MainActivity al iniciar |
| Usuario cambia tema | `theme` | Todos los Activities |
| Usuario toca switch de sonidos | `sound_enabled` | SoundManager para reproducir sonidos |

---

## 🎯 EJEMPLO PRÁCTICO: Dificultad

### SIN SharedPreferences (malo ❌)
```
1. Usuario elige "Difícil" en Settings
2. Se crea tablero 12x12, 60 minas
3. Usuario cierra app
4. Usuario abre app de nuevo
5. ¡¡Vuelve a 8x8 por defecto!! 😞
```

### CON SharedPreferences (bien ✅)
```
1. Usuario elige "Difícil" en Settings
2. Se GUARDA en SharedPreferences: difficulty = "HARD"
3. Se crea tablero 12x12, 60 minas
4. Usuario cierra app
5. Usuario abre app de nuevo
6. MainActivity LEE SharedPreferences: difficulty = "HARD"
7. ¡¡Se crea tablero 12x12 automáticamente!! 😊
```

---

## 🔐 SEGURIDAD

Los datos de SharedPreferences:
- ✅ Se guardan en el teléfono (no en internet)
- ✅ Son privados de la app (otras apps no pueden verlos)
- ✅ No se pierden si cierras la app
- ✅ Se borran si desinstales la app

---

## 📝 CÓDIGO REAL EN TU APP

### AppPreferences.java
```java
public class AppPreferences {
    private SharedPreferences sp;
    
    public AppPreferences(Context context) {
        sp = context.getSharedPreferences("buscaminas_prefs", Context.MODE_PRIVATE);
    }
    
    // GUARDAR nombre
    public void setPlayerName(String name) {
        sp.edit().putString("player_name", name).apply();
    }
    
    // OBTENER nombre
    public String getPlayerName() {
        return sp.getString("player_name", "Jugador");
    }
    
    // GUARDAR tema
    public void setTheme(Theme theme) {
        sp.edit().putString("theme", theme.name()).apply();
    }
    
    // OBTENER tema
    public Theme getTheme() {
        String t = sp.getString("theme", "LIGHT");
        return Theme.valueOf(t);
    }
}
```

### MainActivity.java (usando AppPreferences)
```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    AppPreferences prefs = new AppPreferences(this);
    
    // Leer dificultad guardada
    GameDifficulty dif = prefs.getDifficulty();  // "MEDIUM"
    
    // Usar para crear tablero
    FILAS = dif.filas;        // 10
    COLUMNAS = dif.columnas;  // 10
    TOTAL_MINAS = dif.minas;  // 30
    
    tablero = new Tablero(FILAS, COLUMNAS, TOTAL_MINAS);
}
```

---

## 🎓 RESUMEN PARA LA SEÑORA

**SharedPreferences = El "cuaderno de notas" del teléfono**

Cuando el usuario:
- ✍️ Escribe su nombre → Se guarda en el cuaderno
- 🎮 Elige dificultad → Se guarda en el cuaderno
- 🌙 Cambia tema → Se guarda en el cuaderno
- 🔊 Desactiva sonidos → Se guarda en el cuaderno

Cuando abre la app de nuevo:
- 📖 Lee el cuaderno
- 🎯 Recupera todos los datos
- ✨ Todo funciona como lo dejó

**¡¡SIN INTERNET, SIN INTERNET REQUERIDO!!** 📱

---

Esto es lo que deberías explicar con capturas 📸
