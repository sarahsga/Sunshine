package com.example.sarahahmed.sunshine;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Sarah Ahmed on 2/19/2015.
 */
public class Test1 extends Activity {
    ArrayList strArray2 = null;
    public String jsonStr2 = null;
    public String title2 = null;
    public ArrayList imgArray = null;
    String test1 = getClass().getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.e("Test1", "onCreate()");
            setContentView(R.layout.test1_layout);
            new FetchTMDB().execute();
            Log.e(test1,"after execute");
        } catch (Exception ex ) {
            Log.e("Test1",ex.getLocalizedMessage());
        }
    }
    public class FetchTMDB extends AsyncTask<Void,Void,Void> {
        public final String LOG_TAG = getClass().getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {
            Log.e(LOG_TAG,"doInBackground()");
            HttpURLConnection urlCon = null;
            String jsonStr;
            try {
                urlCon = null;
                String Str = "Hunger Games";
                String encodedStr = null;
                try {
                    encodedStr = URLEncoder.encode(Str, "UTF-8");
                } catch (Exception ex) {
                    Log.e(LOG_TAG, ex.getLocalizedMessage());
                }
                Uri uri = null;
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("https").authority("api.themoviedb.org").path("/3/search/movie")
                        .appendQueryParameter("api_key", "a9adef2ff5e64f0d1c9a5147f16d2614")
                        .appendQueryParameter("query",encodedStr);
                URL url = new URL(uriBuilder.build().toString());

                urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setRequestMethod("GET");

                Log.e(LOG_TAG, "String: " + Str);
                Log.e(LOG_TAG, "Encoded String: " + encodedStr);
                jsonStr = null;
            } catch (Exception ex) {
                Log.e(test1,ex.getLocalizedMessage());
            }
            try {
                urlCon.connect();
                Log.e(test1,"URL connected");
            } catch(Exception ex) {
                Log.v(LOG_TAG, ex.getLocalizedMessage());
            }
            try {
                Log.e(test1,"inside next try");
                InputStream inputStream = urlCon.getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = null;
                if(inputStream == null) {
                    jsonStr = null;
                    Log.e(test1,"input stream was null");
                }
                Log.e(test1,"after if");
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if ( buffer.length() == 0) {
                    jsonStr = null;
                    Log.e(test1,"jsonStr = null");
                }
                else {
                    jsonStr = buffer.toString();
                    jsonStr2 = jsonStr;
                    Log.e(test1,"jsonStr = " + jsonStr);
                     strArray2 =  parsingJSON();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView;
                            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutTest1);
                            for ( int i = 0 ; i < strArray2.size() ; i++ ) {
                                ((TextView) (findViewById(R.id.textView))).append("\n" + (String) (strArray2.get(i)));
                                imageView = new ImageView(getApplicationContext());
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                    imageView.setImageURI(android.net.Uri.parse(getImageURL().toString()));
                                    layout.addView(imageView);

                            }
                        }
                    });
                }

            } catch(Exception ex) {
                Log.e(test1,ex.getLocalizedMessage());
            }
            return null;
        }

        public ArrayList parsingJSON() {
            String title;
            ArrayList strArray = new ArrayList();
            imgArray = new ArrayList();
            try {
                JSONObject jObj = new JSONObject(jsonStr2);
                JSONArray jsonArray = jObj.getJSONArray("results");
                for ( int i = 0 ; i < jsonArray.length() ; i++ ) {
                    strArray.add(jsonArray.getJSONObject(i).getString("original_title"));
                    imgArray.add(jsonArray.getJSONObject(i).getString("poster_path"));
                    Log.e(test1, (String)(strArray.get(i)));
                }

                return strArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public URL getImageURL() {
            String base_url;
            Uri.Builder uriBuilderBase = new Uri.Builder();
            Uri.Builder building = new Uri.Builder();
            uriBuilderBase.scheme("https").authority("image.tmdb.org").
                    appendPath("t/p/original/");

            for (int j=0 ; j<imgArray.size() ; j++) {
                building = uriBuilderBase.appendPath((String)(imgArray.get(j)));
                try{
                    URL url = new URL(building.build().toString());
                    return url;
                } catch(MalformedURLException ex) {
                    Log.e("test1", "Malformed url exception");
                }
            }
            return null;
        }
    }
}
