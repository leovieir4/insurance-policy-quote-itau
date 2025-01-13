package itau.cotacao.seguro.usecases;

import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;

public interface GetInsurancePolicyUseCase {
    InsurancePolicyResponse getInsurancePolicy(Long idQuery);
}
