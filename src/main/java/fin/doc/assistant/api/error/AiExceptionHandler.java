package fin.doc.assistant.api.error;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AiExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(AiExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public ProblemDetail handleException(Exception ex) {
        log.error("LLM extraction failed: ", ex);
        var pd = ProblemDetail.forStatus(502);
        pd.setTitle("LLM returned malformed response");
        pd.setDetail("Не удалось получить валидный FinancialReport после ретраев. Проверь лог.");
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        // Создаем стандартизированный ответ по RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setTitle("Validation Failed");

        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }
}
