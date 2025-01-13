package itau.cotacao.seguro.validators.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.OfferInactiveException;
import itau.cotacao.seguro.exceptions.OfferNotFoundException;
import itau.cotacao.seguro.utils.Messages;
import itau.cotacao.seguro.validators.ValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class OfferValidationStrategy implements ValidationStrategy {

    @Override
    public void validate(InsurancePolicy insurancePolicy, ProductResponse product, OfferResponse offer) {
        if (offer == null) {
            throw new OfferNotFoundException(Messages.OFFER_NOT_FOUND);
        }

        if (!offer.isActive()) {
            throw new OfferInactiveException(String.format(Messages.OFFER_INACTIVE, insurancePolicy.getOfferId()));
        }
    }
}