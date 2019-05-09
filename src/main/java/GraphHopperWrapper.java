import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GraphHopperWrapper {
    public static int time(double source_lat, double source_long, double dest_lat, double dest_long) {
        try {

            URL url = new URL("http://localhost:8989/route?point=" + source_lat + "%2C" + source_long + "&point=" + dest_lat + "%2C" + dest_long + "&type=json&locale=en-US&vehicle=car&weighting=fastest&elevation=false");

            URLConnection connection = url.openConnection();
            connection.connect();
            int time = Integer.MAX_VALUE;
            // Cast to a HttpURLConnection
            if ( connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int code = httpConnection.getResponseCode();

                if (code==200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    String strTemp = "";
                    String k = "";
                    while (null != (strTemp = br.readLine())) {
                        k = strTemp;
                    }
                    JSONObject myObject = new JSONObject(k);
                    JSONArray nested = myObject.getJSONArray("paths");
                    if(nested !=null) {
                        JSONObject rec = nested.getJSONObject(0);
                        time = rec.getInt("time");
                    }
                    return time;
                }
                return time;
            }
            return time;

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}