package itau.cotacao.seguro.usecases;
import itau.cotacao.seguro.domain.model.Customer;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.services.CatalogService;
import itau.cotacao.seguro.services.InsurancePolicyProducer;
import itau.cotacao.seguro.usecases.impl.CreateInsurancePolicyUseCaseImpl;
import itau.cotacao.seguro.validators.impl.InsurancePolicyValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateInsurancePolicyUseCaseImplTest {

    @Mock
    private CatalogService catalogService;

    @Mock
    private InsurancePolicyValidator validator;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private InsurancePolicyProducer insurancePolicyProducer;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private CreateInsurancePolicyUseCaseImpl createInsurancePolicyUseCase;

    @Test
    void create_validInsurancePolicy_shouldCreateSuccessfully() {
        InsurancePolicy insurancePolicy = new InsurancePolicy();

        ProductResponse productResponse = new ProductResponse();
        OfferResponse offerResponse = new OfferResponse();
        QuoteEntity quoteEntity = new QuoteEntity();
        InsurancePolicyResponse insurancePolicyResponse = new InsurancePolicyResponse();

        Configuration configuration = mock(Configuration.class);
        TypeMap typeMap = mock(TypeMap.class); // Mockar a classe TypeMap
        when(modelMapper.getConfiguration()).thenReturn(configuration);
        when(modelMapper.typeMap(InsurancePolicy.class, QuoteEntity.class)).thenReturn(typeMap); // Retornar o mock de TypeMap
        when(modelMapper.typeMap(QuoteEntity.class, InsurancePolicyResponse.class)).thenReturn(typeMap); // Retornar o mock de TypeMap

        when(catalogService.getProduct(any())).thenReturn(productResponse);
        when(catalogService.getOffer(any())).thenReturn(offerResponse);
        when(modelMapper.map(any(InsurancePolicy.class), eq(QuoteEntity.class))).thenReturn(quoteEntity);
        when(modelMapper.map(any(QuoteEntity.class), eq(InsurancePolicyResponse.class))).thenReturn(insurancePolicyResponse);

        Customer customer = new Customer();
        customer.setDateOfBirth(LocalDate.now());
        customer.setEmail("test");
        customer.setName("test");
        customer.setGender("M");
        customer.setType("test");
        customer.setPhoneNumber(11l);
        customer.setDocumentNumber("123");

        insurancePolicy.setCustomer(customer);
        insurancePolicy.setAssistances(new ArrayList<>());
        insurancePolicy.setCategory("TESTE");
        insurancePolicy.setCoverages(new HashMap<>());
        insurancePolicy.setOfferId(UUID.randomUUID());
        insurancePolicy.setProductId(UUID.randomUUID());
        insurancePolicy.setTotalCoverageAmount(BigDecimal.ONE);
        insurancePolicy.setTotalMonthlyPremiumAmount(BigDecimal.ONE);

        assertDoesNotThrow(() -> createInsurancePolicyUseCase.create(insurancePolicy));

        verify(validator).validate(insurancePolicy, productResponse, offerResponse);
        verify(quoteRepository).save(quoteEntity);
        verify(insurancePolicyProducer).send(insurancePolicyResponse);
    }
}