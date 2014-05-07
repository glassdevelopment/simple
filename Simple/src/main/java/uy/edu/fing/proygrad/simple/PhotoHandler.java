package uy.edu.fing.proygrad.simple;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gonzalo on 04/05/2014.
 */
public class PhotoHandler implements Camera.PictureCallback {

    private static final String CANT_CREATE_DIRECTORY = "Can't create directory to save image";
    private static final String FILE_NAME = "CameraAPIDemo";
    private static final String FILE_NOT_SAVED = "File not saved";
    private static final String IMAGE_SAVED = "New image saved";

    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    /**
     * Stores the picture taken in SD pictures directory with name FILE_NAME
     * @param data photo bytes
     * @param camera camera object
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.d(MenuActivity.TAG, CANT_CREATE_DIRECTORY);
            Toast.makeText(context, CANT_CREATE_DIRECTORY, Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(context, IMAGE_SAVED + ":" + photoFile, Toast.LENGTH_LONG).show();
        }
        catch (Exception error) {
            Log.d(MenuActivity.TAG, FILE_NOT_SAVED + error.getMessage());
            Toast.makeText(context, FILE_NOT_SAVED + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gets the file FILE_NAME object in SD pictures directory
     * @return File object
     */
    private File getDir() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }
}
