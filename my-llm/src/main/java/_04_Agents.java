import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;

public class _04_Agents {

    static class MyAgent {
        @Tool("...")
        void printAgent() {
            System.out.println("Personal secret agent activated!");
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
                .tools(new MyAgent())
                .build();

        var question = "What is the sum of the numbers of letters in the words 'language' and 'model'";
        var response = assistant.chat(question);

        System.out.println(response.content().text());

        System.out.println("\n\n########### TOKEN USAGE ############\n");
        System.out.println(response.tokenUsage());
    }
}
