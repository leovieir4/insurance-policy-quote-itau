package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidTotalCoverageAmountException;
import itau.cotacao.seguro.utils.Messages;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TotalCoverageAmountValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        BigDecimal totalCoverageAmount = insurancePolicy.getTotalCoverageAmount();
        BigDecimal sumOfCoverages = insurancePolicy.getCoverages().values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!totalCoverageAmount.equals(sumOfCoverages)) {
            throw new InvalidTotalCoverageAmountException(Messages.INVALID_TOTAL_COVERAGE_AMOUNT);
        }
    }
}