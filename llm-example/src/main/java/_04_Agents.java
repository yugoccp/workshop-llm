import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;

public class _04_Agents {

    static class Calculator {
        @Tool("Calculates the length of a string")
        int stringLength(String s) {
            return s.length();
        }

        @Tool("Calculates the sum of two numbers")
        int add(int a, int b) {
            return a + b;
        }
    }

    interface Assistant {
        Response<AiMessage> chat(String userMessage);
    }

    public static void main(String[] args) {

        String openAiKey = System.getenv("OPENAI_API_KEY");

        var assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(openAiKey))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new Calculator())
                .build();

        var question = "What is the sum of the numbers of letters in the words 'language' and 'model'";
        var response = assistant.chat(question);

        System.out.println(response.content().text());

        System.out.println("\n\n########### TOKEN USAGE ############\n");
        System.out.println(response.tokenUsage());
    }
}
