import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class ResourceSearchMain {

    public static Map<String, Hexagon> hexagon_map;
    Utilities helper = new Utilities();
    static Date simStartTime = new Date(2016,4,8,8,00,00);
    static Date simEndTime = new Date(2016,4,8,9,00,00);
    static String resource_stream = "/Users/bhargavarisetty/Downloads/ResourceSearch/src/main/java/data/resource_stream.csv";
    static String demand_data = "/Users/bhargavarisetty/Downloads/ResourceSearch/src/main/java/data/demand_data.csv";
    static int noOfCabs = 1000;
    static int clock_update_frequency = 30000;
    static int resource_mlt = 600000;



    public static void main(String[] args) throws ParseException, IOException {
        Date startTime = new Date();
        Date endTime = new Date();
        //long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        Calendar cal = Calendar.getInstance();

        cal.set(simStartTime.getYear(), simStartTime.getMonth(), simStartTime.getDate(), simStartTime.getHours(), simStartTime.getMinutes(), simStartTime.getSeconds());
        startTime = cal.getTime();
        cal.set(simEndTime.getYear(), simEndTime.getMonth(), simEndTime.getDate(), simEndTime.getHours(), simEndTime.getMinutes(), simEndTime.getSeconds());
        endTime = cal.getTime();
        // obtain Resource list - given CSV of rides between input startTime and endTime,
        ResourceAddition resourceAddition = new ResourceAddition();
        Queue<Resource> resource_pool = resourceAddition.readResourcesFromCSV(resource_stream);
        PreProcess loadHexagonData = new PreProcess();
        hexagon_map = loadHexagonData.readHexagonsFromCSV(demand_data);

        //System.out.println("hex map size" + hexagon_map.size());
        // Initialize cabs & obtain list of cabs
        CabCreation cabCreation = new CabCreation(noOfCabs, startTime);

        List<Cab> cabList = cabCreation.createCabs(hexagon_map);
        Result result = new Result(clock_update_frequency, startTime);
        Queue<Resource> pending_resources = new LinkedList<>();
        ;
        System.out.println(result.curr_time + " " + endTime);

        while (result.curr_time.compareTo(endTime) < 0) {

            Simulation simulator = new Simulation();
            simulator.simulate(cabList, result);

            //System.out.println(resource_pool.peek().pickup_time + " " + result.curr_time);
            while (!resource_pool.isEmpty() && resource_pool.peek().pickup_time.getHours() == (result.curr_time.getHours()) && resource_pool.peek().pickup_time.getMinutes() == (result.curr_time.getMinutes()) && resource_pool.peek().pickup_time.getSeconds() >= (result.curr_time.getSeconds()) && resource_pool.peek().pickup_time.getSeconds() < (result.curr_time.getSeconds() + result.global_time/1000)) {
                pending_resources.add(resource_pool.poll());
            }

            System.out.println("Number of customers currently being served at " + result.curr_time + " are " + pending_resources.size());

            pending_resources = Assignment.allocate(pending_resources, cabList, resource_mlt, result);
            result.curr_time = new Date(result.curr_time.getTime() + (int) result.global_time);
        }

        System.out.println();
        System.out.println("-------------------------End Of Simulation----------------------");
        System.out.println("Average Search Time: " + result.search_time/result.assigned_resources);
        System.out.println("Resource Expiration Percentage: " + ((float)result.total_dropped/(float)(result.assigned_resources + result.total_dropped))*100);
        System.out.println("Average wait time for each resource: " + ((float)result.total_waiting/(float)(result.assigned_resources + result.total_dropped + pending_resources.size())));

    }

}