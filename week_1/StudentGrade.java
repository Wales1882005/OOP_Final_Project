import java.util.Scanner;

public class StudentGrade {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

      
        System.out.print("Enter student score (0 - 100): ");
        double score = scanner.nextDouble();

        
        char grade;
        if (score >= 80 && score <= 100) {
            grade = 'A';
        } else if (score >= 75) {
            grade = 'A';
        } else if (score >= 70) {
            grade = 'B';
        } else if (score >= 65) {
            grade = 'B';
        } else if (score >= 60) {
            grade = 'C';
        } else if (score >= 50) {
            grade = 'D';
        } else if (score >= 0) {
            grade = 'F';
        } else {
            System.out.println("Invalid score entered!");
            scanner.close();
            return;
        }

        
        System.out.println("Score: " + score);
        System.out.println("Grade: " + grade);

        scanner.close();
    }
}
