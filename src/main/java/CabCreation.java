import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CabCreation {

    int noOfCabs;
    Date start_time;
    Utilities helper = new Utilities();

    CabCreation(int noOfCabs, Date start_time) {
        this.noOfCabs = noOfCabs;
        this.start_time = start_time;
    }

    List<Cab> createCabs(Map<String, Hexagon> hexagon_map) throws ParseException {

        List<Cab> cabList = new ArrayList<>();

        // initialize cabList current_locations to randomized hexagons
        int equidistant = noOfCabs / hexagon_map.size();


        int count = 1, i;

        List<String> hexagon_list = new ArrayList<String>(hexagon_map.keySet());

        Cab cab;

        while (count < noOfCabs) {

            i = (count - 1) % hexagon_map.size();
            String hexagon_id = hexagon_list.get(i);

            Location current = hexagon_map.get(hexagon_id).center;
            String current_time = helper.getTimeStamp(new Date());
            Hexagon current_hexagon = hexagon_map.get(hexagon_id);

            cab = new Cab(hexagon_id, current_time, current);
            cabList.add(cab);
            current_hexagon.cabList.add(cab);

            count++;
        }

        //System.out.println("hexagon-size: " + hexagon_map.size() + " equidistant: " + equidistant + " no of cabs: " + cabList.size());
        return cabList;
    }
}
