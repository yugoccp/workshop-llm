import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

public class _05_Agents {

    interface BaseChatAgent {
        String chat(String message);
    }

    @SystemMessage("""
                You're responsible to coordinate responses from multiple agents to produce a final answer.
                Understand the user's request and create a plan to address it.
                Use the provided agents to delegate tasks to the appropriate agents.
                Synthesize the results from the agents to provide a coherent response.
                Use only the contents provided by the agents.
                If no agent can handle the request, apologize and ask for a new query.
            """)
    interface OrchestratorAgent extends BaseChatAgent {
    }

    @SystemMessage("""
                Analyze the provided content to identify key insights.
                Use only the provided data.
                If analysis is not possible, provide an apology.
            """)
    interface DataAnalystAgent extends BaseChatAgent {
    }

    @SystemMessage("""
                Format the content as a concise executive email.
                Highlight main points, suggest next steps when applicable, and ask for feedback.
            """)
    interface EmailWriterAgent extends BaseChatAgent {
    }

    @SystemMessage("""
                Interpret the user's intent to generate relevant web search queries.
                Execute a web search for each query.
                Return structured and detailed search results.
            """)
    interface WebSearchAgent extends BaseChatAgent {
    }

    public static void main(String[] args) {
        var chatModel = OpenAiChatModel.builder()
                .modelName("gpt-4o-mini")
                .apiKey("demo")
                .build();

        var orchestratorAgent = AiServices.builder(OrchestratorAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new OrchestratorTool(chatModel))
                .build();

        out.println("=== AGENTS EXAMPLE ===");
        out.println("\nEscreva sua mensagem:");
        var scanner = new Scanner(System.in);
        var response = orchestratorAgent.chat(scanner.nextLine());
        out.println(response);
    }

    static class OrchestratorTool {

        private DataAnalystAgent dataAgent;
        private EmailWriterAgent emailAgent;
        private WebSearchAgent webAgent;

        public OrchestratorTool(ChatLanguageModel chatModel) {
            dataAgent = AiServices.create(DataAnalystAgent.class, chatModel);
            emailAgent = AiServices.create(EmailWriterAgent.class, chatModel);
            webAgent = AiServices.builder(WebSearchAgent.class)
                    .chatLanguageModel(chatModel)
                    .tools(new WebSearchTool())
                    .build();
        }

        @Tool("Delegate task analyse data.")
        public String delegateDataAgent(@P("Task description") String task) {
            out.println("\n\n### Delegating task to DataAnalystAgent: " + task);
            return dataAgent.chat(task);
        }

        @Tool("Delegate task to write email.")
        public String delegateEmailAgent(@P("Task description") String task) {
            out.println("\n\n### Delegating task to EmailWriterAgent: " + task);
            return emailAgent.chat(task);
        }

        @Tool("Delegate task to search on web.")
        public String delegateDataAnalysis(@P("Task description") String task) {
            out.println("\n\n### Delegating task to WebSearchAgent: " + task);
            return webAgent.chat(task);
        }
    }

    static class WebSearchTool {
        @Tool("Return results from the web based on the query.")
        public String searchWeb(String query) throws Exception {
            query = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://html.duckduckgo.com/html/?q=" + query;
            out.println("Searching the web for: " + query);
            var doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            var results = doc.select("div.result__body");
            var resultsBuilder = new StringBuilder();
            for (Element result : results) {
                Element resultTitle = result.selectFirst(".result__title a");
                String snippet = result.selectFirst("a.result__snippet").text();
                String title = resultTitle.text();
                String link = resultTitle.absUrl("href");  // Get absolute link
                resultsBuilder.append("""
                    Title: %s
                    Link: %s
                    Snippet: %s
                    ---
                    """.formatted(title, link, snippet));
            }

            return resultsBuilder.toString();
        }
    }
}