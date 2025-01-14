package itau.cotacao.seguro.validators;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.OfferInactiveException;
import itau.cotacao.seguro.exceptions.OfferNotFoundException;
import itau.cotacao.seguro.validators.impl.OfferValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OfferValidationStrategyTest {

    private final OfferValidationStrategy strategy = new OfferValidationStrategy();

    @Test
    void validate_validOffer_shouldNotThrowException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setOfferId(UUID.randomUUID());

        OfferResponse offer = new OfferResponse();
        offer.setActive(true);

        assertDoesNotThrow(() -> strategy.validate(policy, null, offer));
    }

    @Test
    void validate_offerNotFound_shouldThrowOfferNotFoundException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setOfferId(UUID.randomUUID());

        assertThrows(OfferNotFoundException.class, () -> strategy.validate(policy, null, null));
    }

    @Test
    void validate_inactiveOffer_shouldThrowOfferInactiveException() {
        InsurancePolicy policy = new InsurancePolicy();
        UUID offerId = UUID.randomUUID();
        policy.setOfferId(offerId);

        OfferResponse offer = new OfferResponse();
        offer.setActive(false);

        assertThrows(OfferInactiveException.class, () -> strategy.validate(policy, null, offer));
    }
}