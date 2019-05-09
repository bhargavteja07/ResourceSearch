import java.text.ParseException;
import java.util.*;

public class ResourceSearchMain {

    public static Map<String, Hexagon> hexagon_map;
    public static RSGenerics util = new RSGenerics();

    /*   To be Configured   */
    static Date simStartTime = new Date(2016,4,8,8,00,00);
    static Date simEndTime = new Date(2016,4,8,9,00,00);
    static String resource_stream = "/src/main/java/data/resource_stream.csv";
    static int noOfCabs = 1000;


    /*
    Not to be changed
     */
    static double clock_update_frequency = 0.5;
    static double resource_mlt = 10.0;
    static double ONE_MINUTE_IN_MILLIS = 60000.0; //millisecs
    static String demand_data = "/src/main/java/data/demand_data.csv";


    public static void main(String[] args) throws ParseException {

        Date startTime, endTime;
        Calendar cal = Calendar.getInstance();

        cal.set(simStartTime.getYear(), simStartTime.getMonth(), simStartTime.getDate(), simStartTime.getHours(), simStartTime.getMinutes(), simStartTime.getSeconds());
        startTime = cal.getTime();
        cal.set(simEndTime.getYear(), simEndTime.getMonth(), simEndTime.getDate(), simEndTime.getHours(), simEndTime.getMinutes(), simEndTime.getSeconds());
        endTime = cal.getTime();

        // obtain Resource list - given CSV of rides between input startTime and endTime,
        ResourceStream resourceStream = new ResourceStream();
        Queue<Resource> resource_pool = resourceStream.streamResources(resource_stream);
        BootConfig loadHexagonData = new BootConfig();
        hexagon_map = loadHexagonData.readHexagonsFromCSV(demand_data);
        List<Cab> cabList = initialiseCabsOnH3(hexagon_map);
        Result result = new Result(clock_update_frequency, startTime);
        Queue<Resource> pending_resources = new LinkedList<>();

        while (result.curr_time.compareTo(endTime) < 0) {
            while (!resource_pool.isEmpty() && resource_pool.peek().pickup_time.getHours() == (result.curr_time.getHours()) && resource_pool.peek().pickup_time.getMinutes() == (result.curr_time.getMinutes()) && resource_pool.peek().pickup_time.getSeconds() >= (result.curr_time.getSeconds()) && resource_pool.peek().pickup_time.getSeconds() < (result.curr_time.getSeconds() + result.global_time*60)) {
                pending_resources.add(resource_pool.poll());
            }
            System.out.println("Number of customers currently being served at " + result.curr_time + " are " + pending_resources.size());
            pending_resources = Assignment.allocate(pending_resources, cabList, resource_mlt, result);

            Simulation simulator = new Simulation();
            simulator.startSimulation(cabList, result);

            result.curr_time = new Date(result.curr_time.getTime() + (int) (result.global_time * ONE_MINUTE_IN_MILLIS));
        }

        System.out.println();
        System.out.println("-------------------------End Of Simulation----------------------");
        System.out.println("Average Search Time: " + result.search_time / result.assigned_resources);
        System.out.println("Resource Expiration Percentage: " + ((float) result.total_dropped / (float) (result.assigned_resources + result.total_dropped)) * 100);
        System.out.println("Average wait time for each resource: " + ((float) result.wait_time / (float) (result.assigned_resources + result.total_dropped)));
        System.out.println("Total Dropped Resources: " + result.total_dropped);
        System.out.println("Total Assigned Resources: " + (result.assigned_resources));
    }

    public static List<Cab> initialiseCabsOnH3(Map<String, Hexagon> hexagon_map) {

        List<Cab> cabList = new ArrayList<>();
        int count = 1, i;
        List<String> hexagon_list = new ArrayList<String>(hexagon_map.keySet());
        Cab cab;

        while (count < noOfCabs) {

            i = (count - 1) % hexagon_map.size();
            String hexagon_id = hexagon_list.get(i);
            GeoCoordinate current = hexagon_map.get(hexagon_id).center;
            String current_time = util.getTimeRangeKey(new Date());
            Hexagon current_hexagon = hexagon_map.get(hexagon_id);
            cab = new Cab(count - 1, hexagon_id, current_time, current);
            cabList.add(cab);
            current_hexagon.cabList.add(cab);
            count++;
        }

        return cabList;
    }

}