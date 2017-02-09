package com.example.myapplication2.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.util.UUID;
public class MyActivity extends Activity {
    private static int TAKE_PICTURE = 1;
    private Uri mOutputFileUri;
    private ImageView mImageView;
    private EditText edtText;
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // если хотим, чтобы приложение постоянно имело портретную ориентацию
        //setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);

        // если хотим, чтобы приложение было полноэкранным
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // и без заголовка
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);
        //Logger.setLogLevel(Logger.LogLevel.DEBUG);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");//for Qr code, its "QR_CODE_MODE" instead of "PRODUCT_MODE"
                intent.putExtra("SAVE_HISTORY", false);//this stops saving ur barcode in barcode scanner app's history
                //intent.putExtra("RESULT_ORIENTATION",getResources().getConfiguration().orientation);
                startActivityForResult(intent, 0);
            }
        });
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this,BrowserActivity.class);
                intent.putExtra("code",edtText.getText().toString());
                startActivity(intent);
            }
        });
        //button3.setVisibility(View.GONE);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFullImage();
            }
        });
        mImageView = (ImageView)findViewById(R.id.imageView);
        edtText = (EditText) findViewById(R.id.editText);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                TextView tw = (TextView) findViewById(R.id.textView);
                tw.setText(contents);
                edtText.setText(contents);
                barcode = contents;
            } else
            if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
        if (requestCode == TAKE_PICTURE) {
            // Проверяем, содержит ли результат маленькую картинку
            if (data != null) {
                if (data.hasExtra("data")) {
                    Bitmap thumbnailBitmap = data.getParcelableExtra("data");
                    // TODO Какие-то действия с миниатюрой
                    mImageView.setImageBitmap(thumbnailBitmap);
                }
            } else {
                // TODO Какие-то действия с полноценным изображением,
                // сохраненным по адресу mOutputFileUri
                uploadMultipart();
                mImageView.setImageURI(null);
                mImageView.setImageURI(mOutputFileUri);
            }
        }
    }
    private void saveFullImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                edtText.getText()+".jpg");
        mOutputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
        startActivityForResult(intent, TAKE_PICTURE);

    }
    public void uploadMultipart() {
        //getting name for the image
        String name = edtText.getText()+".jpg";

        //getting the actual path of the image
        String path = mOutputFileUri.getPath();// Environment.getExternalStorageDirectory().getPath();

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "http://192.168.100.49/api/upload.php")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    /*.setNotificationConfig(new UploadNotificationConfig())*/
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
