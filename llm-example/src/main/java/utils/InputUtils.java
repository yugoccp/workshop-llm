package utils;

import java.util.Scanner;
import java.util.function.Consumer;

public class InputUtils {

    static void SystemInputLoop(Consumer<String> consumer) {
        try (Scanner scanner = new Scanner(System.in)) {

            while(true) {

                System.out.println("Enter your question: ");
                String question = scanner.nextLine();

                if(question.equals("exit")) {
                    break;
                }

                consumer.accept(question);

            }
        }
    }
    
}
