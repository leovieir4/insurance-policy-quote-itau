package itau.cotacao.seguro.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack:2.1.0");

    @Bean
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(LocalStackContainer.Service.SQS);
    }
}