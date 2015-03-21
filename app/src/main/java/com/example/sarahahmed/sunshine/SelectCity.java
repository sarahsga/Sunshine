package com.example.sarahahmed.sunshine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sarah Ahmed on 2/7/2015.
 */
public class SelectCity extends ActionBarActivity {
    static String city;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_main);
        Log.e("create","Select City created");

    }

    public static class SelectCityFragment extends Fragment implements AdapterView.OnItemClickListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.e("createsd", "Inside Select city fragment");

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.select_city,container);

            List<String> countries = new ArrayList<String>();
            Locale[] locales = Locale.getAvailableLocales();
            String country;

            for (Locale locale : locales) {
                String iso = locale.getISO3Country();
                String code = locale.getCountry();
                String name = locale.getDisplayCountry();

                if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                    countries.add(name);
                    Log.e("country",name);
                }
            }
            Log.e("Select city", "before adapter");
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,countries);
            Log.e("Select city","before listview initialization");
            ListView listView = (ListView) rootView.findViewById(R.id.select_city_listView);
            Log.e("Select city", "before setAdapter() call");
            listView.setAdapter(adapter);
            Log.e("Select city","after setAdapter() call");


            listView.setOnItemClickListener(this);
            return rootView;
        }

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.e("Select City", "Inside onItemClick()");
            city = (String)((TextView)view).getText();
            try {
                Intent intent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                Log.e("Select City", "Class prolly found");
                intent.putExtra("com.example.sarahahmed.sunshine.cityName", city);
                startActivity(intent);
            } catch(Exception ex) {
                Log.e("Select city", "Class not found exception");
            }
        }

    }
}
