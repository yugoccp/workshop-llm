import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.System.out;

public class _01_Model {
    public static void main(String[] args) throws Exception {
        var json = """
                {
                  "model": "gpt-4o-mini",
                  "messages": [{"role": "user", "content": "Olá Chat!"}]
                }
                """;

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://langchain4j.dev/demo/openai/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try(var client = HttpClient.newHttpClient()) {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            var mapper = new ObjectMapper();
            var responseNode = mapper.readTree(response);
            out.println(responseNode.toPrettyString());
        }
    }
}