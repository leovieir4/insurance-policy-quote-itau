package itau.cotacao.seguro.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.exceptions.InvalidSendMessageException;
import itau.cotacao.seguro.properties.SqsProperties;
import itau.cotacao.seguro.services.impl.InsurancePolicyProducerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyProducerImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyProducerImplTest.class);

    @Mock
    private SqsProperties sqsProperties;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private InsurancePolicyProducerImpl insurancePolicyProducer;

    @Test
    void send_validMessage_shouldSendSuccessfully() throws Exception {
        InsurancePolicyResponse response = new InsurancePolicyResponse();
        String messageBody = "{\"policyNumber\": \"12345678\"}";
        SendMessageResponse sendMessageResponse = (SendMessageResponse) SendMessageResponse.builder().sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build()).build();

        SqsProperties.Queues queues = mock(SqsProperties.Queues.class);
        SqsProperties.InsurenceQuoteReceived insurenceQuoteReceived = mock(SqsProperties.InsurenceQuoteReceived.class);
        when(sqsProperties.getQueues()).thenReturn(queues);
        when(queues.getInsurenceQuoteReceived()).thenReturn(insurenceQuoteReceived);
        when(insurenceQuoteReceived.getUrl()).thenReturn("https://sqs.us-east-2.amazonaws.com/teste/insurance-policy-created");
        when(insurenceQuoteReceived.getGroup()).thenReturn("quote");

        when(objectMapper.writeValueAsString(response)).thenReturn(messageBody);
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class))).thenReturn(CompletableFuture.completedFuture(sendMessageResponse));

        assertDoesNotThrow(() -> insurancePolicyProducer.send(response));

        verify(sqsAsyncClient).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void send_errorOnSendMessage_shouldThrowInvalidSendMessageException() throws Exception {
        InsurancePolicyResponse response = new InsurancePolicyResponse();
        String messageBody = "{\"policyNumber\": \"12345678\"}";

        SqsProperties.Queues queues = mock(SqsProperties.Queues.class);
        SqsProperties.InsurenceQuoteReceived insurenceQuoteReceived = mock(SqsProperties.InsurenceQuoteReceived.class);
        when(sqsProperties.getQueues()).thenReturn(queues);
        when(queues.getInsurenceQuoteReceived()).thenReturn(insurenceQuoteReceived);
        when(insurenceQuoteReceived.getUrl()).thenReturn("https://sqs.us-east-2.amazonaws.com/teste/insurance-policy-created");
        when(insurenceQuoteReceived.getGroup()).thenReturn("quote");

        when(objectMapper.writeValueAsString(response)).thenReturn(messageBody);
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class))).thenThrow(RuntimeException.class);

        assertThrows(InvalidSendMessageException.class, () -> insurancePolicyProducer.send(response));
    }
}