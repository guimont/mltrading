package com.mltrading.models.parser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by gmo on 17/06/2015.
 */
public class ParserCommon {



    public static String loadUrl(URL url) {
        InputStream stream = null;
        try {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            stream =  httpcon.getInputStream();
            return loadStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Charge le contenu d'un stream dans un string
     *
     * @param stream
     * @return
     * @throws IOException
     */
    public static String loadStream(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        char[] buffer = new char[1024];
        int count;
        StringBuilder str = new StringBuilder();
        while ((count = reader.read(buffer)) != -1)
            str.append(buffer, 0, count);
        return str.toString();
    }
}
