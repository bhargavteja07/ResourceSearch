import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hexagon {

    List<List<String>> neighborList;
    GeoCoordinate center = new GeoCoordinate();
    Map<String, Integer> probabilities;

    double probability;
    int day_of_week;
    List<Cab> cabList;

    Hexagon(int day_of_week, List<List<String>> neighborList, double latitude, double longitude) {
        this.day_of_week = day_of_week;
        this.neighborList = neighborList;
        this.center.latitude = latitude;
        this.center.longitude = longitude;
        this.cabList = new ArrayList<>();
    }

    public Map<String, Integer> getTimestamps() {
        return probabilities;
    }

    public void setTimestamps(Map<String, Integer> timestamps)  {
        this.probabilities = timestamps;
    }

    public void addCab(Cab cab){
        cabList.add(cab);
    }

}
