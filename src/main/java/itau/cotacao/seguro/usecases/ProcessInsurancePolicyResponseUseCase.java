package itau.cotacao.seguro.usecases;

import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyListenerRequest;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyRequest;

public interface ProcessInsurancePolicyResponseUseCase {
    void process(InsurancePolicyListenerRequest request);
}
