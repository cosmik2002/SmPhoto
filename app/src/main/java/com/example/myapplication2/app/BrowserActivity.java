package com.example.myapplication2.app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrowserActivity extends AppCompatActivity {
    //private WebView mWebView;
    //String key = "AIzaSyAGF4h2ADggTs9x4yxjaWnTqTdv8Y1GKPM";
    String key = "AIzaSyD0tOCJjYhXJCuA6gquyR9KwuUBXzLshls";
    String cx = "016929100794671277104:e8hg_ki31f8";
    String qry = "spring";// search key word
    ImageArrayAdapter imageAdapter;
    GridView grid;
    TextView lab1;
    ImageView image;
    List<Result> searchResults;
    //SearchClass search;
    boolean searching = false;
    boolean havePages;
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        grid = (GridView) findViewById(R.id.gridView);
        lab1 = (TextView) findViewById(R.id.textView2);
        image = (ImageView) findViewById(R.id.imageView3);
        qry = getIntent().getStringExtra("code");
        searchResults = new ArrayList<Result>();
        searching = true;
        havePages = true;
        new SearchClass().execute(qry,"0");
        imageAdapter = new ImageArrayAdapter(this,searchResults);
        grid.setAdapter(imageAdapter);
        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            Integer page = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView gridView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (gridView.getLastVisiblePosition() + 1 == totalItemCount && !searching && havePages) {
                    isLoading = true;
                    page++;
                    lab1.setText(page.toString());
                    new SearchClass().execute(qry,page.toString());
                }
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //image.setImageBitmap(((BitmapDrawable)((ImageView)view).getDrawable()).getBitmap());
              new BitmapDownloaderTask(image).execute(searchResults.get(position).getLink());
            }
        });
    }
    class SearchClass extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                    }
                };

                JsonFactory jsonFactory = new JacksonFactory();
                HttpTransport httpTransport = new NetHttpTransport();

                Customsearch customsearch = new Customsearch.Builder(httpTransport, jsonFactory, httpRequestInitializer)
                        .setApplicationName(getResources().getString(R.string.app_name))
                        .build();
                long page =  Integer.parseInt(params[1]);
                Customsearch.Cse.List list = customsearch.cse().list(params[0]);
                list.setKey(key);
                list.setCx(cx);
                list.setSearchType("image");
                if(page>0) {
                    list.setStart(10 * page);
                }
                Search results = list.execute();

                if(results.getItems()!=null) {
                    searchResults.addAll(results.getItems());
                }else{
                    havePages = false;
                    //Toast.makeText(BrowserActivity.this,"no search res",Toast.LENGTH_LONG).show();
                }
                //return results;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute(){
            searching = true;
        }
        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
            imageAdapter.notifyDataSetChanged();
            searching = false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
