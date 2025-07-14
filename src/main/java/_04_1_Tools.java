import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import utils.ChatModelService;

import static java.lang.System.in;
import static java.lang.System.out;

import java.util.Scanner;

public class _04_1_Tools {

    interface ToolsAssistant {
        String chat(String message);
    }

    static class MyTools {
        @Tool("Pesquisa na Internet pela query fornecida")
        public String searchOnWeb(String query) {
            out.println("Pesquisando na web: " + query);
            return "Resultados da pesquisa para: " + query;
        }

        @Tool("Soma dois números inteiros.")
        public int addNumbers(Integer a, Integer b) {
            out.println("Calculando a soma de: " + a + " e " + b);
            return a + b;
        }
    }

    public static void main(String[] args) throws Exception {
        
        out.println("=== TOOLS EXAMPLE ===");
        out.println("\nEscreva sua mensagem: ");
        var scanner = new Scanner(in);
        var message = scanner.nextLine();
        scanner.close();

        // Configura o modelo de chat OpenAI com a chave de API.
        var chatModel = OpenAiChatModel.builder()
                        .modelName("gpt-4o-mini")
                        .apiKey("demo")
                        .build();

        // Constrói o serviço de IA com o modelo de chat especificado e a ferramenta Calculator.
        var toolsAssistant = AiServices.builder(ToolsAssistant.class)
                .chatLanguageModel(chatModel)
                .tools(new MyTools())
                .build();

        var response = toolsAssistant.chat(message);
        out.println("\n\nRESPOSTA:");
        out.println(response);
    }
}
