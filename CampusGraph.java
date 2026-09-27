import java.util.*;

/**
 * Campus map as a graph (adjacency list). Owner: Aakil
 * Menu options: 10, 11, 12, 13, 14, 15
 *
 * Vertices = locations (Library, Cafeteria ...), edges = roads between them.
 * Roads work both ways (undirected), so a connection A-B is stored in A's list AND B's list.
 */
public class CampusGraph {

    // location name -> list of neighbouring locations (LinkedHashMap keeps insertion order)
    private Map<String, List<String>> adjList = new LinkedHashMap<>();

    /** Add a location. Returns false if it already exists. */
    public boolean addLocation(String name) {
        if (adjList.containsKey(name)) {
            return false;
        }
        adjList.put(name, new ArrayList<>());
        return true;
    }

    /** Remove a location AND every connection that points to it. */
    public boolean removeLocation(String name) {
        if (!adjList.containsKey(name)) {
            return false;
        }
        adjList.remove(name);                       // remove the location itself
        for (List<String> neighbours : adjList.values()) {
            neighbours.remove(name);                // remove it from every other list
        }
        return true;
    }

    /** Connect two locations both ways. False if missing, same, or already connected. */
    public boolean addConnection(String a, String b) {
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) return false;
        if (a.equals(b)) return false;
        if (adjList.get(a).contains(b)) return false;   // already connected
        adjList.get(a).add(b);
        adjList.get(b).add(a);
        return true;
    }

    /** Remove the road between two locations. False if it does not exist. */
    public boolean removeConnection(String a, String b) {
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) return false;
        if (!adjList.get(a).contains(b)) return false;
        adjList.get(a).remove(b);
        adjList.get(b).remove(a);
        return true;
    }

    /** Print each location with its neighbours. */
    public void displayConnections() {
        if (adjList.isEmpty()) {
            System.out.println("No campus locations added yet.");
            return;
        }
        System.out.println("--- Campus Network ---");
        for (Map.Entry<String, List<String>> entry : adjList.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    /**
     * Breadth-first traversal: visit the start, then all its neighbours,
     * then their neighbours, and so on (uses a queue and a visited set).
     */
    public void bfs(String start) {
        if (!adjList.containsKey(start)) {
            System.out.println("Location not found.");
            return;
        }
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> order = new ArrayList<>();

        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            String current = queue.remove();            // take from the front
            order.add(current);
            for (String neighbour : adjList.get(current)) {
                if (!visited.contains(neighbour)) {     // only visit each location once
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        System.out.println("BFS from " + start + ": " + String.join(" -> ", order));
    }

    public boolean hasLocation(String name) {
        return adjList.containsKey(name);
    }
}
