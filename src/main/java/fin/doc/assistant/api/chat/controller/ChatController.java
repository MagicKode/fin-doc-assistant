package fin.doc.assistant.api.chat.controller;

import fin.doc.assistant.api.chat.dto.FinancialReport;
import fin.doc.assistant.api.service.FinancialExtractionService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * Простейший контроллер для проверки работы Spring AI + OpenAI.
 * Это "Hello World" для LLM-инженерии: мы отправляем вопрос,
 * получаем ответ от GPT-4o-mini.
 */

@RestController
@RequestMapping("/api/v1")
@Validated
public class ChatController {
    private final FinancialExtractionService financialExtractionService;

    public ChatController(FinancialExtractionService financialExtractionService) {
        this.financialExtractionService = financialExtractionService;
    }

    @GetMapping("/chat")
    public CompletableFuture<FinancialReport> chat(@RequestParam @NotBlank(message = "Сообщение не должно быть пустым") String message) {
        return financialExtractionService.extractAsync(message);
    }
}
