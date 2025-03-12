import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import utils.ChatModelService;

import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.System.in;
import static java.lang.System.out;

public class _03_Retrieval {

    public static void main(String[] args) {
        // Construindo diferentes contextos para exemplo
        var context = Stream.of(
                        "Documentação de API: Guia completo para integrar serviços de recomendação via IA.",
                        "Quarta: Reunião com cliente | Sábado: Almoço com os pais às 14h | Domingo: Churrasco com amigos às 12h.",
                        "Previsão do tempo hoje é de sol e calor durante o dia todo, com chuva para a noite.")
                .map(TextSegment::from)
                .toList();

        // Transformando o contexto em vetores e armazenando em base de conhecimento
        var embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        var embeddings = embeddingModel.embedAll(context).content();
        var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();
        embeddingStore.addAll(embeddings, context);

        // Obtendo mensagem do usuário
        var scanner = new Scanner(in);
        out.println("Escreva sua mensagem: ");
        var message = scanner.nextLine();

        // Transformando a mensagem do usuário em vetor e buscando contexto mais próximo
        var embed = embeddingModel.embed(message).content();
        var embedSearch = EmbeddingSearchRequest.builder()
                .queryEmbedding(embed)
                .minScore(0.7)
                .maxResults(1)
                .build();
        var embedMatches = embeddingStore.search(embedSearch).matches().stream()
                            .map(EmbeddingMatch::embedded)
                            .map(TextSegment::text)
                            .findAny().orElse("Nenhum contexto encontrado.");

        // Compondo a mensagem do usuário com o contexto
        var prompt = String.format("""
                Usuário: %s
                Responda SOMENTE baseado no contexto: %s
                """, message, embedMatches);

        out.println("\n=== PROMPT ===");
        out.println(prompt);

        // Obtendo resposta do LLM
        var chatModel = new ChatModelService();
        var response = chatModel.generate(prompt);
        out.println("\n=== RESPOSTA LLM ===");
        out.println(response);
    }
}
