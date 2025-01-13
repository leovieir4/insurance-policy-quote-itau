package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InsurancePolicyValidator {

    private final List<ValidationStrategy> strategies;

    public InsurancePolicyValidator(List<ValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        strategies.forEach(strategy -> strategy.validate(insurancePolicy, product, offer));
    }
}