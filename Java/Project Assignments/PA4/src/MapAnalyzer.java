import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * This class reads map data, calculates fastest routes, connectivity, makes analysis,
 * and writes the results to an output file.
 * AUTHOR NOTE: Hocam i tried my best to implement the algorithm logic that you mentioned in the pdf,
 * I hope you can understand the algorithm clearly as it's a bit complicated in my implementation.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class MapAnalyzer {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        String[] content = FileInput.readFile(args[0], false, false); // Reads the file as it is without discarding or trimming anything and stores it in string array namely content.
        FileOutput.writeToFile(args[1], "", false, false); //For reinitializing the file, it is not necessary but good practice for making sure about there is no leftover.

        analyzeMap(content, args[1]);
   
    }

 
    /**
     * This method calculates the fastest route and analyzes the connectivity of a map
     * based on given content and writes the results to a specified path.
     * 
     * @param content An array consists of input lines.
     * @param path The path of the output file.
     */
    public static void analyzeMap(String[] content, String path){
        Point startingPoint = new Point(content[0].split("\t")[0]);
        Point endPoint = new Point(content[0].split("\t")[1]);
        // Create an symbolic startingConnection which will represent the first point. As you see, it doesn't have a road and its distance is 0.
        Connection startingConnection = new Connection(startingPoint, startingPoint, 0, null);
        
        // For calculation of the fastest route in the full map.
        ArrayList<Connection> connections = new ArrayList<Connection>(); // Arraylist to store roads
        Map<String, Connection> roadMap = new HashMap<String, Connection>(); // Hashmap to store Connection objects (which has distance values) according to points.
        roadMap.put(startingPoint.getName(), startingConnection); // Put starting point in the map as it will start the calculation.
        Connection.calculateFastestRoute(content, startingPoint, startingPoint, endPoint, connections, roadMap, path);
        // Write the output.
        String formattedStr = String.format("Fastest Route from %s to %s (%d KM):", startingPoint.getName(), endPoint.getName(), roadMap.get(endPoint.getName()).getDistance());
        FileOutput.writeToFile(path, formattedStr, true, true);
        writeFastestRoute(startingPoint, endPoint, roadMap, path);


        // For calculation of the barely connected map.
        ArrayList<Connection> connectionsBarely = new ArrayList<Connection>(); // Arraylist to store roads
        Map<String, Connection> barelyConnectedMap = new HashMap<String, Connection>(); // Hashmap to store Connection objects (which has distance values) according to points.
        barelyConnectedMap.put(startingPoint.getName(), startingConnection); // Put starting point in the map as it will start the calculation.
        Connection.calculateBarelyConnectedMap(content, startingPoint, connectionsBarely, barelyConnectedMap);
        // Write the output and get the road inputs.
        String[] barelyRoadInputs = writeBarely(startingPoint, endPoint, barelyConnectedMap, path);
        

        // For calculation of the fastest route in the barely connected map.
        ArrayList<Connection> connectionsNewRoute = new ArrayList<Connection>(); // Arraylist to store roads
        Map<String, Connection> barelyConnectedRouteMap = new HashMap<String, Connection>(); // Hashmap to store Connection objects (which has distance values) according to points.
        barelyConnectedRouteMap.put(startingPoint.getName(), startingConnection); // Put starting point in the map as it will start the calculation.
        Connection.calculateFastestRoute(barelyRoadInputs, startingPoint, startingPoint, endPoint, connectionsNewRoute, barelyConnectedRouteMap, path);
        // Write the output.
        formattedStr = String.format("Fastest Route from %s to %s on Barely Connected Map (%d KM):", startingPoint.getName(), endPoint.getName(), barelyConnectedRouteMap.get(endPoint.getName()).getDistance());
        FileOutput.writeToFile(path, formattedStr, true, true);
        writeFastestRoute(startingPoint, endPoint, barelyConnectedRouteMap, path);

        // Write the analysis.
        writeAnalysis(content, endPoint, roadMap, barelyConnectedMap, barelyConnectedRouteMap, path);
    }



   
   /**
    * This method writes the route of a route map (which is created early on) according to its startingPoint 
    * and endPoint.
    * 
    * @param startingPoint Starting point of the route.
    * @param endPoint End point of the route.
    * @param routeMap A map that created early on which has the fastest route to the points.
    * @param path The path of the output file.
    */
    public static void writeFastestRoute(Point startingPoint, Point endPoint, Map<String, Connection> routeMap, String path){
        ArrayList<Connection> route = Connection.getRoute(startingPoint, endPoint, routeMap);
        
        for (Connection currentCon : route){ // Write all of the roads.
            String formattedStr = String.format("%s\t%s\t%d\t%d", currentCon.getPoint1().getName(), currentCon.getPoint2().getName(), currentCon.getRoadLength(), currentCon.getRoadId());
            FileOutput.writeToFile(path, formattedStr, true, true);
        }
    }
    


    /**
     * This method writes the constructed roads on a barely connected map.
     * 
     * @param startingPoint Starting point of the route.
     * @param endPoint end point of the route.
     * @param barelyConnectedMap A map that contains the connections in the barely connected map.
     * @param path The path of the output file.
     * @return An array of strings containing information about the roads in the barely connected map,
     * formatted as "point1 point2 roadLength roadId".
     */
    public static String[] writeBarely(Point startingPoint, Point endPoint, Map<String, Connection> barelyConnectedMap, String path){
        barelyConnectedMap.remove(startingPoint.getName()); // Remove the symbolic starting connection.
        
        // Get objects from the map to sort them.
        ArrayList<Connection> sortedRoute = new ArrayList<Connection>();
        for (Map.Entry<String, Connection> entry : barelyConnectedMap.entrySet()){
            Connection connection = entry.getValue();
            sortedRoute.add(connection);
        }

        Road.sortRoads(sortedRoute); // Sort the connections according to their roads.

        FileOutput.writeToFile(path, "Roads of Barely Connected Map is:", true, true);
        String formattedStr;
        String[] barelyRoadInputs = new String[sortedRoute.size()+1]; // +1 is for the first line that won't be considered in the method.
        barelyRoadInputs[0] = " "; // This line won't be calculated in the method as in the method it starts iterating from index 1.
        for (int i = 0; i < sortedRoute.size(); i++){ // Write all of the constructed roads of the barely connected map.
            Connection currentObj = sortedRoute.get(i);
            formattedStr = String.format("%s\t%s\t%d\t%d", currentObj.getPoint1().getName(), currentObj.getPoint2().getName(), currentObj.getRoadLength(), currentObj.getRoadId());
            barelyRoadInputs[i+1] = formattedStr; // Store the roads because they will be used in the calculation of the fastest route.
            FileOutput.writeToFile(path, formattedStr, true, true);
        }
        
        return barelyRoadInputs;
    }



    /**
     * This method calculates and writes the ratio of construction material usage and
     * fastest route length between a original map and a barely connected map.
     * 
     * @param content An array consists of input lines.
     * @param endPoint end point of the route.
     * @param roadMap A map which stores the routes to the points in the normal map.
     * @param barelyConnectedMap A map which stores the connections in the barely connected map.
     * @param barelyConnectedRouteMap A map which stores the routes to the points in the barely connected map.
     * @param path The path of the output file.
     */
    public static void writeAnalysis(String[] content, Point endPoint, Map<String, Connection> roadMap, Map<String, Connection> barelyConnectedMap, Map<String, Connection> barelyConnectedRouteMap, String path){
        String formattedStr = "Analysis:";
        FileOutput.writeToFile(path, formattedStr, true, true);
        
        // Calculate total road length for each map and write the ratio.
        double totalMaterial = 0;
        for (int i = 1; i < content.length; i++){
            double roadLength = Double.parseDouble(content[i].split("\t")[2]);
            totalMaterial += roadLength;
        }

        double barelyMaterial = 0;
        for (Map.Entry<String, Connection> entry : barelyConnectedMap.entrySet()){
            Connection connection = entry.getValue();
            barelyMaterial += (double) connection.getRoadLength();
        }

        formattedStr = String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f", barelyMaterial/totalMaterial);
        FileOutput.writeToFile(path, formattedStr, true, true);

        // Calculate fastest route length for each map and write the ratio.
        double normalFastest = roadMap.get(endPoint.getName()).getDistance();
        double barelyFastest = barelyConnectedRouteMap.get(endPoint.getName()).getDistance();
        formattedStr = String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", barelyFastest/normalFastest);
        FileOutput.writeToFile(path, formattedStr, true, false);
    }
}
