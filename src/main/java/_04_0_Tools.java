import utils.ChatModelService;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class _04_0_Tools {

    public static void main(String[] args) throws Exception {

        var prompt = """
            Responda à pergunta do USUÁRIO.
            Caso necessário acionar uma função definido em FERRAMENTAS para responder o USUÁRIO:
            1. Defina os valores dos parâmetros da função.
            2. Retorne apenas a chamada da função com os parâmetros definidos.
            
            ### USUÁRIO:
            %s

            ### FERRAMENTAS:
            - searchOnWeb(String query): Pesquisa query na Internet
            - somaNumeros(Integer a, Integer b): Soma dois números inteiros
            """;

        var scanner = new Scanner(in);
        out.println("Escreva sua mensagem: ");
        var message = scanner.nextLine();

        var chatModel = new ChatModelService();
        var response = chatModel.generate(String.format(prompt, message));
        out.println(response);
    }
}
