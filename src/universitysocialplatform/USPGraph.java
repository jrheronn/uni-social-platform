package universitysocialplatform;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class USPGraph {
    private Map<String, List<String>> adjacencylist;
    private static Map<String, Student> students;
    private boolean isUndirected;

    // Counter for DFS numbering.
    private int dfsCounter;
    // Array to store DFS numbers
    private int[] dfsnumber;
    // Array to store lowest vertices.
    private int[] low;

    public USPGraph(int numberOfVertices, boolean isUndirected) {
        this.adjacencylist = new HashMap<>(numberOfVertices);
        this.students = new HashMap<>(numberOfVertices);
        this.isUndirected = isUndirected;
    }

    public void populateData(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            // Skip the first line of input file.
            bufferedReader.readLine();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\t");

                // Assign the data from the input file.
                String id = data[0];
                String studentFirstName = data[1];
                String studentLastName = data[2];
                String college = data[3];
                String department = data[4];
                String email = data[5];
                int friendCount = Integer.parseInt(data[6]);

                // Create student object.
                Student student = new Student(id, studentFirstName, studentLastName, college, department, email, friendCount);
                // Populate students map.
                students.put(id, student);

                List<String> friendList = new ArrayList<>();
                for (int i = 7; i < data.length; i++) {
                    if (!data[i].isEmpty()) {
                        friendList.add(data[i]);
                    }
                }
                adjacencylist.put(id, friendList);
            }
            System.out.println("Input file is read successfully..");
            System.out.println("Total number of vertices in the graph: " + getTotalVertices());
            System.out.println("Total number of edges in the graph: " + getTotalEdges());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEdge(String student1, String student2) {
        // Add student2 to the friend list of student 1.
        adjacencylist.computeIfAbsent(student1, k -> new ArrayList<>()).add(student2);

        if (isUndirected) {
            // If graph is undirected, add student1 to friend list of student2 also.
            adjacencylist.computeIfAbsent(student2, k -> new ArrayList<>()).add(student1);

        }
    }

    public int getTotalEdges() {
        int totalEdges = 0;

        for (Map.Entry<String, List<String>> entry : adjacencylist.entrySet()) {
            totalEdges += entry.getValue().size();
        }
        return  isUndirected ? totalEdges / 2 : totalEdges;
    }

    public int getTotalVertices() {
        return students.size();
    }

    public static boolean hasVertex(String student1) {
        return students.containsKey(student1);
    }

    public void removeEdge(String student1, String student2) {
        if (adjacencylist.containsKey(student1)) {
            List<String> friendList = adjacencylist.get(student1);
            friendList.remove(student2);
        }
        if (isUndirected) {
            // If graph is undirected, remove edge is the other direction as well.
            if (adjacencylist.containsKey(student2)) {
                List<String> friendList = adjacencylist.get(student2);
                friendList.remove(student1);
            }
        }
    }

    public boolean hasEdge(String student1, String student2) {
        if (adjacencylist.containsKey(student1)) {
            List<String> friendList = adjacencylist.get(student1);
            return friendList.contains(student2);
        }
        return false;
    }

    public void removeVertex(String id) {
        if (students.containsKey(id)) {
            // Removed the student from the students map.
            students.remove(id);

            // Remove the student from the adjacency list.
            List<String> friendList = adjacencylist.remove(id);

            // Removed= the student from the other students' friendlists.
            for (List<String> otherFriendList : adjacencylist.values()) {
                otherFriendList.remove(id);
            }

            // Update friend counts of other accounts.
            for (String friendId : friendList) {
                if (students.containsKey(friendId)) {
                    Student friend = students.get(friendId);
                    friend.decrementFriendCount();
                }
            }
        }
    }

    // Method to return the size of a friend list.
    public int countFriends(String id) {
        if (adjacencylist.containsKey(id)) {
            List<String> friendList = adjacencylist.get(id);
            return friendList.size();
        }
        return 0;
    }

    // Method to return the friends in a friend list.
    public List<String> getFriends(String id) {
        return adjacencylist.getOrDefault(id, new ArrayList<>());
    }

    public List<List<String>> getConnectedFriends(String college) {
        List<List<String>> connectedFriends = new ArrayList<>();
        Set<String> checked = new HashSet<>();

        // Iterate through students in graph
        for (Map.Entry<String, Student> entry : students.entrySet()) {
            String id = entry.getKey();
            Student student = entry.getValue();

            // If the student is not checked and belongs to college.
            if (!checked.contains(id) && student.getCollege().equals(college)) {
                List<String> connectedFriend = bfs(id, college, checked);
                connectedFriends.add(connectedFriend);
            }
        }
        return connectedFriends;
    }

    private List<String> bfs(String id, String college, Set<String> checked) {
        List<String> connectedFriend = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        // Mark first student as checked and enqueue.
        checked.add(id);
        queue.offer(id);

        // BFS traversal
        while (!queue.isEmpty()) {
            String currentID = queue.poll();
            connectedFriend.add(currentID);

            // Explore neighbors of current id.
            for (String friend : adjacencylist.getOrDefault(currentID, new ArrayList<>())) {
                // If the neighbor is not checked and belongs in same college.
                if (!checked.contains(friend) && students.get(friend).getCollege().equals(college)) {
                    checked.add(friend);
                    queue.offer(friend);
                }
            }
        }
        return connectedFriend;
    }

    public String getFirstName(String id) {
        return students.get(id).getFirstName();
    }

    public List<String> getFriendNames(String id) {
        List<String> friendNames = new ArrayList<>();

        // Get the list of friend IDs for student.
        List<String> friendIDs = adjacencylist.getOrDefault(id, new ArrayList<>());

        // Convert friend IDs to friend names.
        for (String friendID : friendIDs) {
            String friendName = getFirstName(id);
            friendNames.add(friendName);
        }
        return friendNames;
    }

    // Method to calculate Closeness Centrality.
    public double closenessCentrality(String id) {
        // Use Dijkstra's algorithm to calculate the shortest path.
        Map<String, Double> shortestPaths = dijkstraAlgorithm(id);

        // Calculate the closeness centrality.
        double farnessCentrality = 0.0;
        for (Double pathLength : shortestPaths.values()) {
            farnessCentrality += 1.0 / pathLength;
        }

        // Return C
        // loseness Centrality.
        return farnessCentrality / (getTotalVertices() - 1);
    }

    // Method for Dijkstra's algorithm.
    private Map<String, Double> dijkstraAlgorithm(String id) {
        Map<String, Double> shortestPaths = new HashMap<>();
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(shortestPaths::get));
        Set<String> checked = new HashSet<>();

        // Initialize distances.
        for (String vertex : students.keySet()) {
            shortestPaths.put(vertex, Double.MAX_VALUE);
        }
        shortestPaths.put(id, 0.0);

        // Initialize priority queue.
        priorityQueue.offer(id);

        // Dijkstra's algorithm.
        while (!priorityQueue.isEmpty()) {
            String currentVertex = priorityQueue.poll();

            // Skip checked vertex.
            if (checked.contains(currentVertex)) {
                continue;
            }

            checked.add(currentVertex);

            // Update distances for adjacent vertices.
            List<String> neighbors = adjacencylist.get(currentVertex);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!checked.contains(neighbor)) {
                        double newDistance = shortestPaths.get(currentVertex) + 1;
                        if (newDistance < shortestPaths.get(neighbor)) {
                            shortestPaths.put(neighbor, newDistance);
                            priorityQueue.offer(neighbor);
                        }
                    }
                }
            }
        }
        return shortestPaths;
    }

    // Method to find the connectors.
    public void findConnectors() {
        Set<String> connectors = new HashSet<>();
        Set<String> checked = new HashSet<>();
        dfsCounter = 0;
        dfsnumber = new int[students.size()];
        low = new int[students.size()];

    }
}

