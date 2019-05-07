import com.uber.h3core.H3Core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utilities {

    public String getTimeStamp(Date date) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int minutes = calendar.get(Calendar.MINUTE);
        int mod = minutes % 5;
        mod = mod < 3 ? -mod : (5 - mod);
        calendar.set(Calendar.MINUTE, minutes + mod);
        calendar.set(Calendar.SECOND, 00);

        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    /***
     *
     *
     * TO BE CHANGED!!
     */
//    public List<List<String>> readNeighborsList(String hexagon_id, String neighbors) {
//
//        neighbors = neighbors.replace("{", "");
//        neighbors = neighbors.replace("}", "");
//        neighbors = neighbors.replace("'", "");
//        neighbors = neighbors.replace(" ", "");
//        List<List<String>> neighbors_list = new ArrayList<>();
//        String[] list = neighbors.split(":");
//
//        for (int i = 0; i < list.length; i++) {
//            if (Objects.equals(list[i], hexagon_id))
//                continue;
////            neighbors_list.add(list[i]);
//        }
//        return neighbors_list;
//    }
    public List<List<String>> readNeighborsList(String hexagon_id, String neighbors) {

        neighbors = neighbors.replace("{", "");
        neighbors = neighbors.replace("}", "");
        neighbors = neighbors.replace("'", "");
        neighbors = neighbors.replace(" ", "");
        List<List<String>> neighbors_list = new ArrayList<>();
        String[] list = neighbors.split(":");

        int k = 0, t = 0;
        for (int i = 1; i <= 4; i++) {
            k += 6 * i;
            List<String> n = new ArrayList<>();
            while (t < k) {
                t++;
                if (Objects.equals(list[t - 1], hexagon_id))
                    continue;
                n.add(list[t - 1]);
            }
            if (n.size() <= 0)
                System.out.println(i + " Ring size: " + n.size());
            neighbors_list.add(n);
        }
        if (neighbors_list.size() <= 0)
            System.out.println("Neighbours list size: " + neighbors_list.size());
        return neighbors_list;
    }

    public static String getHexFromGeo(double latitude, double longitude) throws IOException {

        H3Core h3 = H3Core.newInstance();

        return h3.geoToH3Address(latitude, longitude, 9);

    }
}
