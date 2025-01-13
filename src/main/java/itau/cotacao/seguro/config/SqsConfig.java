package itau.cotacao.seguro.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.MessageListener;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import itau.cotacao.seguro.config.data.UserServiceAws;
import itau.cotacao.seguro.consumers.InsurancePolicyConsumer;
import itau.cotacao.seguro.properties.AwsCredentialsProperties;
import itau.cotacao.seguro.properties.SqsProperties;
import itau.cotacao.seguro.utils.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


import java.time.Duration;

@Configuration
public class SqsConfig {

    private static final int MESSAGE_VISIBILITY_SECONDS = 30;

    private final AwsCredentialsProperties awsCredentialsProperties;
    private final SqsProperties sqsProperties;
    private final InsurancePolicyConsumer insurancePolicyConsumer;
    private final UserServiceAws userServiceAws;

    public SqsConfig(AwsCredentialsProperties awsCredentialsProperties,
                     SqsProperties sqsProperties,
                     InsurancePolicyConsumer insurancePolicyConsumer,
                     UserServiceAws userServiceAws) {
        this.awsCredentialsProperties = awsCredentialsProperties;
        this.sqsProperties = sqsProperties;
        this.insurancePolicyConsumer = insurancePolicyConsumer;
        this.userServiceAws = userServiceAws;
    }

    @Bean
    @Profile({Profiles.LOCAL})
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsCredentialsProperties.getAccessKeyLocal(),
                                awsCredentialsProperties.getSecretKeyLocal()
                        )
                ))
                .region(Region.of(sqsProperties.getRegion()))
                .build();
    }

    @Bean
    @Profile({Profiles.NOT_LOCAL})
    public SqsAsyncClient sqsAsyncClientServer() {
        return SqsAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                userServiceAws.getAccessKey(),
                                userServiceAws.getSecretKey()
                        )
                ))
                .region(Region.of(sqsProperties.getRegion()))
                .build();
    }

    @Bean
    @Profile({Profiles.LOCAL, Profiles.NOT_LOCAL})
    public SqsMessageListenerContainer<String> sqsMessageListenerContainer(
            SqsAsyncClient sqsAsyncClient,
            SqsMessageListenerContainerFactory<String> factory) {

        SqsMessageListenerContainer<String> container = factory.createContainer(
                sqsProperties.getQueues().getInsurenceQuoteCreated().getName()
        );

        container.configure(options ->
                options.messageVisibility(Duration.ofSeconds(MESSAGE_VISIBILITY_SECONDS))
        );

        container.setMessageListener(insurancePolicyMessageListener());

        return container;
    }

    @Bean
    @Profile({Profiles.LOCAL, Profiles.NOT_LOCAL})
    public MessageListener<String> insurancePolicyMessageListener() {
        return message -> {
            try {
                insurancePolicyConsumer.receiveMessage(message.getPayload());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    @Profile({Profiles.LOCAL, Profiles.NOT_LOCAL})
    public SqsMessageListenerContainerFactory<String> sqsMessageListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        SqsMessageListenerContainerFactory<String> factory = new SqsMessageListenerContainerFactory<>();
        factory.setSqsAsyncClient(sqsAsyncClient);
        factory.configure(sqsContainerOptionsBuilder -> {
            sqsContainerOptionsBuilder.acknowledgementMode(AcknowledgementMode.ON_SUCCESS);
        });
        return factory;
    }
}