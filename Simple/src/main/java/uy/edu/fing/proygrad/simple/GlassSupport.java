package uy.edu.fing.proygrad.simple;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by gmelo on 5/20/14.
 */
public class GlassSupport {

    public static void playSoundEffect(Context context, int sound) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.playSoundEffect(sound);
    }

}
