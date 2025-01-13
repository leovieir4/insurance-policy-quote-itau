package itau.cotacao.seguro.validators;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;

public interface ValidationStrategy {
    void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer);
}
