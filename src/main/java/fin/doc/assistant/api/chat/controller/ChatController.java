package fin.doc.assistant.api.chat.controller;

import fin.doc.assistant.api.chat.dto.FinancialReport;
import fin.doc.assistant.api.service.DocumentIngestionService;
import fin.doc.assistant.api.service.impl.FinancialExtractionServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final FinancialExtractionServiceImpl financialExtractionServiceImpl;
    private final DocumentIngestionService documentIngestionService;

    public ChatController(FinancialExtractionServiceImpl financialExtractionServiceImpl, DocumentIngestionService documentIngestionService) {
        this.financialExtractionServiceImpl = financialExtractionServiceImpl;
        this.documentIngestionService = documentIngestionService;
    }

    @GetMapping("/chat")
    public CompletableFuture<FinancialReport> chat(@RequestParam @NotBlank(message = "Сообщение не должно быть пустым") String message) {
        return financialExtractionServiceImpl.extractAsync(message);
    }

    @PostMapping("/ingest")
    public String ingestDocument(@RequestParam String fileName) {
        documentIngestionService.ingestTextFile(fileName);
        return "Документ успешно загружен и нарезан";
    }
}
