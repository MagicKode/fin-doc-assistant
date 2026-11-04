package fin.doc.assistant.api.chat.controller;

import fin.doc.assistant.api.chat.dto.FinancialReport;
import fin.doc.assistant.api.service.FinancialExtractionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Простейший контроллер для проверки работы Spring AI + OpenAI.
 * Это "Hello World" для LLM-инженерии: мы отправляем вопрос,
 * получаем ответ от GPT-4o-mini.
 */

@RestController
public class ChatController {
    private final FinancialExtractionService financialExtractionService;

    public ChatController(FinancialExtractionService financialExtractionService) {
        this.financialExtractionService = financialExtractionService;
    }

    @GetMapping("/chat")
    public FinancialReport chat(@RequestParam String message) {
        return financialExtractionService.extract(message);
    }
}
