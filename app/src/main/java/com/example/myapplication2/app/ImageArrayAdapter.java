package com.example.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * Created by nicholas on 01.02.2017.
 */
public class ImageArrayAdapter extends BaseAdapter{
    private Context mContext;
    private List<Result> search;

    public ImageArrayAdapter(Context c, List<Result> s) {
        mContext = c;
        search = s;
    }

    @Override
    public int getCount() {
        if(search != null)
         return search.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return search.get(position).getImage().getThumbnailLink();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(search==null) return null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.mipmap.ic_launcher);
        new BitmapDownloaderTask(imageView).execute(search.get(position).getImage().getThumbnailLink());
        return imageView;
    }
   /* class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return getImageBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Bitmap", "Error getting bitmap", e);
        }
        return bm;
    }*/
}
