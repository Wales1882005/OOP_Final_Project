import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class  week8{
    public static void main(String[] args) {
        ArrayList<String> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("----- ADD TASKS -----");

        for (int i = 0; i < 3; i++) {
            System.out.print("Enter Task " + (i + 1) + ": ");
            String task = scanner.nextLine();
            tasks.add(task);
        }

        System.out.println("\n----- TASK LIST -----");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }

        try (FileWriter writer = new FileWriter("task.txt")) {
            for (String task : tasks) {
                writer.write(task + "\n");
            }
            System.out.println("\nTasks saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks: " + e.getMessage());
        }

        System.out.println("\n----- TASKS LOADED FROM FILE -----");
        try (Scanner fileScanner = new Scanner(new File("task.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: task.txt file not found.");
        }

        scanner.close();
    }
}
