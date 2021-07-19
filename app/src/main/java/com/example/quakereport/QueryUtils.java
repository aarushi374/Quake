package com.example.quakereport;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Helper methods related to requesting and receiving earthquake data from USGS.
     */

        /**
         * Create a private constructor because no one should ever create a {@link QueryUtils} object.
         * This class is only meant to hold static variables and methods, which can be accessed
         * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
         */
        private QueryUtils() {
        }

        public static ArrayList<Quake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        ArrayList<Quake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
        /**
         * Return a list of  objects that has been built up from
         * parsing a JSON response.
         */
        public static ArrayList<Quake> extractEarthquakes(String json) {

            // Create an empty ArrayList that we can start adding earthquakes to
            ArrayList<Quake> earthquakes = new ArrayList<>();

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {
                JSONObject strjson=new JSONObject(json);
                JSONArray ja=strjson.getJSONArray("features");
                for(int i=0;i< ja.length();i++) {
                    JSONObject fo=ja.getJSONObject(i);
                    JSONObject properties=fo.getJSONObject("properties");
                    double mag= properties.getDouble("mag");
                    String url=properties.getString("url");
                    //DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
                    //String magnitude=magnitudeFormat.format(mag);
                    String place=properties.optString("place");
                    int index=place.indexOf("of");
                    String direction=null,country=null;
                    if(index==-1)
                    {
                        country=place;
                        direction=null;
                    }
                    else
                    {
                        direction = place.substring(0, index + 2);
                        country = place.substring(index + 3);
                    }
                    long ltime=properties.getLong("time");
                    Date obj=new Date(ltime);
                    SimpleDateFormat dateformat=new SimpleDateFormat("MMM DD, yyyy");
                    String day= dateformat.format(obj);
                    SimpleDateFormat timeformat=new SimpleDateFormat("h:mm a");
                    String time=timeformat.format(obj);
                    earthquakes.add(new Quake(mag,country,day,time,direction,url));
                }
                // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                // build up a list of Earthquake objects with the corresponding data.

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return earthquakes;
        }

    }

