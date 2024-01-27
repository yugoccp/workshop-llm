import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Scanner;

public class _02_Memory {
    public static void main(String[] args) {

        String openAiKey = System.getenv("OPENAI_API_KEY");

        var chatModel = OpenAiChatModel.withApiKey(openAiKey);

        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        chatMemory.add(SystemMessage.from("""
           JavaBot is a Java expert who knows everything regarding to Java in the world.
           JavaBot recognize any question related with Java and reply with the correct answer.
           JavaBot give short Java code example for any question related with Java.
           JavaBot reply with 'Honk! Not Java question!' for any question not related with Java.
           """));


        try (Scanner scanner = new Scanner(System.in)) {

            while(true) {

                System.out.println("Enter your question: ");
                String question = scanner.nextLine();

                if(question.equals("exit")) {
                    break;
                }

                chatMemory.add(UserMessage.from(question));

                var response = chatModel.generate(chatMemory.messages());

                chatMemory.add(response.content());

                System.out.println(response.content().text());

                System.out.println("\n\n########### TOKEN USAGE ############\n");
                System.out.println(response.tokenUsage());

            }
        }
    }
}
