import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import static java.lang.System.out;

public class _04_2_Tools {

    interface ToolsAssistant {
        String chat(String message);
    }

    static class CalculatorTool {
        @Tool("Retorna a raiz quadrada de um número fornecido.")
        public double squareRoot(double num) {
            return Math.sqrt(num);
        }
    }

    public static void main(String[] args) throws Exception {

        // Constrói o serviço de IA com o modelo de chat especificado e a ferramenta Calculator.
        var toolsAssistant = AiServices.builder(ToolsAssistant.class)
                .chatLanguageModel(OpenAiChatModel.builder()
                        .modelName("gpt-4o-mini")
                        .apiKey("demo")
                        .build())
                .tools(new CalculatorTool())
                .build();

        // Envia uma mensagem para calcular a raiz quadrada de um número e imprime a resposta.
        var finalResponse = toolsAssistant.chat("Qual a raiz quadrada de 3443242320?");
        out.println(finalResponse);
    }
}
