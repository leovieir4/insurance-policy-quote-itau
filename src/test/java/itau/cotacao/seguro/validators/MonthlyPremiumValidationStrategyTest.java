package itau.cotacao.seguro.validators;

import itau.cotacao.seguro.api.catalog.MonthlyPremiumAmount;
import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidPremiumValueException;

import itau.cotacao.seguro.validators.impl.MonthlyPremiumValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MonthlyPremiumValidationStrategyTest {

    private final MonthlyPremiumValidationStrategy strategy = new MonthlyPremiumValidationStrategy();

    @Test
    void validate_validPremium_shouldNotThrowException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setTotalMonthlyPremiumAmount(BigDecimal.valueOf(75));

        OfferResponse offer = new OfferResponse();
        MonthlyPremiumAmount premiumAmount = new MonthlyPremiumAmount();
        premiumAmount.setMinAmount(BigDecimal.valueOf(50));
        premiumAmount.setMaxAmount(BigDecimal.valueOf(100));
        offer.setMonthlyPremiumAmount(premiumAmount);

        assertDoesNotThrow(() -> strategy.validate(policy, null, offer));
    }

    @Test
    void validate_premiumBelowMinimum_shouldThrowInvalidPremiumValueException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setTotalMonthlyPremiumAmount(BigDecimal.valueOf(45)); // Abaixo do mínimo

        OfferResponse offer = new OfferResponse();
        MonthlyPremiumAmount premiumAmount = new MonthlyPremiumAmount();
        premiumAmount.setMinAmount(BigDecimal.valueOf(50));
        premiumAmount.setMaxAmount(BigDecimal.valueOf(100));
        offer.setMonthlyPremiumAmount(premiumAmount);

        assertThrows(InvalidPremiumValueException.class, () -> strategy.validate(policy, null, offer));
    }

    @Test
    void validate_premiumAboveMaximum_shouldThrowInvalidPremiumValueException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setTotalMonthlyPremiumAmount(BigDecimal.valueOf(105)); // Acima do máximo

        OfferResponse offer = new OfferResponse();
        MonthlyPremiumAmount premiumAmount = new MonthlyPremiumAmount();
        premiumAmount.setMinAmount(BigDecimal.valueOf(50));
        premiumAmount.setMaxAmount(BigDecimal.valueOf(100));
        offer.setMonthlyPremiumAmount(premiumAmount);

        assertThrows(InvalidPremiumValueException.class, () -> strategy.validate(policy, null, offer));
    }
}