package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.SessionType;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.adapter.GalleryAdapter;

public class ImagePickerDemo  extends AppCompatActivity {

    private static final String TAG = "ImagePickerDemo";
    private static final String IMAGE_DIRECTORY_NAME ="GoogleCameraView" ;

    Uri photoURI;
    String filetype;
    CameraView cameraView;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private int mCurrentFlash;
    private static final Flash[] FLASH_OPTIONS = {
            Flash.AUTO,
            Flash.OFF,
            Flash.ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };


    private Handler mBackgroundHandler;

    ArrayList<Uri> selectedUriList;

    TedBottomPicker bottomSheetDialogFragment;

    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottomSheetView;

    private String pickerType="multi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_demo);

        cameraView = (CameraView) findViewById(R.id.camera);

        bottomSheetView = (FrameLayout) findViewById(R.id.container);

        if (cameraView != null) {
            cameraView.addCameraListener(new CameraListener() {
                @Override
                public void onPictureTaken(byte[] jpeg) {
                    filetype="picture";
                    Toast.makeText(getApplicationContext(), getString(R.string.picture_taken),
                            Toast.LENGTH_SHORT)
                            .show();
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                            Locale.getDefault()).format(
                            new Date());
                    String imageFileName = "JPEG_" + timeStamp + ".jpg";
                    // External sdcard location
                    File mediaStorageDir = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            IMAGE_DIRECTORY_NAME);

                    // Create the storage directory if it does not exist
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                                    + IMAGE_DIRECTORY_NAME + " directory");
                        }
                    }

                    File file = new File(mediaStorageDir.getPath() + File.separator
                            + imageFileName);

                    OutputStream os = null;

                    photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            getApplicationContext().getPackageName() + ".provider", file);

                    MediaScannerConnection.scanFile(ImagePickerDemo.this,
                            new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

//                    bottomSheetDialogFragment.onActivityResultCamera(photoURI);
                    try {
                        os = new FileOutputStream(file);
                        os.write(jpeg);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                    imagePreview(photoURI);
                }

                @Override
                public void onVideoTaken(File video) {
                    filetype="Video";
                    onVideo(video);
                }

            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "photo", Toast.LENGTH_LONG).show();
                    if (cameraView != null) {
                        cameraView.capturePicture();
                    }

                }
            });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    Toast.makeText(getApplicationContext(), "video", Toast.LENGTH_LONG).show();
                    captureVideo();
                    return true;
                }
            });

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if(!checkPermission())
            requestPermission();
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(ImagePickerDemo.this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:

                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean StoragePermission =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && StoragePermission) {
                        cameraView.start();

                        Toast.makeText(getApplicationContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied",
                                Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
        if (bottomSheetDialogFragment == null || !bottomSheetDialogFragment.isVisible()) {

            if (pickerType.equalsIgnoreCase("single"))
                showBottomPicker();
            else if (pickerType.equalsIgnoreCase("multi")) {
                showMultiBottomPicker();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    private void showMultiBottomPicker() {
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(ImagePickerDemo.this)
                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {
                        selectedUriList = uriList;
                        showUriList(uriList);
                    }
                })
                .setPeekHeight(getResources().getDisplayMetrics().heightPixels)
                .showTitle(false)
                .showCameraTile(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .setSelectedUriList(selectedUriList)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());

    }

    private void showBottomPicker() {
        bottomSheetDialogFragment = new TedBottomPicker.Builder(ImagePickerDemo.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        imagePreview(uri);
                    }
                })
                .setPeekHeight(getResources().getDisplayMetrics().heightPixels)
                .showCameraTile(false)
                .showGalleryTile(true)
                .setCompleteButtonText(getString(R.string.done))
                .setEmptySelectionText(getString(R.string.no_select))
                .setSelectedUriList(selectedUriList)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (cameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    cameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (cameraView != null) {
                    cameraView.toggleFacing();
                }
                return true;
            case R.id.switch_gallery:
                showMultiBottomPicker();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void imagePreview(final Uri uri) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_preview);

        ImageView previewImage = (ImageView) dialog.findViewById(R.id.img_preview_image);
        Button saveButton = (Button) dialog.findViewById(R.id.btn_image_preview_save);
        Button cancelButton = (Button) dialog.findViewById(R.id.btn_image_preview_canel);

        Glide.with(this).load(uri).into(previewImage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Uri> vid = new ArrayList<>();
                vid.add(uri);
                PublishActivity.openWithVideoUri(ImagePickerDemo.this, vid);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showUriList(ArrayList<Uri> uriList) {
        PublishActivity.openWithAlbumUri(ImagePickerDemo.this, uriList);
    }


    private void onVideo(File video) {
        videoPreview(Uri.fromFile(video));
    }

    private void captureVideo() {
        cameraView.setSessionType(SessionType.VIDEO);
        if (cameraView.getSessionType() != SessionType.VIDEO) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Recording for 8 seconds...", Toast.LENGTH_LONG).show();

        Toast.makeText(getApplicationContext(),"video captured",
                Toast.LENGTH_SHORT)
                .show();

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(
                new Date());
        String videoFileName = "MP4_" + timeStamp + ".mp4";

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
            }
        }

        File file = new File(mediaStorageDir.getPath() + File.separator
                + videoFileName);


        OutputStream os = null;

        photoURI = FileProvider.getUriForFile(getApplicationContext(),
                getApplicationContext().getPackageName() + ".provider", file);

        MediaScannerConnection.scanFile(ImagePickerDemo.this,
                new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        cameraView.startCapturingVideo(file, 8000);
    }

    public void videoPreview(final Uri uri) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.video_preview);

        final VideoView videoView = (VideoView) dialog.findViewById(R.id.img_preview_video);
        Button saveButton = (Button) dialog.findViewById(R.id.btn_image_preview_save);
        Button cancelButton = (Button) dialog.findViewById(R.id.btn_image_preview_canel);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(videoView);
            }
        });

        Uri videoUri = uri;
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();
                float viewWidth = videoView.getWidth();
                lp.height = (int) (viewWidth * (videoHeight / videoWidth));
                videoView.setLayoutParams(lp);
                playVideo(videoView);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Uri> vid = new ArrayList<>();
                vid.add(uri);
                PublishActivity.openWithVideoUri(ImagePickerDemo.this, vid);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void playVideo(VideoView videoView) {
        if (videoView.isPlaying()) return;
        videoView.start();
    }

}
