package com.alevat.spaceinvaders.io;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundResource {

    WAVE_NOTE_1("note1.wav"),
    WAVE_NOTE_2("note2.wav"),
    WAVE_NOTE_3("note3.wav"),
    WAVE_NOTE_4("note4.wav"),

    FIRE_SHOT("shot.wav"),

    EXPLOSION("explosion.wav"),

    ALIEN_KILLED("invader_killed.wav");

    public static SoundResource[] WAVE_NOTES = new SoundResource[] {
            WAVE_NOTE_1,
            WAVE_NOTE_2,
            WAVE_NOTE_3,
            WAVE_NOTE_4
    };

    private final Clip clip;

    SoundResource(String filename) {
        clip = loadClip(filename);
    }

    private Clip loadClip(String filename) {
        URL url = getClass().getResource("/sounds/" + filename);
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException("Couldn't load sound at URL: " + url);
        }
        return clip;
    }

    public Clip getClip() {
        return clip;
    }
}
