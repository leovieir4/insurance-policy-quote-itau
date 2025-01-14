package itau.cotacao.seguro.validators;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InvalidTotalCoverageAmountException;

import itau.cotacao.seguro.validators.impl.TotalCoverageAmountValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TotalCoverageAmountValidationStrategyTest {

    private final TotalCoverageAmountValidationStrategy strategy = new TotalCoverageAmountValidationStrategy();

    @Test
    void validate_validTotalCoverageAmount_shouldNotThrowException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setTotalCoverageAmount(BigDecimal.valueOf(250000));
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Incêndio", BigDecimal.valueOf(100000));
        coverages.put("Roubo", BigDecimal.valueOf(150000));
        policy.setCoverages(coverages);

        assertDoesNotThrow(() -> strategy.validate(policy, null, null));
    }

    @Test
    void validate_invalidTotalCoverageAmount_shouldThrowInvalidTotalCoverageAmountException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setTotalCoverageAmount(BigDecimal.valueOf(300000)); // Valor total diferente da soma das coberturas
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Incêndio", BigDecimal.valueOf(100000));
        coverages.put("Roubo", BigDecimal.valueOf(150000));
        policy.setCoverages(coverages);

        assertThrows(InvalidTotalCoverageAmountException.class, () -> strategy.validate(policy, null, null));
    }
}