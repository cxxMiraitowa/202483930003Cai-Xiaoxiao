import java.util.Scanner;

public class fightInterface {

    public boolean showFightInterface(int monsterPower, Scanner scanner) {
        System.out.println("\n========== BATTLE INTERFACE ==========");
        System.out.println("You encountered a monster!");
        System.out.println("Monster Power: " + monsterPower);
        System.out.println("Choose your action:");
        System.out.println("1. Fight");
        System.out.println("2. run");
        System.out.print("Enter your choice (1 or 2): ");

        while (true) {
            String input = scanner.next();
            if (input.equals("1")) {
                System.out.println("You chose to fight!");
                return true;
            } else if (input.equals("2")) {
                System.out.println("You chose to run!");
                return false;
            } else {
                System.out.print("Invalid input, please enter 1 or 2: ");
            }
        }
    }
}

