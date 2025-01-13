package itau.cotacao.seguro.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itau.cotacao.seguro.consumers.impl.InsurancePolicyConsumerImpl;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyListenerRequest;
import itau.cotacao.seguro.properties.SqsProperties;
import itau.cotacao.seguro.usecases.ProcessInsurancePolicyResponseUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InsurancePolicyConsumerImplTest {
    @Mock
    private SqsProperties sqsProperties;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProcessInsurancePolicyResponseUseCase processInsurancePolicyResponseUseCase;

    @InjectMocks
    private InsurancePolicyConsumerImpl insurancePolicyConsumer;

    @Test
    void receiveMessage_validMessage_shouldProcessSuccessfully() throws JsonProcessingException {
        String message = "{\"id\": \"123\", \"insurancePolicyId\": 456}";
        InsurancePolicyListenerRequest request = new InsurancePolicyListenerRequest("123", 456L);

        SqsProperties.Queues queues = mock(SqsProperties.Queues.class);
        SqsProperties.InsurenceQuoteCreated insurenceQuoteCreated = mock(SqsProperties.InsurenceQuoteCreated.class);

        when(objectMapper.readValue(message, InsurancePolicyListenerRequest.class)).thenReturn(request);

        insurancePolicyConsumer.receiveMessage(message);

        verify(processInsurancePolicyResponseUseCase).process(request);
    }

    @Test
    void receiveMessage_invalidMessage_shouldThrowJsonProcessingException() throws JsonProcessingException {
        String message = "mensagem inválida";

        SqsProperties.Queues queues = mock(SqsProperties.Queues.class);
        SqsProperties.InsurenceQuoteCreated insurenceQuoteCreated = mock(SqsProperties.InsurenceQuoteCreated.class);

        when(objectMapper.readValue(message, InsurancePolicyListenerRequest.class)).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> insurancePolicyConsumer.receiveMessage(message));

        verify(processInsurancePolicyResponseUseCase, never()).process(any());
    }
}
