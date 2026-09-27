import java.util.Scanner;

/**
 * Menu-driven console application. Owner: Afriha (menu + integration).
 *
 * Integration rule: a Student object is created once and the SAME object is stored in the
 * linked list, the BST and the hash table. So update() on the list also updates the others.
 */
public class Main {

    static Scanner sc = new Scanner(System.in);

    // The data structures shared by the whole menu
    static StudentLinkedList list = new StudentLinkedList();
    static StudentBST bst = new StudentBST();
    static StudentHashTable hashTable = new StudentHashTable();
    static ActionStack history = new ActionStack();
    static ServiceRequestQueue requests = new ServiceRequestQueue();
    static CampusGraph campus = new CampusGraph();

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:  addStudent(); break;
                case 2:  updateStudent(); break;
                case 3:  deleteStudent(); break;
                case 4:  list.display(); break;
                case 5:  addServiceRequest(); break;
                case 6:  requests.dequeueAndProcess(); break;
                case 7:  history.displayRecent(); break;
                case 8:  bst.inorderDisplay(); break;
                case 9:  searchWithHashing(); break;
                case 10: addLocation(); break;
                case 11: removeLocation(); break;
                case 12: addConnection(); break;
                case 13: removeConnection(); break;
                case 14: campus.displayConnections(); break;
                case 15: traverseCampus(); break;
                case 16: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Enter 1-16.");
            }
        } while (choice != 16);
    }

    static void printMenu() {
        System.out.println("\n===== University Student Record & Campus Route System =====");
        System.out.println("1. Add Student Record");
        System.out.println("2. Update Student Record");
        System.out.println("3. Delete Student Record");
        System.out.println("4. Display All Records using Linked List");
        System.out.println("5. Add Service Request to Queue");
        System.out.println("6. Process Next Service Request");
        System.out.println("7. Display Recent Actions using Stack");
        System.out.println("8. Display Students using BST");
        System.out.println("9. Search Student using Hashing");
        System.out.println("10. Add Campus Location");
        System.out.println("11. Remove Campus Location");
        System.out.println("12. Add Campus Connection/Road");
        System.out.println("13. Remove Campus Connection/Road");
        System.out.println("14. Display Campus Connections");
        System.out.println("15. Traverse Campus Locations using BFS");
        System.out.println("16. Exit");
    }

    // ================= Student operations (options 1, 2, 3) =================

    static void addStudent() {
        String id = readNonEmpty("Student ID: ");
        String name = readNonEmpty("Name: ");
        String programme = readNonEmpty("Programme: ");
        double marks = readMarks();

        Student s = new Student(id, name, programme, marks);
        if (list.add(s)) {                 // add() rejects duplicate IDs
            bst.insert(s);
            hashTable.put(s);
            history.push("Added student " + id);
            System.out.println("Student added.");
        } else {
            System.out.println("Error: Student ID " + id + " already exists.");
        }
    }

    static void updateStudent() {
        String id = readNonEmpty("Student ID to update: ");
        if (list.search(id) == null) {
            System.out.println("Error: Student " + id + " not found.");
            return;
        }
        String name = readNonEmpty("New name: ");
        String programme = readNonEmpty("New programme: ");
        double marks = readMarks();

        // Same Student object is in the BST and hash table, so they are updated too
        list.update(id, name, programme, marks);
        history.push("Updated student " + id);
        System.out.println("Student updated.");
    }

    static void deleteStudent() {
        String id = readNonEmpty("Student ID to delete: ");
        Student deleted = list.delete(id);
        if (deleted == null) {
            System.out.println("Error: Student " + id + " not found.");
            return;
        }
        bst.delete(id);
        hashTable.remove(id);
        history.push("Deleted student " + id + " (" + deleted.getName() + ")");
        System.out.println("Student deleted.");
    }

    // ================= Queue and hashing (options 5, 9) =================

    static void addServiceRequest() {
        String id = readNonEmpty("Student ID: ");
        if (list.search(id) == null) {
            System.out.println("Error: Student " + id + " not found.");
            return;
        }
        String request = readNonEmpty("Service request (e.g. Transcript): ");
        requests.enqueue(id + " - " + request);
        System.out.println("Request added to the queue.");
    }

    static void searchWithHashing() {
        String id = readNonEmpty("Student ID to search: ");
        Student s = hashTable.get(id);
        if (s == null) {
            System.out.println("Student " + id + " not found.");
        } else {
            System.out.println("Found: " + s);
        }
    }

    // ================= Campus graph (options 10 - 15) =================

    static void addLocation() {
        String name = readNonEmpty("Location name: ");
        if (campus.addLocation(name)) {
            System.out.println("Location added.");
        } else {
            System.out.println("Error: Location already exists.");
        }
    }

    static void removeLocation() {
        String name = readNonEmpty("Location to remove: ");
        if (campus.removeLocation(name)) {
            System.out.println("Location and its connections removed.");
        } else {
            System.out.println("Error: Location not found.");
        }
    }

    static void addConnection() {
        String a = readNonEmpty("From location: ");
        String b = readNonEmpty("To location: ");
        if (campus.addConnection(a, b)) {
            System.out.println("Connection added.");
        } else {
            System.out.println("Error: Cannot add (location missing, same location, or already connected).");
        }
    }

    static void removeConnection() {
        String a = readNonEmpty("From location: ");
        String b = readNonEmpty("To location: ");
        if (campus.removeConnection(a, b)) {
            System.out.println("Connection removed.");
        } else {
            System.out.println("Error: That connection does not exist.");
        }
    }

    static void traverseCampus() {
        String start = readNonEmpty("Start location: ");
        if (!campus.hasLocation(start)) {
            System.out.println("Error: Location not found.");
            return;
        }
        campus.bfs(start);
    }

    // ================= Input validation helpers =================

    /** Keeps asking until the user types a whole number. */
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /** Keeps asking until the user types something (not empty). */
    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("Input cannot be empty.");
        }
    }

    /** Keeps asking until marks are a number between 0 and 100. */
    static double readMarks() {
        while (true) {
            System.out.print("Marks (0-100): ");
            String line = sc.nextLine().trim();
            try {
                double m = Double.parseDouble(line);
                if (m >= 0 && m <= 100) return m;
                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
