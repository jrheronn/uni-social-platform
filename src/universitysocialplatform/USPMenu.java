package universitysocialplatform;

import universitysocialplatform.USPGraph;

import java.util.List;
import java.util.Scanner;
public class USPMenu {
    static Scanner scanner = new Scanner(System.in);

    static USPGraph uspGraph = new USPGraph(100, true);

    public static void main(String[] args) {
        // Prompt user for input file name.
        System.out.println("Enter input file name: ");
        String file = scanner.next();

        // Populate graph from input file.
        uspGraph.populateData(file);

        int option;
        do {
            displayMenu();
            System.out.print("Enter your Option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    //Remove Friendship.
                    removeFriendship();
                    break;
                case 2:
                    // Delete Account.
                    deleteAccount();
                    break;
                case 3:
                    // Count Friends.
                    countFriends();
                    break;
                case 4:
                    // Friend Circle.
                    friendsCircle();
                    break;
                case 5:
                    // Closeness Centrality.
                    closenessCentrality();
                    break;
                case 6:
                    // Find Connectors.
                    uspGraph.findConnectors();
                    break;
                case 7:
                    // Exit.
                    System.out.println("Exiting USP");
                    break;
            }
        } while (option != 7);

        // Close scanner.
        scanner.close();
    }

    // Method to print menu options
    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Remove friendship");
        System.out.println("2. Delete Account");
        System.out.println("3. Count friends");
        System.out.println("4. Friends Circle");
        System.out.println("5. Closeness centrality");
        System.out.println("6. Find Connectors");
        System.out.println("7. Exit");
    }

    private static void closenessCentrality() {
        // Prompt user to enter the student ID to calculate closeness centrality.
        System.out.println("Enter the student ID to determine closeness centrality: ");
        String id = scanner.next();

        // Check it student exists.
        if (uspGraph.hasVertex(id)) {
            // Calculate closeness centrality.
            double closenessCentrality = uspGraph.closenessCentrality(id);

            // Calculate the normalized closeness centrality
            int totalVertices = uspGraph.getTotalVertices();
            double normalizedClosenessCentrality = closenessCentrality / (totalVertices - 1);

            // Display closeness centrality and normalized closeness centrality.
            String firstName = uspGraph.getFirstName(id);
            System.out.println("The Closeness Centrality for " + firstName + ": " + closenessCentrality);
            System.out.println("The Normalized Closeness Centrality for " + firstName + ": " + normalizedClosenessCentrality);
        } else {
            // Display error if student is not found.
            System.out.println("Sorry..");
            System.out.println(id + " not found.");
        }
    }

    private static void friendsCircle() {
        // Prompt user for college name.
        System.out.println("Enter the college to find the friend circle: ");
        String college = scanner.next();

        // Get connected friends for the college.
        List<List<String>> connectedFriends = uspGraph.getConnectedFriends(college);

        // Display the friend circles
        System.out.println("Following are the friend circles in the College of " + college + ":");
        for (List<String> friends : connectedFriends) {
            // Display individual friend circle.
            for (String id : friends) {
                System.out.print(id);
                // Add separator.
                if (friends.indexOf(id) < friends.size() - 1) {
                    System.out.print(" - ");
                }
            }
            // Add an empty line between friend circles.
            System.out.println();
        }
    }

    private static void countFriends() {
        System.out.println("Enter the ID of the student to count friends: ");
        String id = scanner.next();

        if (uspGraph.hasVertex(id)) {
            // Get student's first name..
            String firstName = uspGraph.getFirstName(id);

            // Call countFriends method to retrieve count and friend list.
            int friendCount = uspGraph.countFriends(id);
            List<String> friends = uspGraph.getFriendNames(id);

            // Display the count and friend list.
            System.out.println("Friend count for " + firstName + ": " + friendCount);
            System.out.println("Friends of " + firstName + " are:");
            for (String friend : friends) {
                System.out.println(friend);
            }
        } else {
            System.out.println("Sorry..");
            System.out.println(id + " not found!");
        }

    }

    private static void deleteAccount() {
        System.out.println("Enter the ID of the student account to delete: ");
        String id = scanner.next();

        if (USPGraph.hasVertex(id)) {
            // Get the first name of the student.
            String firstName = uspGraph.getFirstName(id);

            // Remove vertex and edges.
            uspGraph.removeVertex(id);
            System.out.println("The student " + firstName + " has been successfully removed.");
        } else {
            System.out.println("Sorry..");
            System.out.println(id + " not found!");
        }
        // Display the update graph information.
        displayGraphTotals();
    }

    private static void displayGraphTotals() {
        System.out.println("Total number of vertices in the graph " + uspGraph.getTotalVertices());
        System.out.println("Total number of edges in the graph " + uspGraph.getTotalEdges());
    }

    private static void removeFriendship() {
        // Prompt user for IDs of students to remove friendship.
        System.out.print("Enter ID of first student: ");
        String student1 = scanner.next();
        System.out.print("Enter ID of the second student: ");
        String student2 = scanner.next();

        // Check if students exist in graph.
        if (uspGraph.hasVertex(student1) && uspGraph.hasVertex(student2)) {
            // Get the first names of the students from IDs.
            String firstName1 = uspGraph.getFirstName(student1);
            String firstName2 = uspGraph.getFirstName(student2);

            // Remove the edge between the students.
            if (uspGraph.hasEdge(student1, student2)) {
                uspGraph.removeEdge(student1, student2);
                System.out.println("The friendship between the students " + firstName1 + " and " + firstName2 + " has been successfully removed.");
            } else {
                // Display message if no edge exist.
                System.out.println("Sorry.. There is no edge between the vertices " + firstName1 + " and " + firstName2);
            }
        } else {
            // Display message if no accounts with those IDs exists.
            System.out.println("Sorry..");
            if (uspGraph.hasVertex(student1)) {
                System.out.println(student1 + " not found!");
            }
            if (uspGraph.hasVertex(student2)) {
                System.out.println(student2 + " not found!");
            }
            displayGraphTotals();
        }
    }
}
