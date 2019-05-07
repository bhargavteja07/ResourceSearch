# ResourceSearch

## Documentation: 
https://docs.google.com/document/d/1W9cIP9UzWSZRNMikePaQfSh763PG7DPmO4hwE1fEdkM/edit

Project Setup:
Instructions to setup Grpahhopper in the local machine:
1. open the terminal and run the following commands:
        -"wget https://graphhopper.com/public/releases/graphhopper-web-0.11.0.jar"
        -"wget https://raw.githubusercontent.com/graphhopper/graphhopper/0.11/config-example.yml"
        -"wget https://download.geofabrik.de/north-america/us/new-york-latest.osm.pbf"
The above commands will install necessary dependencies for graphhopper and new-york city map data
2. Run the below command to start the graphhopper server:
"java -Xms1g -Xmx1g -Dgraphhopper.datareader.file=new-york-latest.osm.pbf -Dgraphhopper.graph.location=graph-cache -Dgraphhopper.graph.flag_encoders=car -jar graphhopper-web-0.11.0.jar server config-example.yml"
3. Once the graphhopper is up and running you can execute the main java class
4. Provide the simulation starttime, endtime, Number of Cabs 
5. you can also provide the file path for resource stream but it is set with a default fle path which is located inside the project folder.
6. Execute the java class "ResourceSearchMain.java" from the terminal.
7. The output is printed on the terminal once the simulation ends.

Result set:
Average Search Time
Resource Expiration Percentage
Average Wait time of each resource

