package com.example.myapplication2.app;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by nicholas on 09.02.2017.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        ImageLoader imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(this)); // Проинициализировали конфигом по умолчанию
    }
}
