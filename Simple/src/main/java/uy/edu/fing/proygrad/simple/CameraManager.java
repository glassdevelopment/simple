package uy.edu.fing.proygrad.simple;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * Created by gmelo on 5/5/14.
 */
public class CameraManager {

    private static final String NO_CAMERA_FEATURE = "No camera on this device";
    private static final String NO_FRONT_FACING_CAMERA_FOUND = "No front facing camera found";

    private Context context;

    private Camera camera;
    private int cameraId = 0;

    public CameraManager(Context context) {
        this.context = context;
    }

    public void openCamera() {
        // Check if we have camera feature
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(context, NO_CAMERA_FEATURE, Toast.LENGTH_LONG).show();
        } else {
            cameraId = findRearFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(context, NO_FRONT_FACING_CAMERA_FOUND, Toast.LENGTH_LONG).show();
            } else {
                try {
                    camera = Camera.open(cameraId);
                    // Work around for Camera preview issues.
                    Camera.Parameters params = camera.getParameters();
                    params.setPreviewFpsRange(30000, 30000);
                    camera.setParameters(params);
                }
                catch (Exception e) {
                    Log.e(MenuActivity.TAG, e.getMessage());
                }
            }
        }
    }

    public void takePicture(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            camera.takePicture(null, null, new PhotoHandler(context));
            Toast.makeText(context, "Pass", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Log.e(MenuActivity.TAG, e.getMessage());
        }

    }

    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private int findFrontFacingCamera() {
        return findCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    private int findRearFacingCamera() {
        return findCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    private int findCamera(int cameraType) {
        int cameraId = -1;

        // Search for the rear camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i=0; i<numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraType) {
                cameraId = i;
                break;
            }
        }

        return cameraId;
    }
}
