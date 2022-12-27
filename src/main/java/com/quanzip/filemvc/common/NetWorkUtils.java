package com.quanzip.filemvc.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetWorkUtils {
    public static boolean checkNetworkAvailable() {
        try {
            URL url = new URL("http://google.com");
            URLConnection urlConn = url.openConnection();
            urlConn.connect();
            return true;
        } catch (IOException e) {
            System.out.println("Lost connection!!");
            return false;
        }
    }
}
