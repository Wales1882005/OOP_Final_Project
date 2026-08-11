public class Student {
    
    int studentID;
    String name;
    int age;
    double gpa;

    
    public Student(int studentID, String name, int age, double gpa) {
        this.studentID = studentID;
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }

    
    public void displayInfo() {
        System.out.println("ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("GPA: " + gpa);
    }

    public void study() {
        System.out.println(name + " is studying.");
    }

    public void exam() {
        System.out.println(name + " is taking an exam.");
    }
}
