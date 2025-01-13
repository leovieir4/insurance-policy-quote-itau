package itau.cotacao.seguro.services;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;

import java.util.UUID;

public interface CatalogService {
    ProductResponse getProduct(UUID productId);
    OfferResponse getOffer(UUID offerId);
}
