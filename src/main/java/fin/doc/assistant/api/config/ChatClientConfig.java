package fin.doc.assistant.api.config;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class ChatClientConfig {

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return builder -> {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            // Даем 10 секунд на установку соединения (важно для VPN)
            factory.setConnectTimeout(10_000);
            // Даем 60 секунд на чтение ответа (Groq + VPN могут тормозить)
            factory.setReadTimeout(60_000);
            builder.requestFactory(factory);
        };
    }
}
