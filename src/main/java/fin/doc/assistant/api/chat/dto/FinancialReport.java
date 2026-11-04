package fin.doc.assistant.api.chat.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record FinancialReport(

        @JsonPropertyDescription("Тип операции: INCOME, EXPENSE или UNKNOWN")
        OperationType type,

        @JsonPropertyDescription("Сумма числом, без символа валюты. null если не указана")
        @Nullable
        BigDecimal amount,

        @JsonPropertyDescription("Код валюты ISO 4217: BYN, USD, EUR. null если не указана")
        @Nullable
        String currency,

        @JsonPropertyDescription("Контрагент или название компании. null если не указан")
        @Nullable
        String counterparty,

        @JsonPropertyDescription("Период или дата в формате YYYY-MM-DD. null если не указан")
        @Nullable
        String period,

        @JsonPropertyDescription("Уверенность извлечения от 0.0 до 1.0")
        @Nullable
        Double confidence
) {
    public enum OperationType { INCOME, EXPENSE, UNKNOWN }
}
