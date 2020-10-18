import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Random random = new Random();
        while (true) {
            String input = scanner.nextLine();
            System.out.println(random.nextInt(30) + " " + random.nextInt(16));
        }
    }
}
