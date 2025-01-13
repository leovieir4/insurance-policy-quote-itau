package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidCoverageException;
import itau.cotacao.seguro.utils.Messages;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class CoveragesValidationStrategy implements ValidationStrategy {

    @Override
    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        if (!offer.getCoverages().keySet().containsAll(insurancePolicy.getCoverages().keySet())) {
            throw new InvalidCoverageException(Messages.INVALID_COVERAGES);
        }
    }
}