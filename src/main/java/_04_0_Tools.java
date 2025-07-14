import utils.ChatModelService;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class _04_0_Tools {

    public static void main(String[] args) throws Exception {

        var promptTemplate = """
            Responda à pergunta do USER.
            Caso necessário acionar uma função definido em TOOLS para responder o USER:
            1. Defina os valores dos parâmetros da função.
            2. Retorne SOMENTE a chamada da função com os parâmetros definidos.
            
            <USER>
            %s
            </USER>

            <TOOLS>
            - searchOnWeb(String query): Pesquisa na Internet pela query fornecida
            - addNumbers(Integer a, Integer b): Soma dois números inteiros
            </TOOLS>
            """;

        out.println("=== TOOLS EXAMPLE ===");
        out.println("\nEscreva sua mensagem: ");
        var scanner = new Scanner(in);
        var message = scanner.nextLine();
        scanner.close();

        var chatModel = new ChatModelService();
        var prompt = String.format(promptTemplate, message);
        out.println("\n\nPROMPT:");
        out.println(prompt);

        var response = chatModel.generate(prompt);
        out.println("\n\nRESPOSTA:");
        out.println(response);
    }
}
