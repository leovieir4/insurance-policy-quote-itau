package itau.cotacao.seguro.usecases;

import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;

public interface CreateInsurancePolicyUseCase {
    InsurancePolicyResponse create(InsurancePolicy insurancePolicy);
}