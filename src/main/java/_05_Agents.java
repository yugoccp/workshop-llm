import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
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

    interface BaseAgent {
        String chat(String message);
    }

    @SystemMessage("""
                Coordinate responses from multiple agents to produce a final answer.
                Use only the provided content.
                If no agent can handle the request, apologize and ask for a new query.
            """)
    interface OrchestratorAgent extends BaseAgent {
    }

    @SystemMessage("""
                Analyze the provided content to identify key insights.
                Use only the provided data.
                If analysis is not possible, provide an apology.
            """)
    interface DataAnalystAgent extends BaseAgent {
    }

    @SystemMessage("""
                Format the content as a concise executive email.
                Highlight main points, suggest next steps when applicable, and ask for feedback.
            """)
    interface EmailWriterAgent extends BaseAgent {
    }

    @SystemMessage("""
                Interpret the user's intent to generate relevant web search queries.
                Execute a web search for each query.
                Return structured and detailed search results.
            """)
    interface WebSearchAgent extends BaseAgent {
    }

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

        private final Map<String, BaseAgent> agents = new HashMap<>();

        public OrchestratorTool(DataAnalystAgent dataAgent, EmailWriterAgent emailAgent, WebSearchAgent webAgent) {
            agents.put("DataAnalystAgent", dataAgent);
            agents.put("EmailWriterAgent", emailAgent);
            agents.put("WebSearchAgent", webAgent);
        }

        @Tool("""
                    Delegate tasks to the appropriate agent.
                    Available agents:
                    - DataAnalystAgent: Help with data analysis.
                    - EmailWriterAgent: Help with email writing.
                    - WebSearchAgent: Help with web searches.
                """)
        public String delegate(@P("Agent name") String agent, @P("Task description") String task) {
            out.println("Delegating task to " + agent + ": " + task);
            return agents.get(agent).chat(task);
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