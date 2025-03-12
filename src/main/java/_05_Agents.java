import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.lang.System.out;

public class _05_Agents {

    interface BaseAgent {
        String chat(String message);
    }

    @SystemMessage("""
        Coordinate responses from multiple agents to produce a final answer.
        Use only the provided content.
        If no agent can handle the request, apologize and ask for a new query.
    """)
    interface OrchestratorAgent extends BaseAgent {}

    @SystemMessage("""
        Analyze the provided content to identify key insights.
        Use only the provided data.
        If analysis is not possible, provide an apology.
    """)
    interface DataAnalystAgent extends BaseAgent {}

    @SystemMessage("""
        Format the content as a concise executive email.
        Highlight main points, suggest next steps when applicable, and ask for feedback.
    """)
    interface EmailWriterAgent extends BaseAgent {}

    @SystemMessage("""
        Interpret the user's intent to generate relevant web search queries.
        Execute a web search for each query.
        Return structured and detailed search results.
    """)
    interface WebSearchAgent extends BaseAgent {}

    public static void main(String[] args) {
        var chatModel = OpenAiChatModel.builder()
                .modelName("gpt-4o-mini")
                .apiKey("demo")
                .build();

        var dataAgent = AiServices.create(DataAnalystAgent.class, chatModel);
        var emailAgent = AiServices.create(EmailWriterAgent.class, chatModel);
        var webAgent = AiServices.builder(WebSearchAgent.class)
                .chatLanguageModel(chatModel)
                .tools(new WebSearchTool())
                .build();

        var orchestratorAgent = AiServices.builder(OrchestratorAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new OrchestratorTool(dataAgent, emailAgent, webAgent))
                .build();

        out.println("Escreva sua mensagem:");
        var scanner = new Scanner(System.in);
        var response = orchestratorAgent.chat(scanner.nextLine());
        out.println(response);
    }

    static class OrchestratorTool {
        private final DataAnalystAgent dataAgent;
        private final EmailWriterAgent emailAgent;
        private final WebSearchAgent webAgent;

        public OrchestratorTool(DataAnalystAgent dataAgent, EmailWriterAgent emailAgent, WebSearchAgent webAgent) {
            this.dataAgent = dataAgent;
            this.emailAgent = emailAgent;
            this.webAgent = webAgent;
        }

        @Tool("Delegate task for WebSearchAgent")
        public String webSearch(String task) {
            out.println("Delegating WebSearchAgent: " + task);
            return webAgent.chat(task);
        }

        @Tool("Delegate task for DataAnalystAgent")
        public String dataAnalysis(String task) {
            out.println("Delegating DataAnalystAgent: " + task);
            return dataAgent.chat(task);
        }

        @Tool("Delegate task for EmailWriterAgent")
        public String writeEmail(String task) {
            out.println("Delegating EmailWriterAgent: " + task);
            return emailAgent.chat(task);
        }
    }

    static class WebSearchTool {
        @Tool("Return results from the web based on the query.")
        public String searchWeb(String query) throws Exception {
            var queryParam = URLEncoder.encode(query, StandardCharsets.UTF_8);
            var apiUrl = String.format("https://api.duckduckgo.com/?q=%s&format=json", queryParam);
            out.println("Searching the web for: " + apiUrl);

            var request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
            try (var client = HttpClient.newHttpClient()) {
                var result = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                out.println("Web search result: " + result);
                return result;
            }
        }
    }
}