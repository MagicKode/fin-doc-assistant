package fin.doc.assistant.api.service;

import fin.doc.assistant.api.chat.dto.FinancialReport;

import java.util.concurrent.CompletableFuture;

public interface FinancialExtractionService {
    CompletableFuture<FinancialReport> extractAsync(String message);
}
