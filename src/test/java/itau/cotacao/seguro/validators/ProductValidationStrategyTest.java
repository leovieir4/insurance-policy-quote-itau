package itau.cotacao.seguro.validators;


import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.ProductInactiveException;
import itau.cotacao.seguro.exceptions.ProductNotFoundException;
import itau.cotacao.seguro.validators.impl.ProductValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductValidationStrategyTest {

    private final ProductValidationStrategy strategy = new ProductValidationStrategy();

    @Test
    void validate_validProduct_shouldNotThrowException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setProductId(UUID.randomUUID());

        ProductResponse product = new ProductResponse();
        product.setActive(true);

        assertDoesNotThrow(() -> strategy.validate(policy, product, null));
    }

    @Test
    void validate_productNotFound_shouldThrowProductNotFoundException() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setProductId(UUID.randomUUID());

        assertThrows(ProductNotFoundException.class, () -> strategy.validate(policy, null, null));
    }

    @Test
    void validate_inactiveProduct_shouldThrowProductInactiveException() {
        InsurancePolicy policy = new InsurancePolicy();
        UUID productId = UUID.randomUUID();
        policy.setProductId(productId);

        ProductResponse product = new ProductResponse();
        product.setActive(false);

        assertThrows(ProductInactiveException.class, () -> strategy.validate(policy, product, null));
    }
}