import java.util.Date;

public class Resource {

    GeoCoordinate pickup_geoCoordinate;
    GeoCoordinate drop_geoCoordinate;
    Date pickup_time;
    Date drop_time;
    String hexagon_id;
    double wait_time;
    boolean status;

    Resource(GeoCoordinate pickup_geoCoordinate, Date pickup_time,
             GeoCoordinate drop_geoCoordinate, Date drop_time,
             String hexagon_id){
        this.pickup_geoCoordinate = pickup_geoCoordinate;
        this.drop_geoCoordinate = drop_geoCoordinate;
        this.pickup_time = pickup_time;
        this.drop_time = drop_time;
        this.hexagon_id = hexagon_id;
        this.status = true;
        this.wait_time=0;
    }
}
