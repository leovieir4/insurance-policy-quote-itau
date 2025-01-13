package itau.cotacao.seguro.usecases;

import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.exceptions.InsurancePolicyNotFoundException;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.usecases.impl.GetInsurancePolicyUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInsurancePolicyUseCaseImplTest {

    private static final Long ID_QUERY = 123L;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GetInsurancePolicyUseCaseImpl getInsurancePolicyUseCase;

    @Test
    void getInsurancePolicy_shouldReturnInsurancePolicyResponse() {
        QuoteEntity quoteEntity = new QuoteEntity();
        InsurancePolicyResponse expectedResponse = new InsurancePolicyResponse();

        when(quoteRepository.findByIdQuery(ID_QUERY)).thenReturn(quoteEntity);
        Configuration configuration = mock(Configuration.class);
        TypeMap typeMap = mock(TypeMap.class);
        when(modelMapper.getConfiguration()).thenReturn(configuration);
        when(modelMapper.map(quoteEntity, InsurancePolicyResponse.class)).thenReturn(expectedResponse);
        when(modelMapper.typeMap(any(), any())).thenReturn(typeMap);

        InsurancePolicyResponse actualResponse = getInsurancePolicyUseCase.getInsurancePolicy(ID_QUERY);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getInsurancePolicy_noQuoteFound_shouldThrowInsurancePolicyNotFoundException() {
        when(quoteRepository.findByIdQuery(ID_QUERY)).thenReturn(null);

        assertThrows(InsurancePolicyNotFoundException.class, () -> getInsurancePolicyUseCase.getInsurancePolicy(ID_QUERY));
    }
}