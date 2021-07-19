package com.example.quakereport;

import android.content.Context;

import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Quake>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String url;
    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url= url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Quake> loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<Quake> quake= QueryUtils.fetchEarthquakeData(url);
        return quake;
    }
}
