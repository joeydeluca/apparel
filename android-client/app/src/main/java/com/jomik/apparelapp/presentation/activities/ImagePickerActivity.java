package com.jomik.apparelapp.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public abstract class ImagePickerActivity extends AppCompatActivity {

    private final String TAG = ImagePickerActivity.class.getSimpleName();

    Uri mCropImageUri = null;
    Uri chosenImageUri = null;
    String chosenImageUuid = null;
    SimpleDraweeView simpleDraweeView = null;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            chosenImageUri = UCrop.getOutput(data);
            simpleDraweeView.setImageURI(chosenImageUri);
            chosenImageUuid = new File(chosenImageUri.toString()).getName();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("ImagePickerActivity", cropError.getMessage());
            Toast.makeText(getApplicationContext(), "Error cropping image", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Uri imageUri = ImagePickerHelper.getPickImageResultUri(data, getApplicationContext());

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ImagePickerHelper.isUriRequiresPermissions(imageUri, getApplicationContext())) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                startUCrop(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startUCrop(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startUCrop(Uri selectedImageUri) {
        UCrop uCrop = UCrop.of(selectedImageUri, Uri.fromFile(
                getImageFile(UUID.randomUUID().toString())
        )).withMaxResultSize(1000, 1000);

        // TODO: make this abstract
        if (this instanceof EditItemActivity) {
            uCrop.withAspectRatio(9, 16);
        }
        else if (this instanceof EditEventActivity) {
            uCrop.withAspectRatio(4, 3);
        }

        uCrop.start(this);
    }

    private File getExternalPhotoStorageDir() {
        // Get the directory for the app's private pictures directory.
        File file = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "ApparelApp");
        if (!file.exists() && !file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private File getImageFile(String photoUuid) {
        String filename = photoUuid + ".png";
        File file;

        if(isExternalStorageWritable() && isExternalStorageReadable()) {
            Log.i(TAG, "Using external storage");
            file = new File(getExternalPhotoStorageDir(), filename);
        } else {
            Log.i(TAG, "Using internal storage");
            file = new File(getFilesDir(), filename);
        }

        return file;
    }

}
