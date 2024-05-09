import java.sql.*;
import java.util.Scanner;

public class College {
    private static final String url = "jdbc:mysql://localhost:3306/College";
    private static final String user = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Show the list of all the enrolled students");
            System.out.println("2. Show list of all the instructors");
            System.out.println("3. Show list of the students in a specific course");
            System.out.println("4. Show list of students from the same country, doing a specific course, and above 20 years old");
            System.out.println("5. Delete a student information");
            System.out.println("6. Edit a student detail");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 0:
                    System.out.println("Exiting program...");
                    scanner.close();
                    System.exit(0);
                    break;
                case 1:
                    showStudents();
                    break;
                case 2:
                    showInstructors();
                    break;
                case 3:
                    StudentsInCourse(scanner);
                    break;
                case 4:
                    StudentsInCriteria(scanner);
                    break;
                case 5:
                    deleteStudent(scanner);
                    break;
                case 6:
                    editStudentDetails(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void showStudents() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM students";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                System.out.println("Here is a list of all the students:");
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name") +
                            ", Age: " + resultSet.getInt("age") + ", Course: " + resultSet.getString("course"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showInstructors() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM instructor";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                System.out.println("Here is a list of all our instructors:");
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("instructor_id") + ", Name: " + resultSet.getString("name") +
                            ", Course: " + resultSet.getString("course"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void StudentsInCourse(Scanner scanner) {
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM students WHERE course = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, courseName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    System.out.println("The list of students in " + courseName + " course are:");
                    while (resultSet.next()) {
                        System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name") +
                                ", Age: " + resultSet.getInt("age"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void StudentsInCriteria(Scanner scanner) {
        System.out.print("Enter the course name: ");
        String courseName = scanner.nextLine();
        
        System.out.print("Enter the country: ");
        String country = scanner.nextLine();
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM students WHERE course = ? AND country = ? AND age > 20";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, courseName);
                statement.setString(2, country);
                try (ResultSet resultSet = statement.executeQuery()) {
                    System.out.println("The list of students in " + courseName + " course from " + country + " above 20 years old are:");
                    while (resultSet.next()) {
                        System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name") +
                                ", Age: " + resultSet.getInt("age"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter the ID of the student you want to delete: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM students WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student with ID " + studentId + " deleted successfully.");
                } else {
                    System.out.println("No student was found with ID " + studentId + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private static void editStudentDetails(Scanner scanner) {
        System.out.print("Enter the ID of the student you want to edit: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter the new name: ");
        String newName = scanner.nextLine();
        
        System.out.print("Enter the new age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Enter the new course: ");
        String newCourse = scanner.nextLine();
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newName);
                statement.setInt(2, newAge);
                statement.setString(3, newCourse);
                statement.setInt(4, studentId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student details updated successfully.");
                } else {
                    System.out.println("No student was found with ID " + studentId + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
