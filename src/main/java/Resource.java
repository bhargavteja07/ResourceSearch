import java.util.Date;

public class Resource {

    Location pickup_location;
    Location drop_location;
    Date pickup_time;
    Date drop_time;
    String hexagon_id;
    double wait_time;
    boolean status;

    Resource(Location pickup_location, Date pickup_time,
             Location drop_location, Date drop_time,
             String hexagon_id){
        this.pickup_location = pickup_location;
        this.drop_location = drop_location;
        this.pickup_time = pickup_time;
        this.drop_time = drop_time;
        this.hexagon_id = hexagon_id;
        this.status = true;
        this.wait_time=0;
    }
}
