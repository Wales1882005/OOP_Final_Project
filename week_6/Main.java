public class Main {
    public static void main(String[] args) {
        // Instantiate Lecturer with the details shown in the expected output
        Lecturer lecturer = new Lecturer("L100", "Dr Ahmad", "Java Programming", "Faculty of Information Technology");

        // Display information
        lecturer.displayInfo();
        lecturer.displaySubject();
    }
}