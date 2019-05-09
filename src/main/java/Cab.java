public class Cab {

    GeoCoordinate current;
    String current_hexagon_id;
    String current_time;
    int status;
    int id = -1;
    String destination_hex;
    double destination_distance;
    double current_travel_time;
    double total_waiting_time;
    double journeytime;


    Cab(int id,String current_hexagon_id, String current_time, GeoCoordinate current) {
        this.current_hexagon_id = current_hexagon_id;
        this.current_time = current_time;
        this.current = current;
        this.status = 0;
        this.id = id;
        this.current = current;
        this.current_travel_time = 0;
        this.destination_distance = 0;
        this.total_waiting_time = 0; //
        this.journeytime = 0;  // cab to resource + resource to drop off
    }

    void setDestination(String hex_id, double distance, double travel_time, int status) {
        this.destination_hex = hex_id;
        this.destination_distance = distance;
        this.current_travel_time = travel_time;
        this.status = status;
    }
}
