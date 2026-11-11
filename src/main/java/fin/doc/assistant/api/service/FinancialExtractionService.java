package fin.doc.assistant.api.service;

import fin.doc.assistant.api.chat.dto.FinancialReport;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FinancialExtractionService {
    private static final Logger log = LoggerFactory.getLogger(FinancialExtractionService.class);
    private final ChatClient chatClient;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public FinancialExtractionService(ChatClient.Builder clientBuilder) {
        this.chatClient = clientBuilder.build();
    }

    // 1. CircuitBreaker: если метод падает, размыкает цепь и вызывает fallback
    // 2. TimeLimiter: следит, чтобы CompletableFuture завершился за 15 секунд
    // 3. Retry: если вылетела сетевая ошибка, повторяет вызов
    @CircuitBreaker(name = "limCircuitBreaker", fallbackMethod = "fallback")
    @Retry(name = "limRetry")
//    @TimeLimiter(name = "limTimeLimiter")
    public CompletableFuture<FinancialReport> extractAsync(String message) {
        // Оборачиваем синхронный вызов Spring AI в асинхронный CompletableFuture
        return CompletableFuture.supplyAsync(() -> {
            FinancialReport report = chatClient.prompt()
                    .system("Ты — ассистент по финансовым документам. Извлеки данные из текста пользователя. " +
                            "Если какого-то поля нет в тексте, верни null. Не выдумывай данные. " +
                            "Верни ответ СТРОГО в формате JSON без лишнего текста.")
                    .user(message)
                    .call()
                    .entity(FinancialReport.class);

            return report;
        }, executorService);
    }

    // Fallback-метод: срабатывает, если все 3 попытки провалились или сработал CircuitBreaker.
    // Он должен принимать те же аргументы, что и основной метод, плюс Throwable (исключение).
    public CompletableFuture<FinancialReport> fallback(String message, Throwable ex) {
        System.err.println("⚠️ Fallback сработал! Причина: " + ex.getMessage());

        //Возвращаем безопасный пустой объект, чтобы сервис не упал с 500-й ошибкой
        return CompletableFuture.completedFuture(
                new FinancialReport(
                        FinancialReport.OperationType.UNKNOWN,
                        null, null, null, null, 0.0
                )
        );
    }
}
