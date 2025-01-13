package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.ProductInactiveException;
import itau.cotacao.seguro.exceptions.ProductNotFoundException;
import itau.cotacao.seguro.utils.Messages;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class ProductValidationStrategy implements ValidationStrategy {

    @Override
    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        if (product == null) {
            throw new ProductNotFoundException(Messages.PRODUCT_NOT_FOUND);
        }

        if (!product.isActive()) {
            throw new ProductInactiveException(String.format(Messages.PRODUCT_INACTIVE, insurancePolicy.getProductId()));
        }
    }
}