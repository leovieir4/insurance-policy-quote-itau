package validators;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidAssistanceException;
import itau.cotacao.seguro.validators.impl.AssistancesValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AssistancesValidationStrategyTest {

    private final AssistancesValidationStrategy assistancesValidationStrategy = new AssistancesValidationStrategy();

    @Test
    void validate_validAssistances_shouldNotThrowException() {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        insurancePolicy.setAssistances(List.of("Assistência 1", "Assistência 2"));

        OfferResponse offerResponse = new OfferResponse();
        offerResponse.setAssistances(List.of("Assistência 1", "Assistência 2", "Assistência 3"));

        assertDoesNotThrow(() -> assistancesValidationStrategy.validate(insurancePolicy, null, offerResponse));
    }

    @Test
    void validate_invalidAssistances_shouldThrowInvalidAssistanceException() {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        insurancePolicy.setAssistances(List.of("Assistência 1", "Assistência 4"));

        OfferResponse offerResponse = new OfferResponse();
        offerResponse.setAssistances(List.of("Assistência 1", "Assistência 2", "Assistência 3"));

        assertThrows(InvalidAssistanceException.class,
                () -> assistancesValidationStrategy.validate(insurancePolicy, null, offerResponse));
    }
}