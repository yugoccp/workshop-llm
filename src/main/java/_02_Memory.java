import org.apache.commons.lang3.StringUtils;
import utils.ChatModelService;

import java.util.ArrayList;

import static java.lang.System.out;

public class _02_Memory {

    public static void main(String[] args) {
        var chatModel = new ChatModelService();
        withoutMemory(chatModel);
        withMemory(chatModel);
    }

    static void withoutMemory(ChatModelService chatModel) {
        out.println("\n\n=== EXEMPLO SEM MEMORIA ===\n");

        var prompt1 = "Meu nome é John Doe!";
        out.println(prompt1);

        var response1 = chatModel.generate(prompt1);
        out.println(response1);

        var prompt2 = "Qual o meu nome?";
        out.println(prompt2);

        var response2 = chatModel.generate(prompt2);
        out.println(response2);
    }

    static void withMemory(ChatModelService chatModel) {
        out.println("\n\n=== EXEMPLO COM MEMORIA ===\n");

        var chatMemory = new ArrayList<String>();

        var prompt1 = "Meu nome é John Doe!";
        out.println(prompt1);
        chatMemory.add(prompt1);

        var response1 = chatModel.generate(StringUtils.join(chatMemory, "\n"));
        out.println(response1);
        chatMemory.add(response1);

        var prompt2 = "Qual o meu nome?";
        out.println(prompt2);
        chatMemory.add(prompt2);

        var response2 = chatModel.generate(StringUtils.join(chatMemory, "\n"));
        out.println(response2);
    }
}
