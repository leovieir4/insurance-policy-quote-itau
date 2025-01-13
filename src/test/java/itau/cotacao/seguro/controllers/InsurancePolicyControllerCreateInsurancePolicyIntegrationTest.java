package itau.cotacao.seguro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itau.cotacao.seguro.api.catalog.MonthlyPremiumAmount;
import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.config.JacksonConfig;
import itau.cotacao.seguro.config.ModelMapperConfig;
import itau.cotacao.seguro.configs.Definictions;
import itau.cotacao.seguro.controllers.dto.request.CustomerRequest;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyRequest;
import itau.cotacao.seguro.services.CatalogService;
import itau.cotacao.seguro.usecases.CreateInsurancePolicyUseCase;
import itau.cotacao.seguro.usecases.GetInsurancePolicyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ModelMapperConfig.class, Definictions.class, JacksonConfig.class})
@ActiveProfiles("test")
public class InsurancePolicyControllerCreateInsurancePolicyIntegrationTest {

    private LocalStackContainer localstack;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    @Autowired
    private GetInsurancePolicyUseCase getInsurancePolicyUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private InsurancePolicyController insurancePolicyController;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @BeforeEach
    void setUp() {
        localstack = new LocalStackContainer("latest")
                .withServices(LocalStackContainer.Service.SQS);
        localstack.start();

        String sqsEndpoint = localstack.getEndpointOverride(LocalStackContainer.Service.SQS).toString();
        System.out.println("SQS endpoint: " + sqsEndpoint);

        sqsAsyncClient = SqsAsyncClient.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.SQS))
                .region(Region.US_EAST_2)
                .build();

        SendMessageResponse sendMessageResponse = SendMessageResponse.builder()
                .messageId("12345")
                .build();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setActive(true);
        productResponse.setOffers(List.of(UUID.fromString("adc56d77-348c-4bf0-908f-22d402ee715c")));
        OfferResponse offerResponse = getOfferResponse();
        when(catalogService.getProduct(any())).thenReturn(productResponse);
        when(catalogService.getOffer(any())).thenReturn(offerResponse);
    }

    @AfterEach
    void tearDown() {
        if (localstack != null) {
            localstack.stop();
        }
    }

    @Test
    void createInsurancePolicy_validRequest_shouldReturnCreated() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest();
        request.setProductId(UUID.fromString("1b2da7cc-b367-4196-8a78-9cfeec21f587"));
        request.setOfferId(UUID.fromString("adc56d77-348c-4bf0-908f-22d402ee715c"));
        request.setCategory("HOME");
        request.setTotalMonthlyPremiumAmount(BigDecimal.valueOf(75.25));
        request.setTotalCoverageAmount(BigDecimal.valueOf(825000.00));

        Map<String, BigDecimal> requestCoverages = new HashMap<>();
        requestCoverages.put("Incêndio", BigDecimal.valueOf(250000.00));
        requestCoverages.put("Desastres naturais", BigDecimal.valueOf(500000.00));
        requestCoverages.put("Responsabiliadade civil", BigDecimal.valueOf(75000.00));
        request.setCoverages(requestCoverages);

        request.setAssistances(List.of("Encanador", "Eletricista", "Chaveiro 24h"));

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setDocumentNumber("36205578900");
        customerRequest.setName("John Wick");
        customerRequest.setType("NATURAL");
        customerRequest.setGender("MALE");
        customerRequest.setDateOfBirth(LocalDate.parse("1973-05-02"));
        customerRequest.setEmail("johnwick@gmail.com");
        customerRequest.setPhoneNumber(11950503030L);
        request.setCustomer(customerRequest);

        mockMvc.perform(post("/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());  // Registra o módulo
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // Evita timestamp
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OfferResponse getOfferResponse() {
        OfferResponse offerResponse = new OfferResponse();
        offerResponse.setActive(true);
        MonthlyPremiumAmount monthlyPremiumAmount = new MonthlyPremiumAmount();
        monthlyPremiumAmount.setMinAmount(BigDecimal.valueOf(50));
        monthlyPremiumAmount.setMaxAmount(BigDecimal.valueOf(100));
        offerResponse.setMonthlyPremiumAmount(monthlyPremiumAmount);

        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Incêndio", BigDecimal.valueOf(250000));
        coverages.put("Desastres naturais", BigDecimal.valueOf(500000));
        coverages.put("Responsabiliadade civil", BigDecimal.valueOf(75000));
        offerResponse.setCoverages(coverages);

        offerResponse.setAssistances(List.of("Encanador", "Eletricista", "Chaveiro 24h"));
        return offerResponse;
    }
}
