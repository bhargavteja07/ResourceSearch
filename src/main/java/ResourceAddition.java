import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourceAddition {

    Queue<Resource> readResourcesFromCSV(String fileName) {

        Queue<Resource> resources = new LinkedList<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            br.readLine();
            String line = br.readLine();

            while (line != null) {

                line = line.replace("\"", "");
                String[] attributes = line.split(",");

                Resource resource = createResource(attributes);
                resources.add(resource);

                line = br.readLine();

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resources;
    }


    static Resource createResource(String[] metadata) throws ParseException {

        Location pickup_location = new Location();
        pickup_location.latitude = Double.parseDouble(metadata[16]);
        pickup_location.longitude = Double.parseDouble(metadata[15]);

        Location drop_location = new Location();
        drop_location.latitude = Double.parseDouble(metadata[18]);
        drop_location.longitude = Double.parseDouble(metadata[17]);

        Date pickup_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(metadata[1]);
        Date drop_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(metadata[2]);

        String hexagon_id = metadata[19];
        return new Resource(pickup_location, pickup_time, drop_location, drop_time, hexagon_id);
    }
}
