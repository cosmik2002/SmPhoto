package com.example.myapplication2.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nicholas on 30.01.2017.
 */
public class PhotoActivity extends Activity implements Camera.AutoFocusCallback,Camera.PictureCallback,SurfaceHolder.Callback,Camera.PreviewCallback {
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //Button button = (Button) findViewById(R.id.button3);
        preview = (SurfaceView) findViewById(R.id.surfaceView2);
        SurfaceHolder surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(PhotoActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        camera = Camera.open();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success)
        {
            // если удалось сфокусироваться, делаем снимок
            camera.takePicture(null, null, null, this);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try
        {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        ViewGroup.LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
            ;
        }
        else
        {
            // ландшафтный
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        preview.setLayoutParams(lp);
        camera.startPreview();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // сохраняем полученные jpg в папке /sdcard/CameraExample/
        // имя файла - System.currentTimeMillis()

        try
        {
            File saveDir = new File("/sdcard/SmPfoto/");

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            FileOutputStream os = new FileOutputStream(String.format("/sdcard/SmPfoto/%d.jpg", System.currentTimeMillis()));
            os.write(data);
            os.close();
        }
        catch (Exception e)
        {
        }

        // после того, как снимок сделан, показ превью отключается. необходимо включить его
        camera.startPreview();


    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
