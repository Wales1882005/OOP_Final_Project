class Student {
    // Attributes
    String name;
    int age;
    double gpa;

    // Constructor
    Student(String studentName, int studentAge, double studentGpa) {
        name = studentName;
        age = studentAge;
        gpa = studentGpa;
    }

    public static void main(String[] args) {
        Student s1 = new Student("Alice", 20, 3.8);

        System.out.println("Name: " + s1.name);
        System.out.println("Age: " + s1.age);
        System.out.println("GPA: " + s1.gpa);
        
        // Calling the method
        s1.study();
    } 

    public void study() {
        System.out.println(name + " is studying.");
    }
}