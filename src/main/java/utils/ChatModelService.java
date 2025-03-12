package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

public class ChatModelService {

    // Using demo URL. For OpenAI, add authentication header and use `https://api.openai.com/v1` URL.
    private final String CHAT_URL = "http://langchain4j.dev/demo/openai/v1/chat/completions";

    /**
     * Generates a response from the chat model for a single message.
     *
     * @param message The message to send to the chat model.
     * @return The response message from the chat model.
     */
    public String generate(String message) {
        var json = String.format("""
                {
                  "model": "gpt-4o-mini",
                  "messages": [{"role": "user", "content": "%s"}]
                }
                """, message);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(CHAT_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try(var client = HttpClient.newHttpClient()) {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            var mapper = new ObjectMapper();
            return mapper.readTree(response).get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            return "Erro ao gerar resposta.";
        }
    }

    /**
     * Generates a response from the chat model for a collection of messages and tools.
     *
     * @param messages The collection of messages to send to the chat model.
     * @param tools The tools definition in JSON format.
     * @return The raw response from the chat model.
     */
    public String generate(Collection<String> messages, String tools) {
        var json = String.format("""
                {
                    "model": "gpt-4o-mini",
                    "messages": [%s],
                    "tools": [%s]
                }
                """, StringUtils.join(messages, ","), tools);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(CHAT_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try(var client = HttpClient.newHttpClient()) {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            return "Erro ao gerar resposta.";
        }
    }


}