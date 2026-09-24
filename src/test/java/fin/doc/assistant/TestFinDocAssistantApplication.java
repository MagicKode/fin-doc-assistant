package fin.doc.assistant;

import org.springframework.boot.SpringApplication;

public class TestFinDocAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.from(FinDocAssistantApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
