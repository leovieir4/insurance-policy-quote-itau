package itau.cotacao.seguro.services.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.exceptions.InvalidSendMessageException;
import itau.cotacao.seguro.properties.SqsProperties;
import itau.cotacao.seguro.services.InsurancePolicyProducer;
import itau.cotacao.seguro.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.UUID;


@Service
public class InsurancePolicyProducerImpl implements InsurancePolicyProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyProducerImpl.class);

    private final SqsProperties sqsProperties;
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    public InsurancePolicyProducerImpl(SqsProperties sqsProperties, SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
        this.sqsProperties = sqsProperties;
        this.sqsAsyncClient = sqsAsyncClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(InsurancePolicyResponse insurancePolicyResponse) {
        try {
            LOGGER.info("Enviando mensagem para a fila SQS: {}", insurancePolicyResponse);

            String messageBody = objectMapper.writeValueAsString(insurancePolicyResponse);
            SendMessageRequest sendMessageRequest = buildSendMessageRequest(messageBody);
            SendMessageResponse response = sqsAsyncClient.sendMessage(sendMessageRequest).get();

            if (response.sdkHttpResponse().isSuccessful()) {
                logSuccessfulMessage(response);
            } else {
                logErrorMessage(response);
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao enviar mensagem para a fila SQS", e);
            throw new InvalidSendMessageException(String.format(Messages.SEND_MENSAGE_ERROR, e.getMessage()));
        }
    }

    private SendMessageRequest buildSendMessageRequest(String messageBody) {
        SqsProperties.InsurenceQuoteReceived queueProperties = sqsProperties.getQueues().getInsurenceQuoteReceived();
        return SendMessageRequest.builder()
                .queueUrl(queueProperties.getUrl())
                .messageBody(messageBody)
                .messageDeduplicationId(generateDeduplicationId())
                .messageGroupId(queueProperties.getGroup())
                .build();
    }

    private String generateDeduplicationId() {
        return UUID.randomUUID().toString();
    }

    private void logSuccessfulMessage(SendMessageResponse response) {
        String queueUrl = sqsProperties.getQueues().getInsurenceQuoteReceived().getUrl();
        LOGGER.info("Mensagem enviada com sucesso para a fila SQS: {}", queueUrl);
        LOGGER.info("MessageId: {}", response.messageId());
    }

    private void logErrorMessage(SendMessageResponse response) {
        LOGGER.error("Erro ao enviar mensagem para a fila SQS: {}", response.sdkHttpResponse().statusCode());
    }
}