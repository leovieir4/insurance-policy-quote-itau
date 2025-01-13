package itau.cotacao.seguro.consumers.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import itau.cotacao.seguro.consumers.InsurancePolicyConsumer;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyListenerRequest;
import itau.cotacao.seguro.properties.SqsProperties;
import itau.cotacao.seguro.usecases.ProcessInsurancePolicyResponseUseCase;
import org.springframework.stereotype.Component;


@Component
public class InsurancePolicyConsumerImpl implements InsurancePolicyConsumer {

    private final SqsProperties sqsProperties;
    private final ObjectMapper objectMapper;
    private final ProcessInsurancePolicyResponseUseCase processInsurancePolicyResponseUseCase;


    public InsurancePolicyConsumerImpl(SqsProperties sqsProperties, ObjectMapper objectMapper,
                                       ProcessInsurancePolicyResponseUseCase processInsurancePolicyResponseUseCase) {
        this.sqsProperties = sqsProperties;
        this.objectMapper = objectMapper;
        this.processInsurancePolicyResponseUseCase = processInsurancePolicyResponseUseCase;
    }

    @Override
    @SqsListener(value = "#{@sqsProperties.getQueues().getInsurenceQuoteCreated().getName()}")
    public void receiveMessage(String message) throws JsonProcessingException {
        InsurancePolicyListenerRequest insurancePolicyListenerRequest =
                objectMapper.readValue(message, InsurancePolicyListenerRequest.class);

        processInsurancePolicyResponseUseCase.process(insurancePolicyListenerRequest);
    }
}
