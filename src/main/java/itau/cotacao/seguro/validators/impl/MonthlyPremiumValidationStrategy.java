package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidPremiumValueException;
import itau.cotacao.seguro.utils.Messages;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MonthlyPremiumValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        BigDecimal minPremium = offer.getMonthlyPremiumAmount().getMinAmount();
        BigDecimal maxPremium = offer.getMonthlyPremiumAmount().getMaxAmount();
        BigDecimal premium = insurancePolicy.getTotalMonthlyPremiumAmount();

        if (premium.compareTo(minPremium) < 0 || premium.compareTo(maxPremium) > 0) {
            throw new InvalidPremiumValueException(String.format(
                    Messages.INVALID_PREMIUM_VALUE, minPremium, maxPremium));
        }
    }
}