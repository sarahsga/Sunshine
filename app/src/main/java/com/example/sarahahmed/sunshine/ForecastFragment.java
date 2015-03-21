package com.example.sarahahmed.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Sarah Ahmed on 2/6/2015.
 */

public class ForecastFragment extends Fragment {
    Bundle bundle;
    List<String> forecastList;
    ArrayList weatherDescriptionArray = null;
    View rootView;
    public String cityGet = "Karachi";
    public ArrayAdapter<String> adapter;
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setHasOptionsMenu(true);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        Log.e(getClass().getSimpleName(), "onActivityCreated()");
        String str = null;
        try {
            MainActivity activity = (MainActivity)getActivity();
            str =  activity.getMyData();
        } catch(Exception ex) {
            Log.e(getClass().getSimpleName(), ex.toString());
        }
        Log.e(getClass().getSimpleName(), "str = " + str);
        if ( str != null) {
            Log.e("Forecast Fragment", "bundle != null");
            cityGet = str;
            Log.e("Forecast Fragment", "cityGet = " + cityGet);
        } else {
            Log.e(getActivity().toString(),"bundle == null");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        Log.w("Forecast Fragment", "inside onoptionscreatemenu fragment");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.w("Forecast Fragment", "inside onoptionsitemselected fragment");

        if (id == R.id.action_test ) {
            Log.e("Forecast Fragment","action test selected");
            try {
                Intent intent = new Intent(MyApplication.getAppContext(), Test1.class);
                startActivity(intent);
            } catch(Exception ex) {
                Log.e("exception caught", "Exception caught");
                Log.e(getClass().getSimpleName(),"test clicked " + ex.getStackTrace());
            }
        }

        if ( id == R.id.action_refresh) {
            new FetchWeatherTask().execute("Karachi");
            return true;
        }

        if ( id == R.id.action_city) {
            try {
                Class clazz = Class.forName("com.example.sarahahmed.sunshine.SelectCity" );
                Intent intent = new Intent(MyApplication.getAppContext(),clazz);
                startActivity(intent);
            } catch(ClassNotFoundException e) {
                Log.e("Error", "Select City Class not found exception by Sarah");
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("sarah", "inside oncreateview fragment");

        String[] str = {
                "a", "b", "c", "d", "e",
        };
        forecastList = new ArrayList<String>(Arrays.asList(str));
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview, forecastList);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_fragment);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                test(view);
            }
        });
        return rootView;
    }

    public void test(View view) {

        try {
            String str = ((TextView)view).getText().toString();
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(MyApplication.getAppContext(),detailActivity.class);
            intent.putExtra("weatherInfo", ((TextView)view).getText());
            startActivity(intent);
        } catch (Exception e) {
            Log.e("test() in ForecastFragment",e.getLocalizedMessage());
        }

    }

    public class FetchWeatherTask extends AsyncTask<String,Void,String[]> {

        private String forecastJsonStr;
        private String city = cityGet;
        private Uri.Builder uriBuilder = new Uri.Builder();
        private final String LOG_TAG = "It's a log tag!! Wohooo...";

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            Log.d(LOG_TAG, "Inside doInBackground()");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
// Will contain the raw JSON response as a string.
            forecastJsonStr = null;
            try {
// Construct the URL for the OpenWeatherMap query
// Possible parameters are avaiable at OWM's forecast API page, at
// http://openweathermap.org/API#forecast
                uriBuilder.scheme("http");
                uriBuilder.authority("api.openweathermap.org");
                uriBuilder.path("data/2.5/forecast/daily");
                uriBuilder.appendQueryParameter("q", params[0]);
                uriBuilder.appendQueryParameter("mode","json");
                uriBuilder.appendQueryParameter("units","metric");
                uriBuilder.appendQueryParameter("cnt","7");

                URL url2 = new URL(uriBuilder.build().toString());
                Log.e("My URL is ", url2.toString());
// Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url2.openConnection();
                urlConnection.setRequestMethod("GET");
                Log.e(LOG_TAG,"Before connect");
                urlConnection.connect();
                Log.e(LOG_TAG,"After connect");
// Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
// Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
// But it does make debugging a *lot* easier if you print out the completed
// buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
// Stream was empty. No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.e(LOG_TAG,"Forecast JSON String" + forecastJsonStr);
                parseJSON();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView;
                        textView = (TextView)(getActivity().findViewById(R.id.forecastFragment_textView));
                        for (int k=0 ; k<weatherDescriptionArray.size() ; k++) {
                            textView.append((String)(weatherDescriptionArray.get(k)));
                        }
                    }
                });
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getLocalizedMessage());
// If the code didn't successfully get the weather data, there's no point in attemping
// to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getWeatherDataFromJson(forecastJsonStr,7);
            } catch (JSONException e) {
                Log.e("doInBackGround() FetchWeatherTask$ForecastFragment", e.getLocalizedMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            forecastList.clear();
            for (String str : strings) {
                forecastList.add(str);
            }
            adapter.notifyDataSetChanged();

        }

        public void parseJSON() {
            try {
                JSONObject jObj = new JSONObject(forecastJsonStr);
                JSONArray jArray = jObj.getJSONArray("list");
                weatherDescriptionArray = new ArrayList();
                for ( int i=0 ; i<jArray.length() ; i++ ) {
                    weatherDescriptionArray.add(jArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                            .getString("description"));
                    Log.e("Forecast Fragment parseJSON ", (String)(weatherDescriptionArray.get(i)));
                }
            } catch (JSONException e) {
                Log.e("parseJSON in Forecast Fragment",e.getLocalizedMessage());
            }
        }

        public double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex) {
            try {
                JSONObject jObj = new JSONObject(weatherJsonStr);
                Double maxTemp = jObj.getJSONArray("list").getJSONObject(dayIndex).getJSONObject("temp").getDouble("max");
                return maxTemp;
            } catch (JSONException e) {
                Log.e("getMaxTemperatureForDay() in FetchWeatherTask$ForecastFragment", e.getLocalizedMessage());
                return -1;
            }

        }

        private String getReadableDateString(long time) {
            Date date = new Date(time*1000);
            SimpleDateFormat format = new SimpleDateFormat("EE, MMM d");
            return format.format(date);
        }

        private String formatHighLows(double high, double low) {
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);
            String str = roundedHigh + "/" + roundedLow;
            return str;
        }

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException{
            JSONObject jObj = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = jObj.getJSONArray("list");

            String[] resultStrs = new String[numDays];
            for ( int i=0 ; i<weatherArray.length() ; i++) {
                String day, description, highAndLow;

                //get the json object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                Long dt = dayForecast.getLong("dt");
                day = getReadableDateString(dt);
                Double min = dayForecast.getJSONObject("temp").getDouble("min");
                Double max = dayForecast.getJSONObject("temp").getDouble("max");
                highAndLow = formatHighLows(max,min);

                description = dayForecast.getJSONArray("weather").getJSONObject(0).getString("main");

                resultStrs[i] = day + "-" + description + "-" + highAndLow;
                Log.e("getWeatherDataFromJsonStr() in FetchWeatherTask$ForecastFragment", resultStrs[i]);
            }
            return resultStrs;
        }
    }
}

