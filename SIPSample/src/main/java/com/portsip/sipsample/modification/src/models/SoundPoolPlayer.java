package com.portsip.sipsample.modification.src.models;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.media.SoundPool;


import java.util.HashMap;

public class SoundPoolPlayer {
    private SoundPool mShortPlayer = null;
    private final HashMap<Integer, Integer> mSounds = new HashMap<>();

    public SoundPoolPlayer(Context pContext) {
        // setup Sound pool

    }

    int id;

    public void playButtonSound() {
        try {
            mShortPlayer.stop(id);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void play( int res) {
        try {
            mShortPlayer.stop(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int iSoundId = mSounds.get(res);
        id = this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);

    }

    // Cleanup
    public void release() {
        // Cleanup
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }
}