import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Assignment {

    public static Queue<Resource> allocate(Queue<Resource> resources, List<Cab> cabList, int mlt, Result result) {

        int res_count = resources.size();
        int min_time;
        int count = 0;

        int outOfBoundaryHexagons = 0;

        while (count < res_count) {

            min_time = 2147483647;
//            System.out.println("count: "+count + " resource size: "+resources.size());
            Resource r = resources.poll();
//            System.out.println("resource hex: " + r.hexagon_id);
            int cab_id = -1;
            count++;
            if (r.wait_time >= (mlt / 60000.0)) {
//                System.out.println("Dropped cab: " + r.hexagon_id + " because of wait_time: " + r.wait_time);
                result.total_dropped += 1;
                result.total_waiting -= 1;
                continue; // resource dropped from queue
            }

            double src_lat = r.pickup_location.latitude;
            double src_long = r.pickup_location.longitude;
            List<List<String>> neighboursByLevel;
            if (ResourceSearchMain.hexagon_map.get(r.hexagon_id) != null) {
                neighboursByLevel = ResourceSearchMain.hexagon_map.get(r.hexagon_id).neighborList;
            } else {
                continue;
            }

            List<Cab> neighbourCabList = new ArrayList<>();
            for (List<String> levelList : neighboursByLevel) {
                for (String neighbour : levelList) {
                    if (ResourceSearchMain.hexagon_map.containsKey(neighbour))
                        neighbourCabList.addAll(ResourceSearchMain.hexagon_map.get(neighbour).cabList);
                    else {
//                        outOfBoundaryHexagons++;
                        continue;
                    }
                }
            }


            for (int j = 0; j < neighbourCabList.size(); j++) {
                if (neighbourCabList.get(j).status != 1) {

                    double dest_lat = neighbourCabList.get(j).current.latitude;
                    double dest_long = neighbourCabList.get(j).current.longitude;
                    if (neighbourCabList.get(j).status == 0 && src_lat != 0.0 && src_long != 0.0
                            && dest_lat != 0.0 && dest_long != 0.0) {

                        int curr_cab_time = Graphhopper.time(src_lat, src_long, dest_lat, dest_long);

                        // finding closest cab
                        if (curr_cab_time < min_time) {
                            min_time = curr_cab_time;
                            cab_id = j;
                        }
                    }
                }
            }

            // if cab is available and can reach before (mlt - already waited time)
            if (cab_id != -1 && ((min_time / 60000.0) + r.wait_time) <= (mlt / 60000.0)
                    && r.pickup_location.latitude != 0.0 && r.pickup_location.longitude != 0.0
                    && r.drop_location.latitude != 0 && r.drop_location.longitude != 0.0) {

                Cab assignedCab = neighbourCabList.get(cab_id);
                // status 1 indicates successful assignment
                assignedCab.status = 1;
                //System.out.println("Cab allocated:" + cab_id + " for resource at: " + r.hexagon_id);

//                result.search_time += assignedCab.total_waiting_time;
//                System.out.println(assignedCab.total_waiting_time);
                assignedCab.total_waiting_time = 0;
                result.assigned_resources += 1;

                int time = Graphhopper.time(r.pickup_location.latitude, r.pickup_location.longitude, r.drop_location.latitude, r.drop_location.longitude);
                assignedCab.journeytime = (time + min_time) / 60000.0;
                try {
                    assignedCab.destination_hex = Utilities.getHexFromGeo(r.drop_location.latitude, r.drop_location.longitude);
                }
                catch (IOException e){
                    //System.out
                }

                if (r.wait_time > 0) {
                    result.total_waiting -= 1;
                }

            } else {
                resources.add(r);
                r.wait_time += result.global_time/60000.0;
                result.wait_time += result.global_time / 60000.0;
                result.total_waiting += 1;
                r.status = false;
//                System.out.println("wait time: " + min_time);
            }
        }



        System.out.println("Total Dropped Resources: " + result.total_dropped);
        System.out.println("Total Assigned Resources: " + (result.assigned_resources));
//        System.out.println("Total Waiting Resources: " + (result.total_waiting));
//        System.out.println("Total Wait Time: " + result.wait_time);
//        System.out.println("Total Search Time: " + result.search_time + " in minutes");
//        System.out.println("Current Time: " + result.curr_time);
        System.out.println();
        return resources;
    }

    public static void main(String[] args) {
        Queue<Resource> resourceQueue = new LinkedList<>();
        // obtain Resource list - given CSV of rides between input startTime and endTime,
        ResourceAddition resourceAddition = new ResourceAddition();
        Queue<Resource> resources = resourceAddition.readResourcesFromCSV("src/main/java/data/sample_data.csv");
    }
}