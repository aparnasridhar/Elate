package com.aparnasridhar.elate.remote;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
           // url = new URL("https://dl.dropboxusercontent.com/u/231329/xyzreader_data/data.json" );
            //https://api.myjson.com/bins/3b9i0
            url = new URL("https://api.myjson.com/bins/lz6o" );
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
        }

        BASE_URL = url;
    }
}
