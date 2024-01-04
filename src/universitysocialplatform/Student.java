package universitysocialplatform;

import java.util.List;

public class Student {
    private String id;
    private String studentsFirstName;
    private String studentLastName;
    private String college;
    private String department;
    private String email;
    private int friendCount;
    List<Integer> friendList;

    public Student(String id, String studentsFirstName, String studentLastName, String college,
                   String department, String email, int friendCount) {
        this.id = id;
        this.studentsFirstName = studentsFirstName;
        this.studentLastName = studentLastName;
        this.college = college;
        this.department = department;
        this.email = email;
        this.friendCount = friendCount;
        this.friendList = friendList;
    }

    public String getStudentsFirstName() {
        return studentsFirstName;
    }

    // Getter to return friend count.
    public int getFriendCount() {
        return friendCount;
    }

    // Method to decrement friend count.
    public void decrementFriendCount() {
        if (friendCount > 0) {
            friendCount--;
        }
    }

    // Getter to return College.
    public Object getCollege() {
        return college;
    }

    public String getFirstName() {
        return studentsFirstName;
    }
}
