# 🎮 BUSCAMINAS - PROYECTO ANDROID

## Documentación del Proyecto

---

## 📋 Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Características Principales](#características-principales)
3. [Especificaciones Técnicas](#especificaciones-técnicas)
4. [Funcionalidades Detalladas](#funcionalidades-detalladas)
5. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
6. [Guía de Uso](#guía-de-uso)

---

## 📌 Descripción General

**Buscaminas** es un juego clásico implementado en Android nativo (Java) con características modernas y un sistema avanzado de puntuación, logros y estadísticas.

### Información del Proyecto
- **Nombre:** Buscaminas v3.0
- **Plataforma:** Android (API 24+)
- **Lenguaje:** Java
- **Estatus:** ✅ Completado y Funcional

---

## 🎯 Características Principales

### 1. **Gameplay Principal**
- **Tablero dinámico** de 8x8 casillas (expandible a 10x10 y 12x12)
- **Click simple:** Revelar casilla
- **Click largo (long-press):** Marcar/desmarcar bandera 🚩
- **Sistema de flood-fill:** Revela automáticamente zonas sin minas
- **Detección inteligente:** Las minas se colocan después del primer click (garantiza victoria inicial)

### 2. **Sistema de Dificultades**
| Nivel | Tablero | Minas | Descripción |
|-------|---------|-------|------------|
| 🟢 Fácil | 8×8 | 10 | Perfecto para principiantes |
| 🟡 Medio | 10×10 | 30 | Desafío moderado |
| 🔴 Difícil | 12×12 | 60 | Para expertos |

### 3. **Sistema de Puntuación y Combos**
```
Puntos Base = 10 + (Minas Adyacentes × 5)
Multiplicador = 1.0 + (Combo / 10.0)

Niveles de Combo:
- 5+ clicks seguros: "¡BUENA RACHA!" 🔥
- 10+ clicks seguros: "¡INCREÍBLE!" 🔥🔥
- 20+ clicks seguros: "¡ÉPICO!" 🔥🔥🔥
```

### 4. **Logros (7 Total)**
- 🏆 **Primer Triunfo** - Gana tu primera partida
- ⚡ **Maestro de Velocidad** - Gana en menos de 30 segundos
- 👑 **Experto Buscaminas** - Gana 5 partidas
- 💯 **Juego Perfecto** - Gana sin errores
- 🛡️ **Superviviente** - Sobrevive 3 partidas seguidas
- ⏰ **Desafío de Tiempo** - Completa 10 partidas
- 🎯 **Coleccionista** - Desbloquea 5 logros

### 5. **Leaderboard Global**
- **Top 10** mejores tiempos
- Medallas: 🥇 🥈 🥉 para los 3 primeros
- Información: Nombre, Tiempo, Dificultad, Puntuación
- Persistencia local con Gson + SharedPreferences

### 6. **Temas Visuales**
- ☀️ **Tema Claro** (Colores brillantes)
- 🌙 **Tema Oscuro** (Colores suaves)
- Cambio dinámico sin reiniciar la app
- Guardado automático de preferencia

### 7. **Sistema de Sonidos**
- 🔊 Click en casilla
- 🎵 Victoria (ganador)
- ❌ Derrota (perdedor)
- 🚩 Marcar bandera
- Control on/off desde Configuración

### 8. **Animaciones**
- 📈 Pulse (escala suave)
- 🎉 Celebrate (rotación victoria)
- 📉 Shake (vibración derrota)
- ✨ Fade In/Out
- 🎯 Click feedback

---

## 🛠️ Especificaciones Técnicas

### Stack Tecnológico
```
├── Lenguaje: Java
├── Mínimo API: 24 (Android 7.0)
├── Target API: 36 (Android 15)
├── Build Tool: Gradle 8.x
├── IDE: Android Studio
└── Librerías: Gson, AppCompat
```

### Librerías Externas
```gradle
implementation 'androidx.appcompat:appcompat:1.8.0'
implementation 'androidx.constraintlayout:constraintlayout:2.2.2'
implementation 'com.google.android.material:material:1.14.0'
implementation 'com.google.code.gson:gson:2.10.1'
```

### Arquitectura de Datos
```
SharedPreferences (Local Storage)
├── Preferencias (Tema, Sonidos, Nombre)
├── Logros (JSON con Gson)
├── Estadísticas (JSON con Gson)
├── Leaderboard (JSON con Gson)
└── Estado de Partida (JSON con Gson)
```

---

## 🎮 Funcionalidades Detalladas

### A. Flujo de Juego

#### Inicio
1. Usuario abre la app
2. Si hay partida guardada → Pregunta si continuar o nueva
3. Se genera un tablero 8×8 con 10 espacios de seguridad
4. Se inicia el cronómetro

#### Gameplay
1. Usuario hace click → Casilla se revela
2. Si es mina → Juego termina (derrota)
3. Si no es mina → Se suma puntos y se expande flood-fill si procede
4. Usuario puede marcar banderas con click largo
5. Cuando todas las casillas sin minas están descubiertas → Victoria

#### Fin de Partida
- **Victoria:** Bonus de tiempo + combo × 50
- **Derrota:** Muestra todas las minas, penaliza puntos
- Se guarda en estadísticas y leaderboard
- Se verifica si hay nuevos logros

### B. Sistema de Logros

**Cálculo Automático:**
- Se verifica después de cada partida
- Utiliza `AchievementManager`
- Persiste en SharedPreferences
- Muestra progreso en Activity dedicada

### C. Estadísticas

**Métricas Almacenadas:**
- Total partidas jugadas
- Victorias / Derrotas
- Tasa de Victoria (%)
- Mejor tiempo
- Tiempo total jugado
- Historial de últimas 100 partidas

### D. Preferencias de Usuario

**Configurables desde ⚙️ Settings:**
- Nombre del jugador
- Tema (Claro/Oscuro)
- Volumen de sonidos
- Dificultad por defecto

---

## 🏗️ Arquitectura del Proyecto

### Estructura de Carpetas
```
Buscaminas/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/miuniversidad/buscaminas/
│   │       │   ├── MainActivity.java           [Principal]
│   │       │   ├── Tablero.java               [Lógica juego]
│   │       │   ├── Casilla.java               [Modelo]
│   │       │   ├── GameState.java             [Persistencia]
│   │       │   ├── GameStatistics.java        [Estadísticas]
│   │       │   ├── Achievement.java           [Logros]
│   │       │   ├── AchievementManager.java    [Gestor logros]
│   │       │   ├── Leaderboard.java           [Rankings]
│   │       │   ├── ScoreSystem.java           [Puntuación]
│   │       │   ├── SoundManager.java          [Audio]
│   │       │   ├── AnimationUtils.java        [Animaciones]
│   │       │   ├── AppPreferences.java        [Config]
│   │       │   ├── ThemeManager.java          [Temas]
│   │       │   ├── DataManager.java           [Datos]
│   │       │   ├── GameDifficulty.java        [Dificultades]
│   │       │   ├── AchievementsActivity.java  [UI Logros]
│   │       │   ├── StatsActivity.java         [UI Stats]
│   │       │   ├── LeaderboardActivity.java   [UI Leaderboard]
│   │       │   └── SettingsActivity.java      [UI Config]
│   │       └── res/
│   │           ├── layout/
│   │           │   ├── activity_main.xml
│   │           │   ├── activity_achievements.xml
│   │           │   ├── activity_stats.xml
│   │           │   ├── activity_leaderboard.xml
│   │           │   └── activity_settings.xml
│   │           └── values/
│   │               ├── colors.xml
│   │               ├── strings.xml
│   │               ├── themes.xml
│   │               └── values-night/
│   │                   └── colors.xml
│   └── build.gradle.kts
└── gradle/libs.versions.toml
```

### Relaciones de Clases
```
MainActivity (Controller)
    ├── Tablero (Model - Game Logic)
    ├── GameStatistics (Model - Stats)
    ├── AchievementManager (Service)
    ├── Leaderboard (Model)
    ├── ScoreSystem (Service)
    ├── SoundManager (Service)
    ├── AppPreferences (Service)
    ├── DataManager (Service)
    └── AnimationUtils (Utility)
```

---

## 👨‍💻 Guía de Uso

### Para Usuarios

#### 🎮 Jugando
1. **Abre la app** → Se carga MainActivity
2. **Click en casilla** → Revela casilla
3. **Click largo en casilla** → Marca bandera 🚩
4. **Busca patrones** → Las casillas con números indican minas cercanas
5. **Completa el tablero** → Revela todas las casillas sin minas

#### 📊 Menú Principal
- **Nueva Partida** → Inicia juego nuevo
- **🏆 Logros** → Ve logros desbloqueados y progreso
- **📊 Stats** → Histórico y estadísticas generales
- **🏅 Top 10** → Leaderboard de mejores tiempos
- **⚙️ Config** → Preferencias y configuración

#### ⚙️ Configuración
```
Nombre del Jugador → Editable
Tema → Switch (☀️ Claro / 🌙 Oscuro)
Sonidos → Switch (On/Off)
Información → Versión y ayuda
```

### Para Desarrolladores

#### Compilar
```bash
./gradlew assembleDebug
```

#### Instalar en Device
```bash
./gradlew installDebug
```

#### Ejecutar Tests
```bash
./gradlew test
```

#### Git Workflow
```bash
git pull origin main
git checkout -b feature/nueva-feature
git add .
git commit -m "feat: descripción"
git push origin feature/nueva-feature
```

---

## 📊 Estadísticas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Clases Java** | 20+ |
| **Layouts XML** | 10+ |
| **Líneas de Código** | 2500+ |
| **Features Principales** | 15+ |
| **Logros Implementados** | 7 |
| **Dificultades** | 3 |
| **Animaciones** | 6 |

---

## 🚀 Roadmap Futuro

### Version 4.0
- [ ] Dificultad INSANE (16×16, 99 minas)
- [ ] Modo infinite (minas infinitas)
- [ ] Replays de partidas
- [ ] Custom tableros (usuario define tamaño)

### Version 5.0
- [ ] Multiplayer online
- [ ] Cloud sync (Firebase)
- [ ] Notificaciones push
- [ ] Torneos
- [ ] Badges y avatares

---

## 📝 Notas Importantes

✅ **Completado:**
- Sistema completo de juego
- Persistencia de datos
- UI responsiva
- Animaciones suaves
- Tema claro/oscuro
- Sistema de logros
- Leaderboard

⚠️ **Consideraciones:**
- Los sonidos son placeholders (necesitan archivos .wav/.mp3)
- SharedPreferences es local (no sincroniza entre dispositivos)
- Máximo 50 entradas en leaderboard por espacio

---

## 👥 Equipo

- **Desarrollador:** Frank
- **Plataforma:** Android Studio
- **Asistente IA:** Kiro

---

## 📄 Licencia

Proyecto educativo - Universidad

---

**Última Actualización:** Septiembre 2026  
**Versión:** 3.0  
**Estado:** ✅ Producción

---

*Documento generado automáticamente con información del proyecto*
