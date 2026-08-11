public class main {
    public static void main(String[] args) {
        // Test person
        person p1 = new person("John Doe", 12345);
        System.out.println("Name: " + p1.getName());
        System.out.println("ID: " + p1.getId());
        p1.introduce();
        
        System.out.println();
        
        // Test student
        Student s1 = new Student("Jane Smith", 67890);
        System.out.println("Name: " + s1.getName());
        System.out.println("ID: " + s1.getId());
        s1.introduce();
        
        System.out.println();
        
        // Test lecturer
        lecturer l1 = new lecturer("Dr. Brown", 11111, "Mathematics");
        System.out.println("Name: " + l1.getName());
        System.out.println("ID: " + l1.getId());
        l1.introduce();
    }
    
}
