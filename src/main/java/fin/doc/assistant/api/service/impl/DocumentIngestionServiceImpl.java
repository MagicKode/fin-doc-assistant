package fin.doc.assistant.api.service.impl;

import fin.doc.assistant.api.service.DocumentIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionServiceImpl implements DocumentIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngestionServiceImpl.class);
    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    public DocumentIngestionServiceImpl(VectorStore vectorStore, ResourceLoader resourceLoader) {
        this.vectorStore = vectorStore;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Загружает текстовый файл, режет его на куски (chunks) и сохраняет в векторную БД.
     */
    @Override
    public void ingestTextFile(String filePath) {
        log.info("начинаем загрузку файла: {}", filePath);

        //1.Читаем файл
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        //2. Нарезаем на куски (Сргтлштп)
        //   // TokenTextSplitter режет текст по токенам (примерно 800 токенов на кусок)
        // Это нужно, чтобы влезть в контекстное окно модели и сохранить смысл.
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(50)
                .build();
        List<Document> splitDocuments = splitter.split(documents);

        log.info("Файл разбит на {} чанков. Сохраняем в pgvector...", splitDocuments.size());

        //3. Сохраняем в PostgreSQL (Spring AI автоматически сделает эибеддинги через Groq)
        vectorStore.add(splitDocuments);

        log.info("Сохранение завершено успешно!");
    }
}
