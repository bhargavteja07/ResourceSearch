import java.text.ParseException;
import java.util.*;

public class Simulation {

    private int[] indexNeighbourArr = new int[6];
    Utilities helper = new Utilities();


    public static void addCabToHexagon(Cab cab) {
        if (ResourceSearchMain.hexagon_map.get(cab.current_hexagon_id) != null) {
            ResourceSearchMain.hexagon_map.get(cab.current_hexagon_id).cabList.remove(cab);
        }
        if (ResourceSearchMain.hexagon_map.get(cab.destination_hex) != null) {
            ResourceSearchMain.hexagon_map.get(cab.destination_hex).addCab(cab);
        }
    }

    public List<Cab> simulate(List<Cab> cabList, Result res) {

        List<Cab> temp = new ArrayList<>();
        for (int i = 0; i < cabList.size(); i++) {

            Cab cab = cabList.get(i);
            if (cab.status == 1) {
                if (cab.journeytime < 0) {
                    cab.journeytime = 0;
                    cab.status = 0;

                    addCabToHexagon(cab);
                    cab.current_hexagon_id = cab.destination_hex;
                    cab.destination_hex = "";

                    temp.add(cab);
                } else {
                    cab.journeytime -= res.global_time / 60000.0;
                    continue;
                }
            }
            if(cab.status==2) {

                cab.current_travel_time -= res.global_time;

                if (cab.current_travel_time > 0) {
                    res.search_time += res.global_time / 60000.0;
                    continue;
                } else if (cab.current_travel_time < 0 && !Objects.equals(cab.destination_hex, "")) {
                    addCabToHexagon(cab);
                    cab.current_hexagon_id = cab.destination_hex;
                    cab.destination_hex = "";
                    cab.status = 0;
                }
            }
            String cab_hex_id = cab.current_hexagon_id;
            Hexagon source_hexagon = ResourceSearchMain.hexagon_map.get(cab_hex_id);


            if(source_hexagon!=null && source_hexagon.neighborList !=null) {

                List<String> firstRingNeighbours = source_hexagon.neighborList.get(0);
                for (int l = 0; l < firstRingNeighbours.size(); l++)
                    indexNeighbourArr[l] = l;

                Map<String, Integer> expectedNumbers = new HashMap<>();

                // can be updated to CLOCK TIME
                String current_time = "06:00:00";
                try {
                    current_time = helper.getTimeStamp(res.curr_time);
                }
                catch (ParseException ex){

                }

                // gets the first layer/ring of neighbours.


                for (int j = 0; j < firstRingNeighbours.size(); j++) {
                    String neighbour = firstRingNeighbours.get(j);
                    if (ResourceSearchMain.hexagon_map.get(neighbour) != null && ResourceSearchMain.hexagon_map.get(neighbour).getTimestamps() != null) {
                        expectedNumbers.put(neighbour,
                                (ResourceSearchMain.hexagon_map.get(neighbour).getTimestamps().getOrDefault(current_time, 0)) + 1);
                    } else
                        expectedNumbers.put(neighbour, 1);
                }
                // Random based on Probability Distribution
                //int chosenIndex = RandomNumberGenerator.customizedRandom(indexNeighbourArr, expectedNumbers, firstRingNeighbours);
                // // Normal Random
                int chosenIndex = RandomNumberGenerator.normalRandom(firstRingNeighbours.size()-1);

                String destination_hex_id = firstRingNeighbours.get(chosenIndex);
                if (ResourceSearchMain.hexagon_map.get(destination_hex_id) != null) {

                    Location destination = ResourceSearchMain.hexagon_map.get(firstRingNeighbours.get(chosenIndex)).center;

                    int milliSeconds = Graphhopper.time(source_hexagon.center.latitude, source_hexagon.center.longitude, destination.latitude, destination.longitude);

                    cab.setDestination(destination_hex_id, 0, milliSeconds, 2);
//                System.out.println("travel time: " + cab.current_travel_time);
                }


            }
        }
        return temp;
    }
}