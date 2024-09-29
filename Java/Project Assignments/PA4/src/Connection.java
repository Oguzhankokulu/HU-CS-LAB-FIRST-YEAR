import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


/**
 * This class represents connections between points with distance to the starting point and road
 * information, providing methods for calculating the fastest route and creating a barely connected
 * map.
 * 
 * @author Oguzhan
 * @version 1.0
 */
public class Connection {
    private Point point1, point2;
    private Road road;
    private int distance;

    // Constructor.
    public Connection(Point point1, Point point2, int distance, Road road){
        this.point1 = point1;
        this.point2 = point2;
        this.distance = distance;
        this.road = road;
    }

    // Setters and getters.
    public Point getPoint1() {
        return this.point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return this.point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRoadLength() {
        return this.road.getLength();
    }

    public void setRoadLength(int length) {
        this.road.setLength(length);
    }

    public int getRoadId() {
        return this.road.getId();
    }

    public void setRoadId(int id) {
        this.road.setId(id);
    }



   /**
    * This method recursively finds the fastest route between a starting point and an end point using a map of connections and a list of roads.
    * Its algorithm logic is: It first gets the neighbors of the points and then it sorts them according to their distance to start point.
    * Then it checkes the shortest distance connection object to see if it was calculated before and if it's valid it adds the object to the map.
    * Then it calls itself with the new point.
    * 
    * @param content An array consists of input lines.
    * @param startingPoint Starting point of the route.
    * @param endPoint End point of the route.
    * @param connections An arraylist to store connections (also representing roads).
    * @param roadMap A map to store point name and equivalent object (also stores distance from the starting point).
    * @param path The path of the output file.
    */
    public static void calculateFastestRoute(String[] content, Point startingPoint, Point staticStart, Point endPoint, ArrayList<Connection> connections, Map<String, Connection> roadMap, String path){
        if (roadMap.containsKey(endPoint.getName())){ // Base case if fastest route to end point is found then return.
            return;
        }

        // Add neighbor connections to connections arraylist.
        connections.addAll(findConnections(content, startingPoint, roadMap));
        sortConnections(connections, staticStart, endPoint, roadMap);

        // There is while loop because if a road isn't necessary (which means both endpoints' route have been calculated)
        // then remove that road and move to the next closest connection to continue the calculation.
        // If there is a new connection that has ben added to roadMap then isNext will return to false and function will call itself
        // with this way new connections will be explored from new endpoints and if a road isn't necessary, the attempt to find new routes will be continued.
        boolean isNext = false;
        while (!isNext && connections.size() != 0) {
            Connection closest = connections.get(0);
            // Do not append it to map if it is in the map or it does not have an end point in the map.
            if (roadMap.containsKey(closest.getPoint1().getName())){ // To decide which point is the next point to be calculated.
                if (roadMap.containsKey(closest.getPoint1().getName()) ^ roadMap.containsKey(closest.getPoint2().getName())){
                    roadMap.put(closest.getPoint2().getName(), closest); // Put it in the map so fastest route will be calculated to that point.
                    connections.remove(closest); // Remove from connections to avoid recalculation.
                    isNext = true; // To end the while loop because the next point is found.
                    // Call the method again with the new point.
                    calculateFastestRoute(content, closest.getPoint2(), staticStart, endPoint, connections, roadMap, path);
                }
            } else if (roadMap.containsKey(closest.getPoint2().getName())){
                if (roadMap.containsKey(closest.getPoint1().getName()) ^ roadMap.containsKey(closest.getPoint2().getName())){
                roadMap.put(closest.getPoint1().getName(), closest); // Put it in the map so fastest route will be calculated to that point.
                connections.remove(closest); // Remove from connections to avoid recalculation.
                isNext = true; // To end the while loop because the next point is found.
                // Call the method again with the new point.
                calculateFastestRoute(content, closest.getPoint1(), staticStart, endPoint, connections, roadMap, path);
                }
            } connections.remove(closest); // If if statements don't evaluate remove from connections to avoid recalculation.
        }
    }



    /**
     * This method iteratively adds connections to a map until all
     * points are connected in a barely connected manner.
     * 
     * @param content An array consists of input lines.
     * @param startingPoint Starting point of the route.
     * @param connections An arraylist to store connections (also representing roads).
     * @param barelyConnectedMap A map to store point name and equivalent object (also stores distance from the starting point).
     */
    public static void calculateBarelyConnectedMap(String[] content, Point startingPoint, ArrayList<Connection> connections, Map<String, Connection> barelyConnectedMap){
        // Create a points list to check its size.
        ArrayList<String> points = new ArrayList<>();
        for (int i = 1; i < content.length; i++){
            String point1 = content[i].split("\t")[0];
            String point2 = content[i].split("\t")[1];
            if (!points.contains(point1)){
                points.add(point1);
            }
            if (!points.contains(point2)){
                points.add(point2);
            }
        }
        
        // Add neighbor connections to connections arraylist to start the calculation.
        connections.addAll(findConnections(content, startingPoint, barelyConnectedMap));
        // As stated in the pdf, number of roads that are supposed to be added must be exactly one less than the number of points.
        // There isn't minus 1 in the statement because barelyConnectedMap contains one extra startingConnection which isn't representing a real connection.
        // It just represents a point to start with.
        while ((points.size()) > barelyConnectedMap.size()){
            // Sort roads.
            Road.sortRoads(connections);

            Connection smallest = connections.get(0);
            if (barelyConnectedMap.containsKey(smallest.getPoint1().getName())){ // To decide which point is the next point to be calculated. If map contains point1 then point2 should be the new point.
                // If one of the end point is on the map(not both), if it's then add the new road and continue with the new neighbors.
                if (barelyConnectedMap.containsKey(smallest.getPoint1().getName()) ^ barelyConnectedMap.containsKey(smallest.getPoint2().getName())){
                    barelyConnectedMap.put(smallest.getPoint2().getName(), smallest); // Put it in the map so the road to that point will be created in the map.
                    connections.remove(smallest); // Remove from connections to avoid recalculation.
                    connections.addAll(findConnections(content, smallest.getPoint2(), barelyConnectedMap)); // Add new neighbors according to our next point.
                } else { // If the road doesn't connect new point to the current map, remove it.
                    connections.remove(smallest);
                }
            } else if (barelyConnectedMap.containsKey(smallest.getPoint2().getName())){
                // If one of the end point is on the map(not both), if it's then add the new road and continue with the new neighbors.
                if (barelyConnectedMap.containsKey(smallest.getPoint1().getName()) ^ barelyConnectedMap.containsKey(smallest.getPoint2().getName())){
                    barelyConnectedMap.put(smallest.getPoint1().getName(), smallest); // Put it in the map so the road to that point will be created in the map.
                    connections.remove(smallest); // Remove from connections to avoid recalculation.
                    connections.addAll(findConnections(content, smallest.getPoint1(), barelyConnectedMap)); // Add new neighbors according to our next point.
                } else { // If the road doesn't connect new point to the current map, remove it.
                    connections.remove(smallest);
                }
            }
        }
        
    }



    /**
     * This function takes in an array of road data, a starting point, and a map of
     * known connections, and returns a list of neighboring connections based on the starting point.
     * 
     * @param content An array consists of input lines.
     * @param startingPoint The point whose neighbors will be checked.
     * @param roadMap A map which has the previously created connections.
     * @return An arraylist of Connection objects which represents the new neighbors to startingPoint.
     */
    public static ArrayList<Connection> findConnections(String[] content, Point startingPoint, Map<String, Connection> roadMap){
        ArrayList<Connection> neighbors = new ArrayList<>();
        for (int i = 1; i < content.length; i++){ // To disclude the first line in the content.
            String point1Str = content[i].split("\t")[0]; // First point string of the road.
            String point2Str = content[i].split("\t")[1]; // Second point string of the road.

            // If starting point (which is the parameter of the called method) is one of the end of the current road.
            if (startingPoint.getName().equals(point1Str) || startingPoint.getName().equals(point2Str)){
                // Initialize fields for the object.
                Point point1 = new Point(point1Str); 
                Point point2 = new Point(point2Str);
                int roadLength = Integer.parseInt(content[i].split("\t")[2]);
                int roadId = Integer.parseInt(content[i].split("\t")[3]);
                int distance;
                Road road = new Road(roadLength, roadId); // Make Road object to construct the Connection object

                // If one of the end points is calculated before in the map, but don't evaluate if both end are present.
                if (roadMap.containsKey(point1Str) ^ roadMap.containsKey(point2Str)){
                    if (roadMap.containsKey(point1Str)){ // If the first point is known, then take it's distance
                        distance = roadMap.get(point1Str).getDistance() + roadLength;
                    } else { // If the second point is known
                        distance = roadMap.get(point2Str).getDistance() + roadLength;
                    }
                    // Add new Connection object to connections (neighbors of the point) to calculate later.
                    neighbors.add(new Connection(point1, point2, distance, road));
                }
                
            }
        }
        return neighbors;
    }



    /**
     * This method sort connections according to its elements' distance first, if equal and one of the connections leads to the endpoint
     * then sort according to smallest road first and if connections don't lead to the endpoint then sort according to the ID.
     * 
     * @param connections ArrayList which will be sorted.
     * @param staticStart Point object which represents the starting point of the input.
     * @param endPoint Point object which represents the end point of the input.
     * @param roadMap An ArrayList of Connection objects that represent the fastest route
     * from a starting point to an end point on a map. The route is determined by following connections
     * in the routeMap starting from the end point and moving towards the starting point.
     */
    public static void sortConnections(ArrayList<Connection> connections, Point staticStart, Point endPoint, Map<String,Connection> roadMap){
        Collections.sort(connections, (c1, c2) -> {
            int distanceComparision = c1.getDistance() - c2.getDistance();
            if (distanceComparision != 0) { // If distance to the starting point different, return small one first.
                return distanceComparision;
            } else if ((c1.getPoint1().getName().equals(endPoint.getName()) || c1.getPoint2().getName().equals(endPoint.getName())) && (c2.getPoint1().getName().equals(endPoint.getName()) || c2.getPoint2().getName().equals(endPoint.getName()))){
                // If 2 points have same distance to the starting point and connect to endpoint.
                ArrayList<Connection> c1Route = new ArrayList<>();
                ArrayList<Connection> c2Route = new ArrayList<>();
                // Get routes of that 2 points.
                c1Route.addAll( getRoute(staticStart, Point.findPoind(c1, endPoint), roadMap));
                c2Route.addAll( getRoute(staticStart, Point.findPoind(c2, endPoint), roadMap));
                // Total id in case roads are the same but differ in id summation.
                while (true){
                    int startingRoadLength = c1Route.get(0).getRoadLength() - c2Route.get(0).getRoadLength();
                    if (startingRoadLength != 0){ // If first road of the route is different in length, then get the short one first.
                        return startingRoadLength;
                    } else { // If first road is the same in both points then get roads' id and move to the next road in the routes to check.
                        c1Route.remove(0);
                        c2Route.remove(0);
                    }
                    if (c1Route.size() == 0 || c2Route.size() == 0){ // If route is empty than it means routes are the same in the terms of road lengths.
                        break;
                    }
                }
                // If the routes are the same in the terms of road lengths,
                // then sort according to first road's id in the routes.
                c1Route.addAll( getRoute(staticStart, Point.findPoind(c1, endPoint), roadMap));
                c2Route.addAll( getRoute(staticStart, Point.findPoind(c2, endPoint), roadMap));
                return c1Route.get(0).getRoadId() - c2Route.get(0).getRoadId();
            } else { // If distances are equal but points aren't connected to endpoint then sort roads according to it's id.
                return c1.getRoadId() - c2.getRoadId();
            }
        });
    }


    
    /**
     * This method retrieves the fastest route between a starting point and an end point by
     * traversing through a map of connections.
     * 
     * @param startingPoint Point represents the point from which the route begins.
     * @param endPoint Point represents the destination point where the route ends.
     * @param routeMap A map `Map<String, Connection>` that stores connections
     * between different points on a map. Each connection object in the map represents a road or path
     * between two points. The keys in the map are the names of the points, and the values are the
     * corresponding connection objects.
     * @return An ArrayList of Connection objects that represent the fastest route
     * from a starting point to an end point on a map. The route is determined by following connections
     * in the routeMap starting from the end point and moving towards the starting point.
     */
    public static ArrayList<Connection> getRoute(Point startingPoint, Point endPoint, Map<String, Connection> routeMap){
        ArrayList<Connection> route = new ArrayList<>(); // To store connection objects. These objects create the fastest route.
        // Start from endPoint object and go previous points in the loop.
        Connection tempObject = routeMap.get(endPoint.getName());
        route.add(tempObject);
        String lastPoint = endPoint.getName(); // Last point which will be checked.
    
        // While not reached to startingConnection (Which is a symbolic connection, its both points is the starting point).
        while (!tempObject.getPoint1().getName().equals(startingPoint.getName()) && !tempObject.getPoint2().getName().equals(startingPoint.getName())){
            if (tempObject.getPoint1().getName().equals(lastPoint)){ // If point1 is the lastpoint then point2 is the previous point (which will be checked).
                lastPoint = tempObject.getPoint2().getName();
                tempObject = routeMap.get(lastPoint);
                route.add(tempObject);
            } else {
                lastPoint = tempObject.getPoint1().getName();
                tempObject = routeMap.get(lastPoint);
                route.add(tempObject);
            }
        }
        Collections.reverse(route); // To get first road in the first line of the output.
        return route;
    }
}
