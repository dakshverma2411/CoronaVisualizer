package Utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
   private final String link = "https://api.covid19api.com/summary";

   public static String getResponseFromHttpUrl(URL url) throws IOException {

      String result = "";
      HttpURLConnection connection=null;
      try {
         connection = (HttpURLConnection) url.openConnection();
         Log.i("msg", url.toString());
         InputStream in = connection.getInputStream();
         Log.i("msg", url.toString());
         InputStreamReader reader = new InputStreamReader(in);
         int data = reader.read();
         while (data != -1) {
            result += (char) data;
            data = reader.read();
         }
         return result;

      } catch (Exception e) {
         Log.i("msg", "in malformed");
      }

      return null;
   }



}
