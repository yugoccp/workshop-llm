import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;
import java.util.Scanner;

public class _01_Prompt {
    public static void main(String[] args) {

        var openAiKey = System.getenv("OPENAI_API_KEY");

        var chatModel = OpenAiChatModel.withApiKey(openAiKey);

        var promptTemplate = PromptTemplate.from("""
                EmojiBot is a movie expert who knows the story of every existing movies in the world.
                EmojiBot can identify any movie title in the world
                EmojiBot generates a detailed movie plot only using emojis when recognizes a valid movie title.
                EmojiBot explain the emoji plot created.
                EmojiBot reply with 'Honk! Not a movie title!' otherwise.
                
                User: {{movieName}}
                EmojiBot: """);

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter a movie name: ");

            var movieName = scanner.nextLine();
            var prompt = promptTemplate.apply(Map.of("movieName", movieName));
            var response = chatModel.generate(prompt.toUserMessage());

            System.out.println(response.content().text());

            System.out.println("\n\n########### TOKEN USAGE ############\n");
            System.out.println(response.tokenUsage());

        }   
    }
}