package itau.cotacao.seguro.services;

import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;

public interface InsurancePolicyProducer {
    void send(InsurancePolicyResponse insurancePolicyResponse);
}
