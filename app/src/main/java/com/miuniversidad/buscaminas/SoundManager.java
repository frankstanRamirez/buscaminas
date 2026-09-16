package com.miuniversidad.buscaminas;

import android.content.Context;
import android.media.SoundPool;
import android.media.AudioAttributes;

/**
 * Manager para manejar efectos de sonido del juego
 */
public class SoundManager {
    private SoundPool soundPool;
    private int soundClick;
    private int soundWin;
    private int soundLose;
    private int soundFlag;
    private boolean soundEnabled;

    public SoundManager(Context context) {
        this.soundEnabled = true;
        
        // Crear SoundPool con AudioAttributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        // Aquí irían los sonidos reales, por ahora usamos IDs de placeholder
        // En un proyecto real, cargarías archivos .wav o .mp3
        // soundClick = soundPool.load(context, R.raw.click, 1);
        // soundWin = soundPool.load(context, R.raw.win, 1);
        // soundLose = soundPool.load(context, R.raw.lose, 1);
        // soundFlag = soundPool.load(context, R.raw.flag, 1);
    }

    public void playClick() {
        if (soundEnabled && soundClick != 0) {
            soundPool.play(soundClick, 0.5f, 0.5f, 1, 0, 1.0f);
        }
    }

    public void playWin() {
        if (soundEnabled && soundWin != 0) {
            soundPool.play(soundWin, 1f, 1f, 1, 0, 1.0f);
        }
    }

    public void playLose() {
        if (soundEnabled && soundLose != 0) {
            soundPool.play(soundLose, 1f, 1f, 1, 0, 1.0f);
        }
    }

    public void playFlag() {
        if (soundEnabled && soundFlag != 0) {
            soundPool.play(soundFlag, 0.7f, 0.7f, 1, 0, 1.0f);
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
