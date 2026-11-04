package fin.doc.assistant.api.service;

import fin.doc.assistant.api.chat.dto.FinancialReport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FinancialExtractionService {
    private final ChatClient chatClient;

    public FinancialExtractionService(ChatClient.Builder clientBuilder) {
        this.chatClient = clientBuilder.build();
    }

    public FinancialReport extract(String message) {
        return chatClient.prompt()
                .system("Ты — ассистент по финансовым документам. Извлеки данные из текста пользователя. " +
                        "Если какого-то поля нет в тексте, верни null. Не выдумывай данные.")
                .user(message)
                .call()
                .entity(FinancialReport.class);
    }
}
