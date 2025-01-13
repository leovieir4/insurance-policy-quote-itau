package validators;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidCoverageException;
import itau.cotacao.seguro.validators.impl.CoveragesValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CoveragesValidationStrategyTest {

    private final CoveragesValidationStrategy coveragesValidationStrategy = new CoveragesValidationStrategy();

    @Test
    void validate_validCoverages_shouldNotThrowException() {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Incêndio", BigDecimal.valueOf(100000));
        coverages.put("Roubo", BigDecimal.valueOf(50000));
        insurancePolicy.setCoverages(coverages);

        OfferResponse offerResponse = new OfferResponse();
        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("Incêndio", BigDecimal.valueOf(200000));
        offerCoverages.put("Roubo", BigDecimal.valueOf(100000));
        offerCoverages.put("Danos elétricos", BigDecimal.valueOf(75000));
        offerResponse.setCoverages(offerCoverages);

        assertDoesNotThrow(() -> coveragesValidationStrategy.validate(insurancePolicy, null, offerResponse));
    }

    @Test
    void validate_invalidCoverages_shouldThrowInvalidCoverageException() {
        InsurancePolicy insurancePolicy = new InsurancePolicy();
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Incêndio", BigDecimal.valueOf(100000));
        coverages.put("Danos por água", BigDecimal.valueOf(50000)); // Cobertura não presente na oferta
        insurancePolicy.setCoverages(coverages);

        OfferResponse offerResponse = new OfferResponse();
        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("Incêndio", BigDecimal.valueOf(200000));
        offerCoverages.put("Roubo", BigDecimal.valueOf(100000));
        offerCoverages.put("Danos elétricos", BigDecimal.valueOf(75000));
        offerResponse.setCoverages(offerCoverages);

        assertThrows(InvalidCoverageException.class,
                () -> coveragesValidationStrategy.validate(insurancePolicy, null, offerResponse));
    }
}