package ci.velocity.lampa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonEnable;

    private Camera mcamera;
    private android.hardware.Camera.Parameters fparams;
    private MediaPlayer mp3;

    private ImageView imageFlashlight;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;
    private boolean isEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        imageFlashlight = (ImageView) findViewById(R.id.imageFlashlight);
        //buttonEnable = (Button) findViewById(R.id.buttonEnable);

        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;



        imageFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (hasCameraFlash) {
                    if (flashLightStatus)
                        flashLightOff();
                    else
                        flashLightOn();
                } else {
                    Toast.makeText(MainActivity.this, R.string.noflash,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);

            flashLightStatus = true;
            imageFlashlight.setImageResource(R.mipmap.on2);
            playSound();
        } catch (CameraAccessException e) {
        }
    }


    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            imageFlashlight.setImageResource(R.mipmap.off2);
            playSound();
        } catch (CameraAccessException e) {
        }
    }

    private void playSound() {
        if (flashLightStatus) {
            mp3 = MediaPlayer.create(MainActivity.this, R.raw.off);
        } else {
            mp3 = MediaPlayer.create(MainActivity.this, R.raw.on);
        }
        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp3.start();
    }



}
