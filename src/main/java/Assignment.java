import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Assignment {

    public static Queue<Resource> allocate(Queue<Resource> resources, List<Cab> cabList, double mlt, Result result) {

        int res_count = resources.size();
        int count = 0;


        while (count < res_count) {

            double min_time = Integer.MAX_VALUE;
            Resource r = resources.poll();
            int cab_id = -1;
            count++;

            if (r.wait_time >= mlt) {
                result.total_dropped += 1;
                result.total_waiting -= 1;
                continue; // resource dropped from queue
            }

            double src_lat = r.pickup_geoCoordinate.latitude;
            double src_long = r.pickup_geoCoordinate.longitude;
            List<List<String>> neighboursByLevel;

            if (ResourceSearchMain.hexagon_map.get(r.hexagon_id) != null) {
                neighboursByLevel = ResourceSearchMain.hexagon_map.get(r.hexagon_id).neighborList;
            } else {
                continue;
            }

            List<Cab> neighbourCabList = new ArrayList<>();
            neighbourCabList.addAll(ResourceSearchMain.hexagon_map.get(r.hexagon_id).cabList);

            for (List<String> levelList : neighboursByLevel) {
                for (String neighbour : levelList) {
                    if (ResourceSearchMain.hexagon_map.containsKey(neighbour))
                        neighbourCabList.addAll(ResourceSearchMain.hexagon_map.get(neighbour).cabList);
                    else {
                        continue;
                    }
                }
            }


            for (int j = 0; j < neighbourCabList.size(); j++) {

                Cab current = neighbourCabList.get(j);
                if (neighbourCabList.get(j).status != 1) {

                    double dest_lat = ResourceSearchMain.hexagon_map.get(current.current_hexagon_id).center.latitude;
                    double dest_long = ResourceSearchMain.hexagon_map.get(current.current_hexagon_id).center.longitude;

                    if (src_lat != 0.0 && src_long != 0.0 && dest_lat != 0.0 && dest_long != 0.0) {

                        double curr_cab_time = GraphHopperWrapper.time(src_lat, src_long, dest_lat, dest_long) / ResourceSearchMain.ONE_MINUTE_IN_MILLIS;

                        // finding closest cab
                        if (curr_cab_time < min_time) {
                            min_time = curr_cab_time;
                            cab_id = j;
                        } else {

                        }
                    }
                }
            }

            // if cab is available and can reach before (mlt - already waited time)
            if (cab_id != -1 && (min_time + r.wait_time) <= mlt
                    && r.pickup_geoCoordinate.latitude != 0.0 && r.pickup_geoCoordinate.longitude != 0.0
                    && r.drop_geoCoordinate.latitude != 0 && r.drop_geoCoordinate.longitude != 0.0) {

                Cab assignedCab = neighbourCabList.get(cab_id);
//                System.out.println("cab: " + assignedCab.id + " status: " + assignedCab.status + " travel time: " + assignedCab.journeytime);

                Cab original = null;

//                for (Cab c : ResourceSearchMain.hexagon_map.get(assignedCab.current_hexagon_id).cabList) {
//                    if (c.id == assignedCab.id) {
//                        original = c;
//                        System.out.println("cab: " + c.id + " status: " + c.status + " travel time: " + c.journeytime);
//                    }
//                }
                // status 1 indicates successful assignment
                assignedCab.status = 1;

                //System.out.println("Cab allocated:" + cab_id + " for resource at: " + r.hexagon_id);

//                result.search_time += assignedCab.total_waiting_time;
//                System.out.println(assignedCab.total_waiting_time);
//                assignedCab.total_waiting_time = 0;
                result.assigned_resources += 1;

                double time = GraphHopperWrapper.time(r.pickup_geoCoordinate.latitude, r.pickup_geoCoordinate.longitude, r.drop_geoCoordinate.latitude, r.drop_geoCoordinate.longitude) / ResourceSearchMain.ONE_MINUTE_IN_MILLIS;
                assignedCab.journeytime = (time + min_time);

                try {
                    assignedCab.destination_hex = RSGenerics.getHexFromGeo(r.drop_geoCoordinate.latitude, r.drop_geoCoordinate.longitude);
                } catch (IOException e) {
                    //System.out
                }

//                System.out.println("cab: " + assignedCab.id + " status: " + assignedCab.status + " travel time: " + assignedCab.journeytime);
//                System.out.println("Changed cab: " + original.id + " status: " + original.status + " travel time: " + original.journeytime);



                if (r.wait_time > 0) {
                    result.total_waiting -= 1;
                }

            } else {

                resources.add(r);
                if (r.wait_time == 0) result.total_waiting += 1;
                r.wait_time += result.global_time;
                result.wait_time += result.global_time;
                r.status = false;
//                System.out.println("wait time: " + min_time);
            }
        }

//        System.out.println("Average Search Time: " + result.search_time / result.assigned_resources);
//        System.out.println("Resource Expiration Percentage: " + ((float) result.total_dropped / (float) (result.assigned_resources + result.total_dropped)) * 100);
//        System.out.println("Average wait time for each resource: " + ((float) result.wait_time / (float) (result.assigned_resources + result.total_dropped)));
        System.out.println("Total Dropped Resources: " + result.total_dropped);
        System.out.println("Total Assigned Resources: " + (result.assigned_resources));

        System.out.println();
        return resources;
    }


}