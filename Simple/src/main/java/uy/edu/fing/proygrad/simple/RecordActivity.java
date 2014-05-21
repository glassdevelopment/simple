package uy.edu.fing.proygrad.simple;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.IOException;
import java.util.List;

/**
 * Created by gmelo on 5/19/14.
 */
public class RecordActivity extends Activity implements GestureDetector.BaseListener {

    private Camera mCamera;
    private GestureDetector mDetector;
    private MediaRecorder mMediaRecorder;
    private TextureView mPreview;

    private boolean isRecording = false;
    private static final String TAG = "Recorder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        mPreview = (TextureView) findViewById(R.id.surface_view);
        mDetector = new GestureDetector(this).setBaseListener(this);

        // Keep the screen on while recording
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:
                if (isRecording) {
                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder(); // release the MediaRecorder object
                    mCamera.lock();         // take camera access back from MediaRecorder

                    // inform the user that recording has stopped
                    Toast.makeText(this, "Capture", Toast.LENGTH_SHORT);

                    isRecording = false;
                    releaseCamera();

                } else {
                    new MediaPrepareTask().execute(null, null, null);
                }
                return true;
            case SWIPE_DOWN:
                GlassSupport.playSoundEffect(this, Sounds.DISMISSED);
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mDetector.onMotionEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean prepareVideoRecorder(){

        mCamera = CameraHelper.getDefaultCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(supportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }

        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO).toString());

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // initialize video camera
            if (!prepareVideoRecorder()) {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }

            // Camera is available and unlocked, MediaRecorder is prepared,
            // now you can start recording
            mMediaRecorder.start();
            isRecording = true;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                RecordActivity.this.finish();
            }
            // inform the user that recording has started
            Toast.makeText(RecordActivity.this, "Stop", Toast.LENGTH_SHORT);
        }
    }
}
