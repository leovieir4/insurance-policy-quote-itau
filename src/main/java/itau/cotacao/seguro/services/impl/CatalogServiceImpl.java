package itau.cotacao.seguro.services.impl;
import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.exceptions.CatalogApiUnavailableException;
import itau.cotacao.seguro.exceptions.OfferNotFoundException;
import itau.cotacao.seguro.exceptions.ProductNotFoundException;
import itau.cotacao.seguro.properties.CatalogApiProperties;
import itau.cotacao.seguro.services.CatalogService;
import itau.cotacao.seguro.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class CatalogServiceImpl implements CatalogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);

    public static final String CIRCUIT_BREAKER_NAME = "catalog";

    private final RestTemplate restTemplate;
    private final CatalogApiProperties catalogApiProperties;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public CatalogServiceImpl(RestTemplate restTemplate, CatalogApiProperties catalogApiProperties,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.catalogApiProperties = catalogApiProperties;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public ProductResponse getProduct(UUID productId) {
        LOGGER.info("Consultando produto com ID {} na API do Catálogo", productId);
        return getProductWithCircuitBreaker(productId);
    }

    private ProductResponse getProductWithCircuitBreaker(UUID productId) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
        return circuitBreaker.run(getProductSupplier(productId), this::handleGetProductFailure);
    }

    private Supplier<ProductResponse> getProductSupplier(UUID productId) {
        return () -> {
            String url = buildProductUrl(productId);
            LOGGER.debug("URL da requisição para o produto: {}", url);
            try {
                ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
                HttpStatusCode statusCode = response.getStatusCode();
                LOGGER.info("Resposta da API do Catálogo para o produto {}: {}", productId, statusCode);
                return response.getBody();
            } catch (HttpClientErrorException ex) {
                if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                    LOGGER.warn("Produto com ID {} não encontrado na API do Catálogo", productId);
                    throw new ProductNotFoundException(Messages.PRODUCT_NOT_FOUND);
                } else {
                    LOGGER.error("Erro ao consultar produto na API do Catálogo", ex);
                    throw ex;
                }
            }
        };
    }

    private String buildProductUrl(UUID productId) {
        return UriComponentsBuilder.fromHttpUrl(catalogApiProperties.getHost())
                .path(catalogApiProperties.getServices().getGetProducts())
                .pathSegment(productId.toString())
                .toUriString();
    }

    private ProductResponse handleGetProductFailure(Throwable throwable) {
        LOGGER.error("Falha na chamada da API do Catálogo para o produto", throwable);
        throw new CatalogApiUnavailableException(Messages.CATALOG_API_INTERNAL_SERVER_ERRO);
    }

    @Override
    public OfferResponse getOffer(UUID offerId) {
        LOGGER.info("Consultando oferta com ID {} na API do Catálogo", offerId);
        return getOfferWithCircuitBreaker(offerId);
    }

    private OfferResponse getOfferWithCircuitBreaker(UUID offerId) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
        return circuitBreaker.run(getOfferSupplier(offerId), this::handleGetOfferFailure);
    }


    private Supplier<OfferResponse> getOfferSupplier(UUID offerId) {
        return () -> {
            String url = buildOfferUrl(offerId);
            LOGGER.debug("URL da requisição para a oferta: {}", url);
            try {
                ResponseEntity<OfferResponse> response = restTemplate.getForEntity(url, OfferResponse.class);
                HttpStatusCode statusCode = response.getStatusCode();
                LOGGER.info("Resposta da API do Catálogo para a oferta {}: {}", offerId, statusCode);
                return response.getBody();
            } catch (HttpClientErrorException ex) {
                if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                    LOGGER.warn("Oferta com ID {} não encontrada na API do Catálogo", offerId);
                    throw new OfferNotFoundException(Messages.OFFER_NOT_FOUND);
                } else {
                    LOGGER.error("Erro ao consultar oferta na API do Catálogo", ex);
                    throw ex;
                }
            }
        };
    }

    private String buildOfferUrl(UUID offerId) {
        return UriComponentsBuilder.fromHttpUrl(catalogApiProperties.getHost())
                .path(catalogApiProperties.getServices().getGetOffers())
                .pathSegment(offerId.toString())
                .toUriString();
    }

    private OfferResponse handleGetOfferFailure(Throwable throwable) {
        LOGGER.error("Falha na chamada da API do Catálogo para a oferta", throwable);
        throw new CatalogApiUnavailableException(Messages.CATALOG_API_INTERNAL_SERVER_ERRO);
    }
}