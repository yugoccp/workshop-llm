import dev.ai4j.openai4j.Model;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class _00_Model {

    public static void main(String[] args) {

        String openAiKey = System.getenv("OPENAI_API_KEY");

        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .modelName(Model.GPT_3_5_TURBO.stringValue())
                .apiKey(openAiKey)
                .build();

        var prompt = "";
        var response = chatModel.generate(UserMessage.from(prompt));

        System.out.println(response.content().text());

        System.out.println("\n\n########### TOKEN USAGE ############\n");
        System.out.println(response.tokenUsage());
    }
}