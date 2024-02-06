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
                """));

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {

                System.out.println("Enter your question: ");
                String question = scanner.nextLine();

                if (question.equals("exit")) {
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
