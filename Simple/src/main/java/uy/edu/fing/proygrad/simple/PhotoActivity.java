package uy.edu.fing.proygrad.simple;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.IOException;

/**
 * Created by gmelo on 5/6/14.
 */
public class PhotoActivity extends Activity implements GestureDetector.BaseListener {

    private AudioManager mAudioManager;
    private GestureDetector mDetector;
    private CameraManager cameraManager;

    private SurfaceView mPreview;

    private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                cameraManager.takePicture(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Nothing to do here.
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Nothing to do here.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mDetector = new GestureDetector(this).setBaseListener(this);
        cameraManager = new CameraManager(this.getApplicationContext());

        setContentView(R.layout.photo_layout);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(mSurfaceHolderCallback);
    }

    @Override
    protected void onResume() {
        cameraManager.openCamera();
        super.onResume();
    }

    @Override
    protected void onPause() {
        cameraManager.releaseCamera();
        super.onPause();
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:
                playSoundEffect(Sounds.TAP);
                return true;
            case SWIPE_DOWN:
                playSoundEffect(Sounds.DISMISSED);
                finish();
                return true;
            default:
                return false;
        }
    }

    protected void playSoundEffect(int soundId) {
        mAudioManager.playSoundEffect(soundId);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mDetector.onMotionEvent(event);
    }
}
