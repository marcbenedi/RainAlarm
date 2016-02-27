package com.rainalarm.marcb.rainalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BackgroundUpdate extends AsyncTask<Void, Void, String> {
    //Params,Progress,Result

    private String resultConnection = new String ("Valor inicial");
    private Context context;
    MainActivity a = new MainActivity();
    String name = a.PREFS_NAME;
    SharedPreferences preferences;
    String city;
    int LanguagePos;
    int UnitsPos;
    String units[] = {"ERROR","metric","imperial"};
    String language[] = {"ERROR","es","en"};

    public BackgroundUpdate(Context c)
    {
        context = c;
        preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        city = preferences.getString("city", "Barcelona");
        LanguagePos = preferences.getInt("language", 0);
        UnitsPos = preferences.getInt("units",0);
    }

    @Override
    protected String doInBackground(Void... params) {
        //ERROR NetworkOnMainThreadException
        //Possible solució: Crear un nou thread
        final String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+city+"," +
                "Es&mode=json&units="+units[UnitsPos]+"&cnt=2&lang="+language[LanguagePos]+
                "&appid=44db6a862fba0b067b1930da0d769e98";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{
            URL url3 = new URL(url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url3.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            //Guardem el resultat
            resultConnection = buffer.toString();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
        return resultConnection;
        //String with all the JSON response from the API
    }

}
