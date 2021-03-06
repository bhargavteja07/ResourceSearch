import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourceStream {

    Queue<Resource> streamResources(String fileName) {

        Queue<Resource> resources = new LinkedList<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            br.readLine();
            String line = br.readLine();

            while (line != null) {

                line = line.replace("\"", "");
                String[] attributes = line.split(",");

                Resource resource = getResourceObject(attributes);
                resources.add(resource);

                line = br.readLine();

            }
        } catch (IOException e) {

        } catch (ParseException e) {

        }
        return resources;
    }


    static Resource getResourceObject(String[] metadata) throws ParseException {

        GeoCoordinate pickup_geoCoordinate = new GeoCoordinate();
        pickup_geoCoordinate.latitude = Double.parseDouble(metadata[16]);
        pickup_geoCoordinate.longitude = Double.parseDouble(metadata[15]);

        GeoCoordinate drop_geoCoordinate = new GeoCoordinate();
        drop_geoCoordinate.latitude = Double.parseDouble(metadata[18]);
        drop_geoCoordinate.longitude = Double.parseDouble(metadata[17]);

        Date pickup_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(metadata[1]);
        Date drop_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(metadata[2]);

        String hexagon_id = metadata[19];
        return new Resource(pickup_geoCoordinate, pickup_time, drop_geoCoordinate, drop_time, hexagon_id);
    }
}
