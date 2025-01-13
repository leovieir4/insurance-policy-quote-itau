package itau.cotacao.seguro.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface InsurancePolicyConsumer {
    void receiveMessage(String message) throws JsonProcessingException;
}
