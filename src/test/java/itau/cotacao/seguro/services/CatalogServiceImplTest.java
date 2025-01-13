package itau.cotacao.seguro.services;
import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.exceptions.OfferNotFoundException;
import itau.cotacao.seguro.exceptions.ProductNotFoundException;
import itau.cotacao.seguro.properties.CatalogApiProperties;
import itau.cotacao.seguro.services.impl.CatalogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static itau.cotacao.seguro.services.impl.CatalogServiceImpl.CIRCUIT_BREAKER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceImplTest {

    private static final String CATALOG_HOST = "https://example.com";
    private static final String GET_PRODUCTS_ENDPOINT = "/products";
    private static final String GET_OFFERS_ENDPOINT = "/offers";
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final UUID OFFER_ID = UUID.randomUUID();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CatalogApiProperties catalogApiProperties;

    @Mock
    private CircuitBreakerFactory circuitBreakerFactory;

    @Mock
    private CircuitBreaker circuitBreaker;

    @InjectMocks
    private CatalogServiceImpl catalogoService;

    @Test
    void getProduct_shouldReturnProductResponse() {
        ProductResponse mockProductResponse = new ProductResponse();
        ResponseEntity<ProductResponse> responseEntity = new ResponseEntity<>(mockProductResponse, HttpStatus.OK);
        CatalogApiProperties.Services services = mock(CatalogApiProperties.Services.class);

        when(catalogApiProperties.getHost()).thenReturn(CATALOG_HOST);
        when(catalogApiProperties.getServices()).thenReturn(services);
        when(services.getGetProducts()).thenReturn(GET_PRODUCTS_ENDPOINT);
        when(circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME)).thenReturn(circuitBreaker);
        when(circuitBreaker.run(any(Supplier.class), any(Function.class)))
                .thenAnswer(invocation -> ((Supplier<ProductResponse>) invocation.getArgument(0)).get());
        when(restTemplate.getForEntity(anyString(), eq(ProductResponse.class))).thenReturn(responseEntity);

        ProductResponse productResponse = catalogoService.getProduct(PRODUCT_ID);

        assertEquals(mockProductResponse, productResponse);
    }

    @Test
    void getProduct_notFound_shouldThrowProductNotFoundException() {
        CatalogApiProperties.Services services = mock(CatalogApiProperties.Services.class);
        when(catalogApiProperties.getHost()).thenReturn(CATALOG_HOST);
        when(catalogApiProperties.getServices()).thenReturn(services);
        when(services.getGetProducts()).thenReturn(GET_PRODUCTS_ENDPOINT);
        when(circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME)).thenReturn(circuitBreaker);
        when(circuitBreaker.run(any(Supplier.class), any(Function.class)))
                .thenAnswer(invocation -> ((Supplier<ProductResponse>) invocation.getArgument(0)).get());
        when(restTemplate.getForEntity(anyString(), eq(ProductResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ProductNotFoundException.class, () -> catalogoService.getProduct(PRODUCT_ID));
    }

    @Test
    void getOffer_shouldReturnOfferResponse() {
        OfferResponse mockOfferResponse = new OfferResponse();
        ResponseEntity<OfferResponse> responseEntity = new ResponseEntity<>(mockOfferResponse, HttpStatus.OK);
        CatalogApiProperties.Services services = mock(CatalogApiProperties.Services.class);

        when(catalogApiProperties.getHost()).thenReturn(CATALOG_HOST);
        when(catalogApiProperties.getServices()).thenReturn(services);
        when(services.getGetOffers()).thenReturn(GET_OFFERS_ENDPOINT);
        when(circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME)).thenReturn(circuitBreaker);
        when(circuitBreaker.run(any(Supplier.class), any(Function.class)))
                .thenAnswer(invocation -> ((Supplier<OfferResponse>) invocation.getArgument(0)).get());
        when(restTemplate.getForEntity(anyString(), eq(OfferResponse.class))).thenReturn(responseEntity);

        OfferResponse offerResponse = catalogoService.getOffer(OFFER_ID);

        assertEquals(mockOfferResponse, offerResponse);
    }

    @Test
    void getOffer_notFound_shouldThrowOfferNotFoundException() {
        CatalogApiProperties.Services services = mock(CatalogApiProperties.Services.class);

        when(catalogApiProperties.getHost()).thenReturn(CATALOG_HOST);
        when(catalogApiProperties.getServices()).thenReturn(services);
        when(services.getGetOffers()).thenReturn(GET_OFFERS_ENDPOINT);
        when(circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME)).thenReturn(circuitBreaker);
        when(circuitBreaker.run(any(Supplier.class), any(Function.class)))
                .thenAnswer(invocation -> ((Supplier<OfferResponse>) invocation.getArgument(0)).get());
        when(restTemplate.getForEntity(anyString(), eq(OfferResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(OfferNotFoundException.class, () -> catalogoService.getOffer(OFFER_ID));
    }
}