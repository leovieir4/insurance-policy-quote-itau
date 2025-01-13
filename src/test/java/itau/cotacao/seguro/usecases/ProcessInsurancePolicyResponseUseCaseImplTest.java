package itau.cotacao.seguro.usecases;

import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyListenerRequest;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.usecases.ProcessInsurancePolicyResponseUseCase;
import itau.cotacao.seguro.usecases.impl.ProcessInsurancePolicyResponseUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessInsurancePolicyResponseUseCaseImplTest {

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private ProcessInsurancePolicyResponseUseCaseImpl processInsurancePolicyResponseUseCase;

    @Test
    void process_shouldUpdateQuoteEntity() {
        InsurancePolicyListenerRequest request = new InsurancePolicyListenerRequest("123", 456L);
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setIdQuery(123L);

        when(quoteRepository.findByIdQuery(123L)).thenReturn(quoteEntity);

        processInsurancePolicyResponseUseCase.process(request);

        verify(quoteRepository).findByIdQuery(123L);
        verify(quoteRepository).save(quoteEntity);
    }

    @Test
    void process_quoteNotFound_shouldLogWarning() {
        InsurancePolicyListenerRequest request = new InsurancePolicyListenerRequest("123", 456L);

        when(quoteRepository.findByIdQuery(123L)).thenReturn(null);

        processInsurancePolicyResponseUseCase.process(request);

        verify(quoteRepository).findByIdQuery(123L);
        verify(quoteRepository, never()).save(any());
    }
}