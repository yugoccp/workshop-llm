import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ChatModelService;

import java.util.ArrayList;

import static java.lang.System.out;

public class _04_1_Tools {

    public static void main(String[] args) throws Exception {
        // Define a função da ferramenta em formato JSON
        var toolDefinition = """
               {
                   "type": "function",
                   "function": {
                       "name": "squareRoot",
                       "description": "Returns a square root of a given number.",
                       "parameters": {
                           "type": "object",
                           "properties": {
                               "num": { "type": "number" }
                           },
                           "required": ["num"],
                           "additionalProperties": "false"
                       },
                       "strict": "true"
                   }
               }""";

        var messages = new ArrayList<String>();
        messages.add("""
                {"role": "user", "content": "Qual a raiz quadrada de 3443242320?"}
                """);

        var chatModel = new ChatModelService();
        var response = chatModel.generate(messages, toolDefinition);

        var mapper = new ObjectMapper();
        var messageNode = mapper.readTree(response).get("choices").get(0).get("message");

        // Verifica se o modelo solicitou uma chamada de ferramenta
        var toolCall = extractToolCall(messageNode);
        if (toolCall != null) {

            // Adiciona o resultado da chamada da ferramenta ao histórico da conversa
            messages.add(messageNode.toString());
            messages.add(String.format("""
                    {"role": "tool", "tool_call_id": "%s", "content": "%f"}
                    """, toolCall.toolId, squareRoot(toolCall.numArgument)));

            // Gera uma resposta final com as mensagens atualizadas
            response = chatModel.generate(messages, toolDefinition);
            messageNode = mapper.readTree(response).get("choices").get(0).get("message");
        }

        // Exibe o conteúdo final retornado pelo LLM
        var finalResponse = messageNode.get("content").asText();
        out.println(finalResponse);
    }


    /**
     * Calculates the square root of a given number.
     */
    public static double squareRoot(double num) {
        return Math.sqrt(num);
    }

    /**
     * Auxiliary record to represent a tool call.
     */
    private record ToolCall(String toolId, double numArgument){}

    /**
     * Extracts the tool call from a message node.
     */
    private static ToolCall extractToolCall(JsonNode messageNode) throws Exception {
        if (messageNode.has("tool_calls")) {
            var toolNode = messageNode.get("tool_calls").get(0);
            var toolId = toolNode.get("id").asText();
            var toolArgs = toolNode.get("function").get("arguments").asText();
            var mapper = new ObjectMapper();
            var numArg = mapper.readTree(toolArgs).get("num").asDouble();
            return new ToolCall(toolId, numArg);
        }
        return null;
    }
}
