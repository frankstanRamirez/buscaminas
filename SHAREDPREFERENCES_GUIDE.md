# SharedPreferences en Buscaminas v3.0

## ¿Qué es SharedPreferences?

SharedPreferences es el sistema de almacenamiento local de Android que guarda datos en pares **clave-valor** de forma persistente. Es como una base de datos simple que se almacena en el teléfono.

---

## Datos Guardados en Buscaminas

### 1. **Nombre del Jugador**
- **Clave:** `player_name`
- **Tipo:** String
- **Ejemplo:** `"Frank"` o `"Juan"`
- **Dónde se usa:** Settings Activity, Leaderboard

### 2. **Tema (Claro/Oscuro)**
- **Clave:** `theme`
- **Tipo:** Enum (LIGHT, DARK, AUTO)
- **Ejemplo:** `"DARK"` o `"LIGHT"`
- **Dónde se usa:** Todos los Activities para aplicar el tema

### 3. **Sonidos Habilitados**
- **Clave:** `sound_enabled`
- **Tipo:** Boolean
- **Ejemplo:** `true` o `false`
- **Dónde se usa:** SoundManager para reproducir efectos

### 4. **Dificultad Seleccionada**
- **Clave:** `difficulty`
- **Tipo:** Enum (EASY, MEDIUM, HARD)
- **Ejemplo:** `"MEDIUM"`
- **Valores:**
  - EASY: 8x8 tablero, 10 minas
  - MEDIUM: 10x10 tablero, 30 minas
  - HARD: 12x12 tablero, 60 minas
- **Dónde se usa:** MainActivity para crear el tablero

---

## Código de Almacenamiento (AppPreferences.java)

```java
public class AppPreferences {
    private SharedPreferences sharedPreferences;
    
    // Guardar nombre
    public void setPlayerName(String name) {
        sharedPreferences.edit()
            .putString("player_name", name)
            .apply();
    }
    
    // Obtener nombre
    public String getPlayerName() {
        return sharedPreferences.getString("player_name", "Jugador");
    }
    
    // Guardar tema
    public void setTheme(Theme theme) {
        sharedPreferences.edit()
            .putString("theme", theme.name())
            .apply();
    }
    
    // Obtener tema
    public Theme getTheme() {
        String themeStr = sharedPreferences.getString("theme", "LIGHT");
        return Theme.valueOf(themeStr);
    }
    
    // Guardar dificultad
    public void setDifficulty(GameDifficulty difficulty) {
        sharedPreferences.edit()
            .putString("difficulty", difficulty.name())
            .apply();
    }
    
    // Obtener dificultad
    public GameDifficulty getDifficulty() {
        String diffStr = sharedPreferences.getString("difficulty", "EASY");
        return GameDifficulty.valueOf(diffStr);
    }
}
```

---

## Cómo Se Usa en la App

### En MainActivity:
```java
AppPreferences prefs = new AppPreferences(this);
GameDifficulty difficulty = prefs.getDifficulty(); // Obtiene: EASY, MEDIUM o HARD
// Usa esto para crear el tablero con el tamaño correcto
```

### En SettingsActivity:
```java
// Cargar valores guardados
String nombre = appPreferences.getPlayerName();
boolean sonidoActivo = appPreferences.isSoundEnabled();
GameDifficulty dificultad = appPreferences.getDifficulty();

// Guardar cambios cuando el usuario modifica
appPreferences.setPlayerName(nuevoNombre);
appPreferences.setSoundEnabled(nuevoValor);
appPreferences.setDifficulty(nuevaDificultad);
```

### En AchievementsActivity y otros:
```java
// Obtener tema para aplicar colores
Theme tema = appPreferences.getTheme();
if (tema == Theme.DARK) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
}
```

---

## Flujo de Datos

```
USUARIO CAMBIA SETTING EN SettingsActivity
         ↓
   appPreferences.set*(valor)
         ↓
   SharedPreferences guarda en el teléfono
         ↓
   SIGUIENTE VEZ QUE ABRE LA APP
         ↓
   appPreferences.get*() recupera el valor
         ↓
   Se aplica automáticamente (dificultad, tema, nombre, sonido)
```

---

## Resumen de Variables de SharedPreferences

| Variable | Clave | Tipo | Valor Defecto |
|----------|-------|------|---------------|
| Nombre Jugador | `player_name` | String | "Jugador" |
| Tema | `theme` | String | "LIGHT" |
| Sonidos | `sound_enabled` | Boolean | true |
| Dificultad | `difficulty` | String | "EASY" |

---

## Ubicación en el Teléfono

En un teléfono Android real, estos datos se guardan en:
```
/data/data/com.miuniversidad.buscaminas/shared_prefs/
```

Pero es información **interna**, no visible al usuario normalmente.

---

## Ventajas de SharedPreferences

✅ **Persistencia:** Los datos se guardan aunque cierres la app  
✅ **Velocidad:** Acceso rápido a los datos  
✅ **Simplicidad:** Fácil de usar (clave-valor)  
✅ **Seguridad:** Datos privados de la app (no visible a otras apps)  
✅ **Sin Internet:** Funciona completamente offline  

---

Esto es lo que la señora debe entender: **SharedPreferences = Memoria de la app para recordar las preferencias del usuario** 📱✨
